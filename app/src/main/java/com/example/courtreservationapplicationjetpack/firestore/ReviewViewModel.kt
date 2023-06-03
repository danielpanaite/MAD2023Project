package com.example.courtreservationapplicationjetpack.firestore

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class ReviewViewModel: ViewModel() {

    private val db = Firebase.firestore

    companion object{
        const val TAG = "ReviewViewModel"
    }

    //DATA
    private var _reviews = mutableStateOf<List<Review>>(emptyList())
    private var _review = mutableStateOf(Review())
    val reviews: State<List<Review>> = _reviews
    val review: MutableState<Review> = _review

    private val _myReviewsUiState = MutableStateFlow<MyReviewsUiState>(MyReviewsUiState(isLoading = true))
    val myReviewsUiState: StateFlow<MyReviewsUiState> = _myReviewsUiState

    private val _courts = mutableStateOf<List<String>?>(emptyList())
    val courts: MutableState<List<String>?> = _courts

    private var _myReservationUiState = mutableStateOf<List<Reservation>>(emptyList())
    val myReservationUiState: State<List<Reservation>> = _myReservationUiState

    private val _reservationCourtsState = MutableStateFlow<ReservationCourtsState>(ReservationCourtsState())
    val reservationCourtsState: StateFlow<ReservationCourtsState> = _reservationCourtsState


    private lateinit var reg1: ListenerRegistration



    val dateFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("dd/MM/yyyy")
    //----------------------Methods----------------------

    fun getReviewByUser(email: String) {
        // Creating a reference to collection
        val docRef = db.collection("reviews").whereEqualTo("user", email)

        docRef.get().addOnSuccessListener {
            Log.d(TAG, "getReviewByUser")
            val list = mutableListOf<Review>()
            for (document in it.documents) {
                val res = document.toObject(Review::class.java)
                res?.id = document.id // Map the document ID to the "id" property of the Review object
                res?.let { r -> list.add(r) }
            }
            _myReviewsUiState.value = MyReviewsUiState(list, isLoading = false)
        }.addOnFailureListener {
            Log.d(TAG, "Error getting data", it)
        }
    }


    fun setCourts(courts: List<String>) {
        this.courts.value = courts
    }

    fun getReservationByEmail(user: String) {
        // Creating a reference to collection
        val docRef = db.collection("reservations").whereEqualTo("user", user)

        docRef.get().addOnSuccessListener {
            Log.d(TAG, "getReviewByUser")
            val list = mutableListOf<Reservation>()
            for (document in it.documents) {
                val res = document.toObject(Reservation::class.java)
                res?.id = document.id // Map the document ID to the "id" property of the Review object
                res?.let { r -> list.add(r) }
            }
            _myReservationUiState.value = list
        }.addOnFailureListener {
            Log.d(TAG, "Error getting data", it)
        }
    }

    fun getReviewById(id: String) {
        // Creating a reference to document by id
        val docRef = db.document("reviews/$id")

        docRef.get().addOnSuccessListener { documentSnapshot ->
            Log.d(TAG, "getReviewById")
            val review = documentSnapshot.toObject(Review::class.java)
            review?.id = documentSnapshot.id
            _review.value = review!!
        }.addOnFailureListener { exception ->
            Log.d(TAG, "Error getting data", exception)
        }
    }

    fun getCourtsWithIds(courts: List<String>): Flow<List<CourtWithId>> = callbackFlow {
        val courtCollection = db.collection("courts")

        val courtList = mutableListOf<CourtWithId>()

        for (courtId in courts) {
            Log.d("courtId in courts", "$courtId")

            val courtDoc = courtCollection.document(courtId)
            val courtSnapshot = courtDoc.get().await()

            if (courtSnapshot.exists()) {
                val court = courtSnapshot.toObject(Court::class.java)
                court?.let { courtList.add(CourtWithId(courtId, it)) }
                Log.d("court", "$court")
            }
        }

        try {
            trySend(courtList).isSuccess
        } catch (e: Exception) {
            close(e)
        }

        awaitClose { /* Non è necessaria alcuna operazione di pulizia */ }
    }

    fun getCourtsWithId() {
        viewModelScope.launch {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

            val currentTimestamp = Timestamp.now()
            val currentDateString = dateFormat.format(currentTimestamp.toDate())
            Log.d("currentTimeStamp", currentDateString)

            val filteredReservations = myReservationUiState.value
                .filter { reservation ->
                    val oldDate = reservation.date.toDate()
                    val nextDate = oldDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().minusDays(1)
                    val newDate = Date.from(nextDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
                    Log.d("reservationDate", "$newDate")
                    Log.d("now", "${Timestamp.now().toDate()}")

                    newDate < Timestamp.now().toDate()
                }
                .map { it.court }

            Log.d("filteredReservations", "$filteredReservations")

            getCourtsWithIds(filteredReservations)
                .collect { courtList ->
                    val courtListWithId = courtList.map { CourtWithId(it.idCourt, it.court) }
                    _reservationCourtsState.value = ReservationCourtsState(courtListWithId)
                    Log.d("courtList", "$courtList")
                }
        }
    }









    val reservationFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("dd/MM/yyyy")





}



data class MyReviewsUiState(val reviewList: List<Review> = listOf(),  val isLoading: Boolean = false) // Add the isLoading property)

data class MyReservationsUiState(val reservationList: List<Reservation> = listOf(), val isLoading: Boolean = false)



data class ReservationCourtsState(val courtList: List<CourtWithId> = listOf())

data class CourtWithId(val idCourt: String, val court: Court)