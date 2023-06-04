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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.ZoneId
import java.util.Date
import kotlin.coroutines.CoroutineContext

class ReservationViewModel: ViewModel(), CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val courtReservationsMap = mutableMapOf<String, List<Reservation>>()
    private val courtReservationsMap2: MutableMap<String, LiveData<List<Reservation>>> = mutableMapOf()


    private val db = Firebase.firestore
    private lateinit var reg1: ListenerRegistration
    private lateinit var reg2: ListenerRegistration
    private lateinit var reg3: ListenerRegistration

    companion object{
        const val TAG = "ReservationViewModel"
    }

    //DATA
    private var _reservations = mutableStateOf<List<Reservation>>(emptyList())
    private var _courtres = mutableStateOf<List<Reservation>>(emptyList())
    private var _reservation = mutableStateOf(Reservation())
    val reservations: State<List<Reservation>> = _reservations
    val reservation: MutableState<Reservation> = _reservation
    val courtres: State<List<Reservation>> = _courtres

    //----------------------Methods----------------------

    fun getUserReservations(user: String) {
        // Creating a reference to collection
        val docRef = db.collection("reservations")
            .whereEqualTo("user", user)
            .whereGreaterThan("date", Timestamp.now())

        // Listen to data in real-time
        reg1 = docRef.addSnapshotListener { snapshot, e ->
            if (e != null)
                Log.d(TAG, "Error getting data", e)
            if (snapshot != null) {
                Log.d(TAG, "getUserReservations")
                val resList = mutableListOf<Reservation>()
                for (document in snapshot.documents) {
                    val res = document.toObject(Reservation::class.java)
                    res?.id = document.id // Map the document ID to the "id" property of the Reservation object
                    res?.let { resList.add(it) }
                }
                _reservations.value = resList
            }
        }
    }

    @Synchronized
    fun getCourtReservations(court: String, date: Timestamp) {
        val oldDate = date.toDate()
        val nextDate = oldDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1)
        val newDate = Date.from(nextDate.atStartOfDay(ZoneId.systemDefault()).toInstant())

        // Creare una chiave univoca per identificare la combinazione di campo e data della corte
        val cacheKey = "$court-$date"

        // Utilizzare un blocco sincronizzato per evitare sovrapposizioni tra diversi item
        synchronized(this) {
            // Verificare se le prenotazioni per la combinazione di corte e data sono già presenti nella mappa
            val cachedReservations = courtReservationsMap[cacheKey]
            if (cachedReservations != null) {
                _courtres.value = cachedReservations
                return
            }

            // Creazione di un riferimento alla collezione
            val docRef = db.collection("reservations")
                .whereEqualTo("court", court)
                .whereGreaterThan("date", date)
                .whereLessThan("date", Timestamp(newDate))

            // Utilizzo delle coroutine per eseguire la query Firestore in modo asincrono
            viewModelScope.launch {
                try {
                    val snapshot = withContext(Dispatchers.IO) { docRef.get().await() }
                    if (snapshot != null) {
                        val resList = mutableListOf<Reservation>()
                        for (document in snapshot.documents) {
                            val res = document.toObject(Reservation::class.java)
                            res?.id = document.id // Mappare l'ID del documento alla proprietà "id" dell'oggetto Reservation
                            res?.let { resList.add(it) }
                        }

                        // Memorizzare le prenotazioni nella mappa
                        courtReservationsMap[cacheKey] = resList

                        // Aggiornare lo stato
                        _courtres.value = resList
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "Error getting data", e)
                }
            }
        }
    }

    fun getCourtReservations2(court: String, date: Timestamp): LiveData<List<Reservation>> {
        val cacheKey = "$court-$date"

        val cachedReservations = courtReservationsMap2[cacheKey]
        if (cachedReservations != null) {
            return cachedReservations
        }

        val reservationsLiveData = MutableLiveData<List<Reservation>>()
        courtReservationsMap2[cacheKey] = reservationsLiveData

        val oldDate = date.toDate()
        val nextDate = oldDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1)
        val newDate = Date.from(nextDate.atStartOfDay(ZoneId.systemDefault()).toInstant())

        val docRef = db.collection("reservations")
            .whereEqualTo("court", court)
            .whereGreaterThan("date", date)
            .whereLessThan("date", Timestamp(newDate))

        viewModelScope.launch {
            try {
                val snapshot = withContext(Dispatchers.IO) { docRef.get().await() }
                if (snapshot != null) {
                    val resList = mutableListOf<Reservation>()
                    for (document in snapshot.documents) {
                        val res = document.toObject(Reservation::class.java)
                        res?.id = document.id
                        res?.let { resList.add(it) }
                    }

                    reservationsLiveData.value = resList
                }
            } catch (e: Exception) {
                Log.d(TAG, "Error getting data", e)
            }
        }

        return reservationsLiveData
    }



    fun getReservationById(id: String) {
        // Creating a reference to document by id
        val docRef = db.document("reservations/$id")

        // Listen to data in real-time
        reg3 = docRef.addSnapshotListener { snapshot, e ->
            if (e != null)
                Log.d(TAG, "Error getting data", e)
            if (snapshot != null) {
                Log.d(TAG, "getReservationById")
                val res = snapshot.toObject(Reservation::class.java)
                res?.id = snapshot.id // Map the document ID to the "id" property of the Reservation object
                _reservation.value = res!!
            }
        }
    }

    fun updateReservation(){
        // Creating a reference to document by id
        val docRef = db.document("reservations/${reservation.value.id}")

        val hash = hashMapOf<String, Any>(
            "user" to reservation.value.user,
            "court" to reservation.value.court,
            "date" to reservation.value.date,
            "notes" to reservation.value.notes,
            "people" to reservation.value.people
        )

        docRef.update(hash).addOnSuccessListener {
            Log.d(TAG, "Document ${reservation.value.id} updated successfully")
        }.addOnFailureListener {
            Log.d(TAG, "Failed to update document ${reservation.value.id}")
        }
    }
    fun insertReservation(reservation: Reservation) {
        // Creating a reference to the collection "reservations" and generating a new document id
        val docRef = db.collection("reservations").document()

        // Adding the reservation data to a HashMap
        val hash = hashMapOf<String, Any>(
            "id" to docRef.id,
            "user" to reservation.user,
            "court" to reservation.court,
            "date" to reservation.date,
            "notes" to reservation.notes,
            "people" to reservation.people
        )

        // Inserting the reservation data to Firestore
        docRef.set(hash).addOnSuccessListener {
            Log.d(TAG, "Reservation ${docRef.id} inserted successfully")
        }.addOnFailureListener {
            Log.d(TAG, "Failed to insert reservation ${docRef.id}")
        }
    }



    fun deleteReservation(){
        reg3.remove()
        // Creating a reference to document by id
        val docRef = db.document("reservations/${reservation.value.id}")

        docRef.delete().addOnSuccessListener {
            Log.d(TAG, "Document ${reservation.value.id} deleted successfully")
        }.addOnFailureListener {
            Log.d(TAG, "Failed to delete document ${reservation.value.id}")
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
        Log.d(TAG, "Registration removed")
    }

}