package com.example.courtreservationapplicationjetpack.firestore

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courtreservationapplicationjetpack.views.reviews.ReviewCreateViewModel
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

    companion object {
        const val TAG = "ReviewViewModel"
    }

    private val _myReviewsUiState =
        MutableStateFlow<MyReviewsUiState>(MyReviewsUiState(isLoading = true))
    val myReviewsUiState: StateFlow<MyReviewsUiState> = _myReviewsUiState

    private var _myReservationUiState = mutableStateOf<List<Reservation>>(emptyList())
    val myReservationUiState: State<List<Reservation>> = _myReservationUiState


    private var _review = mutableStateOf(Review())
    val review: MutableState<Review> = _review


    private val _reservationCourtsState =
        MutableStateFlow<ReservationCourtsState>(ReservationCourtsState())
    val reservationCourtsState: StateFlow<ReservationCourtsState> = _reservationCourtsState


    private val _courtUiState = MutableStateFlow<List<ReviewCourtState>>(emptyList())
    val courtUiState: StateFlow<List<ReviewCourtState>> = _courtUiState


    private lateinit var reg1: ListenerRegistration


    private val _reviewUiState = MutableStateFlow<ReviewUiState>(ReviewUiState())
    var reviewUiState: StateFlow<ReviewUiState> = _reviewUiState


    //var reviewsUiState by mutableStateOf(ReviewUiState())
    //     private set

    //----------------------Methods----------------------

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


    fun getReservationByEmail(user: String) {
        // Creating a reference to collection
        val docRef = db.collection("reservations").whereEqualTo("user", user)

        docRef.get().addOnSuccessListener {
            Log.d(TAG, "getReviewByUser")
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
                    val nextDate = oldDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                        .minusDays(1)
                    val newDate =
                        Date.from(nextDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
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

    fun getReviewByEmailCourtId(email: String, courtId: String) {
        // Creating a reference to collection
        val docRef =
            db.collection("reviews").whereEqualTo("user", email).whereEqualTo("court", courtId)

        var rev = Review()
        docRef.get().addOnSuccessListener {
            Log.d(TAG, "getReviewByUser")
            //val list = mutableListOf<ReviewUiState>()
            for (document in it.documents) {
                val res = document.toObject(Review::class.java)
                res?.id =
                    document.id // Map the document ID to the "id" property of the Review object
                //res?.let { r -> list.add(r) }
                if (res != null) {
                    rev = res
                }
            }
            _reviewUiState.value = ReviewUiState(rev, isEntryValid = false)
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
            (user != "" && court != "" && date != null && review != "" && rating != 0)
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
            Log.d("courtListinvewModel", "$courtList")
        }.addOnFailureListener { exception ->
            Log.d(TAG, "Error getting data", exception)
        }

    }

    fun updateUiState(review: Review) {
        _reviewUiState.value = ReviewUiState(
            review = review,
            isEntryValid = validateInput(review)
        )
    }

    fun createReview() {
        if (validateInput(reviewUiState.value.review)) {

            val esisteRef =
                db.collection("reviews").whereEqualTo("user", reviewUiState.value.review.user)
                    .whereEqualTo("court", reviewUiState.value.review.court)

            Log.d("user esiseRef", "${reviewUiState.value.review.user}")
            Log.d("esisteRef.court", "${reviewUiState.value.review.court}")

            Log.d("esisteRef.get().isSucc", "${esisteRef.get().isSuccessful}")
            esisteRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("task.isSuccessful", "${task.isSuccessful}")
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
                            "review" to reviewUiState.value.review.review,
                            "rating" to reviewUiState.value.review.rating,
                        )
                        Log.d("id", "${docRef.id}")
                        Log.d("user", "${reviewUiState.value.review.user}")
                        Log.d("court", "${reviewUiState.value.review.court}")
                        Log.d("date", "${reviewUiState.value.review.date}")
                        Log.d("review", "${reviewUiState.value.review.review}")
                        Log.d("rating", "${reviewUiState.value.review.rating}")


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
                    // ... il resto del tuo codice per aggiungere la recensione

                } else {
                    // Errore nella query, gestisci l'errore
                    Log.d(
                        UserViewModel.TAG,
                        "Failed to query reviews collection: ${task.exception}"
                    )
                }
            }
        }

        /*
    suspend fun deleteReview(){
        reviewRepository.delete(reviewsUiState.review)
    }

     */


    }
}


data class MyReviewsUiState(val reviewList: List<Review> = listOf(),  val isLoading: Boolean = false) // Add the isLoading property)

data class MyReservationsUiState(val reservationList: List<Reservation> = listOf(), val isLoading: Boolean = false)



data class ReservationCourtsState(val courtList: List<CourtWithId> = listOf())

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
    val isEntryValid: Boolean = false
)


data class ReviewCourtState(val court: CourtWithId? = null)