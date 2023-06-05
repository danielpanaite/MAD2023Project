package com.example.courtreservationapplicationjetpack.firestore

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.Date
import java.util.Locale

class ReviewViewModel: ViewModel() {

    private val db = Firebase.firestore
    companion object {
        const val TAG = "ReviewViewModel"
    }

    private val _myReviewsUiState = MutableStateFlow<MyReviewsUiState>(MyReviewsUiState(isLoading = true))
    val myReviewsUiState: StateFlow<MyReviewsUiState> = _myReviewsUiState

    private var _myReservationUiState = mutableStateOf<List<Reservation>>(emptyList())
    val myReservationUiState: State<List<Reservation>> = _myReservationUiState

    private var _review = mutableStateOf(Review())
    val review: MutableState<Review> = _review

    private val _reservationCourtsState = MutableStateFlow<ReservationCourtsState>(ReservationCourtsState())
    val reservationCourtsState: StateFlow<ReservationCourtsState> = _reservationCourtsState

    private val _courtUiState = MutableStateFlow<List<ReviewCourtState>>(emptyList())
    val courtUiState: StateFlow<List<ReviewCourtState>> = _courtUiState

    private lateinit var reg1: ListenerRegistration

    private val _reviewUiState = MutableStateFlow<ReviewUiState>(ReviewUiState())
    var reviewUiState: StateFlow<ReviewUiState> = _reviewUiState

    val _avg = MutableStateFlow<Float>(0F)
    var avg: MutableStateFlow<Float> = _avg

    private val _courtReviewsState = MutableStateFlow<MyReviewsUiState>(MyReviewsUiState(isLoading = false))
    val courtReviewsState: StateFlow<MyReviewsUiState> = _courtReviewsState



    //----------------------Methods----------------------


    fun getAverageRatingForCourt(courtId: String) {
        // Creating a reference to collection
       // val docRef = db.collection("reviews").whereEqualTo("court", courtId)
        val docRef = db.collection("reviews").whereEqualTo("court", courtId)

        var totalRating = 0
        var numReviews = 0

        docRef.get().addOnSuccessListener {
            Log.d(TAG, "getAverageRatingForCourt")
            for (document in it.documents) {
                val res = document.toObject(Review::class.java)
                if (res != null) {
                    totalRating += res.rating
                    numReviews++
                }
            }

            val averageRating = if (numReviews > 0) totalRating.toFloat() / numReviews.toFloat() else 0f

            _avg.value = averageRating
            Log.d(TAG, "The average rating for court $courtId is $averageRating")

        }.addOnFailureListener {
            Log.d(TAG, "Error getting data", it)
        }
    }

    fun getReviewsByCourt(courtId: String) {
        // Creating a reference to collection
        val docRef = db.collection("reviews").whereEqualTo("court", courtId)
        docRef.get().addOnSuccessListener {
            Log.d(TAG, "getReviewsByCourt")
            val list = mutableListOf<Review>()
            for (document in it.documents) {
                val res = document.toObject(Review::class.java)
                res?.id =
                    document.id // Map the document ID to the "id" property of the Review object
                res?.let { r -> list.add(r) }
            }
            // Update your state with the list of reviews for the specified court
            _courtReviewsState.value = MyReviewsUiState(list, isLoading = false)
        }.addOnFailureListener {
            Log.d(TAG, "Error getting data", it)
        }
    }

    // prende le review dell'utente
    fun getReviewByUser(email: String) {
        // Creating a reference to collection
        val docRef = db.collection("reviews").whereEqualTo("user", email)
        docRef.get().addOnSuccessListener {
            Log.d(TAG, "getReviewByUser")
            val list = mutableListOf<Review>()
            for (document in it.documents) {
                val res = document.toObject(Review::class.java)
                res?.id =
                    document.id // Map the document ID to the "id" property of the Review object
                res?.let { r -> list.add(r) }
            }
            _myReviewsUiState.value = MyReviewsUiState(list, isLoading = false)
        }.addOnFailureListener {
            Log.d(TAG, "Error getting data", it)
        }
    }

    // get all reservation of the user
    fun getReservationByEmail(user: String) {
        // Creating a reference to collection
        val docRef = db.collection("reservations").whereEqualTo("user", user).whereLessThan("date", Timestamp.now())

        docRef.get().addOnSuccessListener {
            Log.d(TAG, "getReservationByEmail")
            val list = mutableListOf<Reservation>()
            for (document in it.documents) {
                val res = document.toObject(Reservation::class.java)
                res?.id =
                    document.id // Map the document ID to the "id" property of the Review object
                res?.let { r -> list.add(r) }
            }
            _myReservationUiState.value = list
        }.addOnFailureListener {
            Log.d(TAG, "Error getting data", it)
            // forse far aprire un popup che ti dice che qualcosa è andato storto
        }
    }

    fun getCourtsWithId() {
        viewModelScope.launch {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

            val currentTimestamp = Timestamp.now()
            val currentDateString = dateFormat.format(currentTimestamp.toDate())
            Log.d("current Time Stamp getCourtsWithId", currentDateString)

            val filteredReservations = myReservationUiState.value
                .filter { reservation ->
                    val oldDate = reservation.date.toDate()
                    val nextDate = oldDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                        .minusDays(1)
                    val newDate = Date.from(nextDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
                    newDate < Timestamp.now().toDate()
                }
                .map { it.court }
                .distinct()

            getCourtsWithIds(filteredReservations)
                .collect { courtList ->
                    val courtListWithId = courtList.map { CourtWithId(it.idCourt, it.court) }
                    _reservationCourtsState.value = ReservationCourtsState(courtListWithId, isLoading = false)
                }
        }
    }

    fun getCourtsWithIds(courts: List<String>): Flow<List<CourtWithId>> = callbackFlow {
        val courtCollection = db.collection("courts")
        val courtList = mutableListOf<CourtWithId>()
        for (courtId in courts) {
            val courtDoc = courtCollection.document(courtId)
            val courtSnapshot = courtDoc.get().await()
            if (courtSnapshot.exists()) {
                val court = courtSnapshot.toObject(Court::class.java)
                court?.let { courtList.add(CourtWithId(courtId, it)) }
            }
        }
        try {
            trySend(courtList).isSuccess
        } catch (e: Exception) {
            close(e)
        }
        awaitClose { /* Non è necessaria alcuna operazione di pulizia */ }
    }

    fun getReviewByEmailCourtId(email: String, courtId: String, reservationId:String) {
        // Creating a reference to collection
        val docRef =
            db.collection("reviews")
                .whereEqualTo("user", email)
                .whereEqualTo("court", courtId)
                .whereEqualTo("idReservation", reservationId)

        var rev = Review()
        docRef.get().addOnSuccessListener {
            Log.d(TAG, "getReviewByUser")
            val list = mutableListOf<ReviewUiState>()
            for (document in it.documents) {
                val res = document.toObject(Review::class.java)
                res?.id =
                    document.id // Map the document ID to the "id" property of the Review object
                res?.let { r -> list.add(r.toReviewsUiState()) }
                if (res != null) {
                    rev = res
                }
            }
            _reviewUiState.value = ReviewUiState(rev, isEntryValid = false, isLoading = false)
        }.addOnFailureListener {
            Log.d(TAG, "Error getting data", it)
        }
    }

    private fun Review.toReviewsUiState(isEntryValid: Boolean = false): ReviewUiState =
        ReviewUiState(
            review = this,
            isEntryValid = isEntryValid
        )


    private fun validateInput(uiState: Review = reviewUiState.value.review): Boolean {
        return with(uiState) {
            (user != "" && court != "" && date != null && rating != 0 && idReservation!="")
        }
    }

    fun courtUiState(courtId: String) {
        // Creating a reference to collection
        //val docRef = db.collection("courts").whereEqualTo("court", courtId)
        Log.d("courtId", "$courtId")
        val docRef = db.document("courts/$courtId")
        val courtList = mutableListOf<ReviewCourtState>()

        docRef.get().addOnSuccessListener { documentSnapshot ->
            Log.d(TAG, "getcourt")
            val res = documentSnapshot.toObject(Court::class.java)
            res?.let { courtList.add(ReviewCourtState(CourtWithId(courtId, it))) }
            _courtUiState.value = courtList
        }.addOnFailureListener { exception ->
            Log.d(TAG, "Error getting data", exception)
        }

    }

    fun updateUiState(review: Review) {
        _reviewUiState.value = ReviewUiState(
            review = review,
            isEntryValid = validateInput(review),
            isLoading = false
        )
    }

    fun createReview() {
        if (validateInput(reviewUiState.value.review)) {
            val esisteRef = db.collection("reviews").whereEqualTo("id", reviewUiState.value.review.id)

            esisteRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val querySnapshot = task.result
                    if (!querySnapshot.isEmpty) {
                        // Recensione esistente, esegui l'aggiornamento
                        var review = reviewUiState.value.review
                        val docRef = db.document("reviews/${reviewUiState.value.review.id}")
                        docRef.set(review)
                            .addOnSuccessListener {
                                Log.d(
                                    UserViewModel.TAG,
                                    "Document ${reviewUiState.value.review.id} updated successfully"
                                )
                            }
                            .addOnFailureListener {
                                Log.d(
                                    UserViewModel.TAG,
                                    "Failed to update document ${reviewUiState.value.review.id}"
                                )
                            }
                    } else {
                        // Nessuna recensione esistente, aggiungi un nuovo documento
                        val docRef = db.collection("reviews").document()
                        // Adding the reservation data to a HashMap
                            val hash = hashMapOf<String, Any>(
                                "id" to docRef.id,
                                "user" to reviewUiState.value.review.user,
                                "court" to reviewUiState.value.review.court,
                                "date" to reviewUiState.value.review.date,
                                "review" to reviewUiState.value.review.review.toString(),
                                "rating" to reviewUiState.value.review.rating,
                                "idReservation" to reviewUiState.value.review.idReservation,
                                )
                        // Inserting the review data to Firestore
                        docRef.set(hash).addOnSuccessListener {
                            Log.d(
                                ReservationViewModel.TAG,
                                "Review ${docRef.id} inserted successfully"
                            )
                        }.addOnFailureListener {
                            Log.d(ReservationViewModel.TAG, "Failed to insert review ${docRef.id}")
                        }
                    }
                } else {
                    // Errore nella query, gestisci l'errore
                    Log.d(
                        UserViewModel.TAG,
                        "Failed to query reviews collection: ${task.exception}"
                    )
                }
            }
        }
    }

    fun deleteReview(){
        val docRef = db.document("reviews/${reviewUiState.value.review.id}")
        docRef.delete().addOnSuccessListener {
            Log.d(ReservationViewModel.TAG, "Document ${reviewUiState.value.review.id} deleted successfully")
        }.addOnFailureListener {
            Log.d(ReservationViewModel.TAG, "Failed to delete document ${reviewUiState.value.review.id}")
        }
    }
}


data class MyReviewsUiState(val reviewList: List<Review> = listOf(),  val isLoading: Boolean = true) // Add the isLoading property)

data class MyReservationsUiState(val reservationList: List<Reservation> = listOf(), val isLoading: Boolean = true)

data class ReservationCourtsState(val courtList: List<CourtWithId> = listOf(),   val isLoading: Boolean = true)

data class CourtWithId(val idCourt: String, val court: Court)

data class ReviewUiState(
    val review: Review = Review(
        "",
        "",
        "",
        Timestamp.now(),
        "",
        0
    ),
    val isEntryValid: Boolean = false,
    val isLoading: Boolean = true
)


data class ReviewCourtState(val court: CourtWithId? = null)