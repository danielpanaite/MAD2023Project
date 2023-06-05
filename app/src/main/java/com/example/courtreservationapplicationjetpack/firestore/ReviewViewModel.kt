package com.example.courtreservationapplicationjetpack.firestore

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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


    //----------------------Methods----------------------


    // prende le review dell'utente
    // [Review(id=1T3aVCXrCX1BkOLMDAAN, user=provaai98lz@gmail.com, court=document10, date=Timestamp(seconds=1685800800, nanoseconds=521000000), review=ciao3, rating=3), Review(id=Zgl5Rpsl3I1bxPGHbapl, user=provaai98lz@gmail.com, court=document122, date=Timestamp(seconds=1685955095, nanoseconds=777000000), review=ppppp, rating=2), Review(id=pK59DVGsaTvklZhlGodj, user=provaai98lz@gmail.com, court=document136, date=Timestamp(seconds=1685951387, nanoseconds=572000000), review=prova, rating=1), Review(id=wZ0DnU2cdyxtjVNm4bS9, user=provaai98lz@gmail.com, court=document103, date=Timestamp(seconds=1685899692, nanoseconds=203000000), review=wowwasas, rating=5)]
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
            Log.d("review list", "$list")
        }.addOnFailureListener {
            Log.d(TAG, "Error getting data", it)
        }
    }


    // get all reservation of the user
    //[Reservation(id=Saxfm6sRw18ORIrz9uVe, user=provaai98lz@gmail.com, court=document20, date=Timestamp(seconds=1685613600, nanoseconds=78000000), notes=no, people=3), Reservation(id=051w64Nj3E2wFDGpcRBh, user=provaai98lz@gmail.com, court=document122, date=Timestamp(seconds=1685613600, nanoseconds=425000000), notes=niente, people=1), Reservation(id=m81yTM1xgJQi8k4nYFqY, user=provaai98lz@gmail.com, court=document10, date=Timestamp(seconds=1685862000, nanoseconds=0), notes=, people=1), Reservation(id=V37gfxuxwVr5yWVT4vy9, user=provaai98lz@gmail.com, court=document136, date=Timestamp(seconds=1685894400, nanoseconds=0), notes=, people=1), Reservation(id=sho0LUzSEwUVe8wXWi0b, user=provaai98lz@gmail.com, court=document122, date=Timestamp(seconds=1685894400, nanoseconds=0), notes=, people=1), Reservation(id=GOK9Hgq2wrTgbpf1bsVA, user=provaai98lz@gmail.com, court=document103, date=Timestamp(seconds=1685944800, nanoseconds=0), notes=, people=2)]
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
            Log.d("reservation ui state", "$list")

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
                    Log.d("reservationDate NEWDATe", "$newDate")
                    Log.d("now", "${Timestamp.now().toDate()}")
                    Log.d("nonewDate < Timestamp.now().toDate()w", "${newDate < Timestamp.now().toDate()}")
//reservationDate NEWDATe   D  Wed May 31 00:00:00 GMT+02:00 2023
//2023-06-05 11:00:14.590 32626-32626 now                      Mon Jun 05 11:00:14 GMT+02:00 2023
//2023-06-05 11:00:14.591 32626-32626 nonewDate ....toDate()w  D  true
                    newDate < Timestamp.now().toDate()
                }
                .map { it.court }
                .distinct()

            Log.d("filteredReservations", "$filteredReservations")
            //[document20, document122, document10, document136, document122, document103]
            // dopo distinct: [document20, document122, document10, document136, document103]

            getCourtsWithIds(filteredReservations)
                .collect { courtList ->
                    val courtListWithId = courtList.map { CourtWithId(it.idCourt, it.court) }
                    _reservationCourtsState.value = ReservationCourtsState(courtListWithId)
                    Log.d("courtList", "$courtList")
                    //[CourtWithId(idCourt=document20, court=Court(URL=https://pixabay.com/get/g4be0d9a560d96ed69d78ec52b2ce1162168c1c389d7fd88edfc622cb5867dc1496d7c82325a1b5bcc1378b8fcc03c1ff02787bcdb7bd4f35a594d8860060b6f4_1280.jpg, id=, name=A.S.D. CITTA' DI ASTI CALCIO A 5, address=Via Antonio Badoni, 23, Asti, center=Asti, sport=calcio, capacity=22)), CourtWithId(idCourt=document122, court=Court(URL=https://pixabay.com/get/gcf67778885b12cb712dec7437c6db908905f9245478912d54552ed8a269ef4847b8abbb68737a7d89098aeb21b6b3592ec5cb2037031aabf9292bf38a373ea70_1280.jpg, id=, name=Don Bosco Tennis Club, address=Corso Dante Alighieri, 186, Asti, center=Asti, sport=tennis, capacity=4)), CourtWithId(idCourt=document10, court=Court(URL=https://pixabay.com/get/g86bbbba6f81f50dc6a3b3ccc2a4ced32d254ff5b137f6d4ef5303703a965157750bcd78320974efa5145a3bccf32daa878bb02d8cd8849f254b6edc34db4a31f_1280.jpg, id=, name=Almese CALCIO, address=Via Granaglie, 30, Almese, center=Almese, sport=calcio, capacity=22)), CourtWithId(idCourt=document136, court=Court(URL=https://pixabay.com/get/g79551f18d6106901da1d91375a1176e9212d031607cab29a50435d35ef82de793eed80573230419007a5671d5870b32bbe1dd36678455ba8fb923924072bde6b_1280.jpg, id=, name=Tennis De Coubertin, address=Via Giovanni Tommaso Terraneo, 11/A, Torino, center=Torino, sport=tennis, capacity=4)), CourtWithId(idCourt=document122, court=Court(URL=https://pixabay.com/get/gcf67778885b12cb712dec7437c6db908905f9245478912d54552ed8a269ef4847b8abbb68737a7d89098aeb21b6b3592ec5cb2037031aabf9292bf38a373ea70_1280.jpg, id=, name=Don Bosco Tennis Club, address=Corso Dante Alighieri, 186, Asti, center=Asti, sport=tennis, capacity=4)), CourtWithId(idCourt=document103, court=Court(URL=https://pixabay.com/get/gf4b2efce48afb41e149a9dd8f0638c800571618311f0c193de9d5d3c1a0ebea87f243abe4ce2ad913624bd86a4f2df0451ff09fd4f86a63c3bc033b95cb9f297_1280.jpg, id=, name=la loggia softball, address=Via Don Luigi Sturzo, 40, La Loggia, center=La Loggia, sport=softball, capacity=18))]
                }
        }
    }


    fun getCourtsWithIds(courts: List<String>): Flow<List<CourtWithId>> = callbackFlow {
        val courtCollection = db.collection("courts")

        val courtList = mutableListOf<CourtWithId>()

        for (courtId in courts) {
            Log.d("courtId in courts getCourtsWithIds", "$courtId")

            val courtDoc = courtCollection.document(courtId)
            val courtSnapshot = courtDoc.get().await()

            if (courtSnapshot.exists()) {
                val court = courtSnapshot.toObject(Court::class.java)
                court?.let { courtList.add(CourtWithId(courtId, it)) }
                Log.d("court getCourtsWithIds", "$court")
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
            _reviewUiState.value = ReviewUiState(rev, isEntryValid = false)
            Log.d("list reviewscourtEmail", "$rev")

            // reviewUiState: MyReviewsUiState(reviewList=[Review(id=1T3aVCXrCX1BkOLMDAAN, user=provaai98lz@gmail.com, court=document10, date=Timestamp(seconds=1685800800, nanoseconds=521000000), review=ciao3, rating=3), Review(id=Zgl5Rpsl3I1bxPGHbapl, user=provaai98lz@gmail.com, court=document122, date=Timestamp(seconds=1685955095, nanoseconds=777000000), review=ppppp, rating=2), Review(id=pK59DVGsaTvklZhlGodj, user=provaai98lz@gmail.com, court=document136, date=Timestamp(seconds=1685951387, nanoseconds=572000000), review=prova, rating=1), Review(id=wZ0DnU2cdyxtjVNm4bS9, user=provaai98lz@gmail.com, court=document103, date=Timestamp(seconds=1685899692, nanoseconds=203000000), review=wowwasas, rating=5)], isLoading=false)
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
            Log.d("court Ui State ", "$courtList")
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

           //val esisteRef =
           //     db.collection("reviews").whereEqualTo("user", reviewUiState.value.review.user)
            //        .whereEqualTo("court", reviewUiState.value.review.court).whereLessThan("date", Timestamp.now())

            val esisteRef = db.collection("revies").whereEqualTo("id", reviewUiState.value.review.id)
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
                            "idReservation" to reviewUiState.value.review.idReservation,

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