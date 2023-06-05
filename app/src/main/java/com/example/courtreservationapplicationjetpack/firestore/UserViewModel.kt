package com.example.courtreservationapplicationjetpack.firestore

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserViewModel: ViewModel(){

    private val db = Firebase.firestore
    private lateinit var reg1: ListenerRegistration
    private lateinit var reg2: ListenerRegistration
    private lateinit var reg3: ListenerRegistration
    private lateinit var reg4: ListenerRegistration

    companion object{
        const val TAG = "UserViewModel"
    }

    //DATA
    private var _users = mutableStateOf<List<Users>>(emptyList())
    private var _user = mutableStateOf(Users())
    val users: State<List<Users>> = _users
    val user: MutableState<Users> = _user

    private var _sports = mutableStateOf<List<String>>(emptyList())
    val sports: State<List<String>> = _sports

    private val _sportsPreferencesUiState = MutableStateFlow(SportsPreferencesUiState(isLoading = true))
    val sportsPreferencesUiState: StateFlow<SportsPreferencesUiState> = _sportsPreferencesUiState

    private val _achievements = MutableStateFlow(AchievementsUi(isLoading = true))
    val achievements: StateFlow<AchievementsUi> = _achievements

    //----------------------Methods----------------------

    fun getUserByEmail(email: String) {
        // Creating a reference to document by id
        val docRef = db.document("users/$email")

        // Listen to data in real-time
        reg3 = docRef.addSnapshotListener { snapshot, e ->
            if (e != null)
                Log.d(TAG, "Error getting data", e)
            if (snapshot != null) {
                Log.d(TAG, "getUserByEmail")
                val res = snapshot.toObject(Users::class.java)
                res?.id = snapshot.id // Map the document ID to the "id" property of the Reservation object
                _user.value = res!!
            }
        }
    }

    fun getUserListByEmails(emails: List<String>){
        val docRef = db.collection("users").whereIn(FieldPath.documentId(), emails)

        docRef.get().addOnSuccessListener {
            Log.d(TAG, "getUserListByEmails")
            val list = mutableListOf<Users>()
            for (document in it.documents) {
                val res = document.toObject(Users::class.java)
                res?.id = document.id // Map the document ID to the "id" property of the Reservation object
                res?.let { r -> list.add(r) }
            }
            _users.value = list
        }.addOnFailureListener {
            Log.d(TAG, "Error getting data", it)
        }
    }

    fun getUserListBySports(friends: List<String>){
        Log.d(TAG, friends.toString())
        val docRef = db.collection("users").whereNotIn(FieldPath.documentId(), friends)

        docRef.get().addOnSuccessListener {
            Log.d(TAG, "getUserListBySports")
            val list = mutableListOf<Users>()
            for (document in it.documents) {
                val res = document.toObject(Users::class.java)
                res?.id = document.id // Map the document ID to the "id" property of the Reservation object
                res?.let { r -> list.add(r) }
            }
            _users.value = list
        }.addOnFailureListener {
            Log.d(TAG, "Error getting data", it)
        }
    }

    fun uploadImageToStorage(imageUri: ByteArray) {
        val storage = FirebaseStorage.getInstance()
        val storageRef: StorageReference = storage.reference

        imageUri.let {
            val imageFileName = "profile_image_${user.value.email}.jpg"
            val imageRef = storageRef.child("profile-images").child(imageFileName)

            val uploadTask = imageRef.putBytes(imageUri)
            //val uploadTask = imageRef.putFile(Uri.parse(imageUri.toString()))

            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                imageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    // Esegui le azioni desiderate con l'URL di download dell'immagine
                    // Ad esempio, aggiorna l'URL dell'immagine nel database Firebase
                    updateImageUrlInFirebaseDatabase(downloadUri.toString())
                } else {
                    // Gestisci l'errore durante il caricamento dell'immagine
                    Log.d(TAG, "Failed to upload image: $imageRef")
                }
            }
        }
    }



    private fun updateImageUrlInFirebaseDatabase(imageUrl: String) {
        val docRef = db.collection("users").document(user.value.email)
        docRef.update("imageUri", imageUrl)
            .addOnSuccessListener {
                Log.d(TAG, "Image URL updated successfully")
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to update image URL")
            }
    }


    fun updateProfile(imageUri: ByteArray) {
        var updatedUser = user.value
        if(imageUri.isNotEmpty()){
            updatedUser = user.value.copy(imageUri = imageUri?.toString())
        }
        val docRef = db.document("users/${user.value.email}")
        docRef.set(updatedUser)
            .addOnSuccessListener {
                Log.d(TAG, "Document ${user.value.id} updated successfully")
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to update document ${user.value.id}")
            }
    }

    fun getSportsWithLevels(email: String) {
        val docRef = db.collection("users").document(email)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            val sportPreferences = documentSnapshot.toObject(Users::class.java)?.sportPreferences
            val sportsList : MutableList<Sport> = mutableListOf()

            if (sportPreferences != null) {
                for (sport in sportPreferences) {
                    val sportName = sport.sportName
                    val masteryLevel = sport.masteryLevel
                    // Esegui altre azioni necessarie con i dati dello sport
                    val sportObject = Sport(sportName, masteryLevel)
                    sportsList.add(sportObject)
                }
                _sportsPreferencesUiState.value = SportsPreferencesUiState(sportsList, isLoading = false)
            }
        }
    }

    fun getAchievements(email: String) {
        val docRef = db.collection("users").document(email)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            val achievements = documentSnapshot.toObject(Users::class.java)?.achievements
            val achievementsList : MutableList<Achievements> = mutableListOf()

            if (achievements != null) {
                for (achievement in achievements) {
                    val title = achievement.title
                    val sportName = achievement.sportName
                    val date = achievement.date
                    val description = achievement.description

                    val achievementObject = Achievements(title, sportName, date, description)
                    achievementsList.add(achievementObject)
                }
                _achievements.value = AchievementsUi(achievementsList, isLoading = false)
            }
        }
    }

    fun addFriend(email: String, friend: String) {
        val docRef = db.collection("users").document(email)
        docRef.update("friends", FieldValue.arrayUnion(friend))
            .addOnSuccessListener {
                Log.d(TAG, "Friend added successfully")
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to add friend")
            }
        val doc2Ref = db.collection("users").document(friend)
        doc2Ref.update("friends", FieldValue.arrayUnion(email))
            .addOnSuccessListener {
                Log.d(TAG, "Friend added successfully")
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to add friend")
            }
    }

    fun addAchievement(
        email: String?,
        selectedSport: String,
        date: Timestamp,
        certificateName: String,
        additionalInfo: String
    ) {
        val userRef = email?.let { db.collection("users").document(it) }
        val achievement = Achievements(
            title = certificateName,
            sportName = selectedSport,
            date = date, // Assumi che dateString sia nel formato "yyyy-MM-dd"
            description = additionalInfo
        )
        userRef?.update("achievements", FieldValue.arrayUnion(achievement))?.addOnSuccessListener {
            // L'achievement è stato aggiunto con successo
            Log.d(TAG, "New achievement inserted correctly")

        }?.addOnFailureListener { e ->
            // Gestisci eventuali errori durante l'aggiunta dell'achievement
            Log.d(TAG, "Error inserting new achievement", e)

        }
    }

    fun deleteAchievement(email: String, achievement: Achievements) {
        val userRef = db.collection("users").document(email)
        userRef.update("achievements", FieldValue.arrayRemove(achievement))
            .addOnSuccessListener {
                // Achievement deleted successfully
                Log.d(TAG, "Achievement deleted successfully")
                updateAchievements(email) // Update the list after deletion
            }
            .addOnFailureListener { e ->
                // Handle failure to delete achievement
                Log.d(TAG, "Error deleting achievement", e)
            }
    }

    private fun updateAchievements(email: String) {
        val docRef = db.collection("users").document(email)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            val achievements = documentSnapshot.toObject(Users::class.java)?.achievements
            val achievementsList: MutableList<Achievements> = mutableListOf()

            if (achievements != null) {
                for (achievement in achievements) {
                    val title = achievement.title
                    val sportName = achievement.sportName
                    val date = achievement.date
                    val description = achievement.description

                    val achievementObject = Achievements(title, sportName, date, description)
                    achievementsList.add(achievementObject)
                }
                _achievements.value = AchievementsUi(achievementsList, isLoading = false)
            }
        }
    }

    fun updateSportsPreferences(email: String, sports: List<Sport>, uncheckedSports: List<String>) {
        val userRef = db.collection("users").document(email)
        viewModelScope.launch {
            try {
                val userSnapshot = userRef.get().await()
                val user = userSnapshot.toObject(Users::class.java)

                if (user != null) {
                    val updatedSports = user.sportPreferences.toMutableList()

                    Log.d("updateUser", "$sports")

                    // Aggiorna le preferenze sportive per ogni sport nella lista
                    sports.forEach { sport ->
                        Log.d("sport in forEach", "$sport")
                        // Verifica se lo sport è già presente tra le preferenze dell'utente
                        val existingSportIndex = updatedSports.indexOfFirst { it.sportName == sport.sportName }


                        if (existingSportIndex != -1) {
                            // Lo sport esiste già, aggiorna solo il livello di competenza
                            val existingSport = updatedSports[existingSportIndex]
                            val updatedSport = existingSport.copy(masteryLevel = sport.masteryLevel)
                            updatedSports[existingSportIndex] = updatedSport
                            Log.d("updatedSports", "$updatedSport")

                        } else {
                            // Lo sport non esiste ancora, aggiungilo alla lista delle preferenze
                            updatedSports.add(Sport(sport.sportName, sport.masteryLevel))
                        }
                    }
                    Log.d("updated sports che inserisce in userRef", "$updatedSports")

                    updatedSports.removeIf { sport -> uncheckedSports.contains(sport.sportName) }

                    userRef.update("sportPreferences", updatedSports).await()
                } else {
                    // L'utente non esiste nel database
                    // Effettua le azioni appropriate
                }
            } catch (e: Exception) {
                // Gestisci eventuali errori durante l'aggiornamento dei dati
            }
        }
    }

    fun getSportsList() {
        // Creating a reference to collection
        val docRef = db.collection("courts")

        docRef.get().addOnSuccessListener {
            Log.d(TAG, "getListSport")
            val list = mutableListOf<String>()
            for (document in it.documents) {
                val res = document.toObject(Court::class.java)
                res?.id = document.id // Map the document ID to the "id" property of the Reservation object
                res?.let { r -> list.add(r.sport)}
            }
            _sports.value = list.toSet().toList()
        }.addOnFailureListener {
            Log.d(TAG, "Error getting data", it)
        }
    }


    //-----------------------------------------------------
    //allows the listener to be removed when the viewModel is destroyed
    override fun onCleared() {
        super.onCleared()
        if(this::reg1.isInitialized)
            reg1.remove()
        if(this::reg2.isInitialized)
            reg2.remove()
        if(this::reg3.isInitialized)
            reg3.remove()
        if(this::reg4.isInitialized)
            reg4.remove()
        Log.d(TAG, "Registration removed")
    }

}

data class SportsPreferencesUiState(
    val sportsList: List<Sport> = listOf(),
    val isLoading: Boolean = false // Add the isLoading property
)

data class SportPreferencesDetails(
    val sportName: String ="",
    val masteryLevel :String = "",
)


data class AchievementsUi(
    val achievementsList: List<Achievements> = listOf(),
    val isLoading: Boolean = false // Add the isLoading property
)

