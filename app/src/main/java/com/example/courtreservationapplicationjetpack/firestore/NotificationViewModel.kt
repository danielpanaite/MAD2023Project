package com.example.courtreservationapplicationjetpack.firestore

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NotificationViewModel: ViewModel() {

    private val db = Firebase.firestore
    private lateinit var reg1: ListenerRegistration

    companion object{
        const val TAG = "NotificationViewModel"
    }

    //DATA
    private var _notifications = mutableStateOf<List<Notification>>(emptyList())
    val notifications: State<List<Notification>> = _notifications


    fun getUserNotifications(user: String) {
        val docRef = db.collection("notifications").whereEqualTo("receiver", user).whereEqualTo("status", "pending").orderBy("date", Query.Direction.DESCENDING)

        reg1 = docRef.addSnapshotListener { snapshot, e ->
            if (e != null)
                Log.d(ReservationViewModel.TAG, "Error getting data", e)
            if (snapshot != null) {
                Log.d(TAG, "getUserNotifications")
                val list = mutableListOf<Notification>()
                for (document in snapshot.documents) {
                    val res = document.toObject(Notification::class.java)
                    res?.id =
                        document.id // Map the document ID to the "id" property of the Reservation object
                    res?.let { r -> list.add(r) }
                }
                _notifications.value = list
            }
        }
    }

    fun updateNotificationStatus(id: String, status: String) {
        val docRef = db.collection("notifications").document(id)
        docRef.update("status", status)
            .addOnSuccessListener {
                docRef.id
                Log.d(TAG, docRef.id)
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to update notification status")
            }
    }

    fun createNotification(notification: Notification) {
        val docRef = db.collection("notifications")
        val hash = hashMapOf<String, Any>(
            "receiver" to notification.receiver,
            "sender" to notification.sender,
            "status" to notification.status,
            "type" to notification.type,
            "court" to notification.court,
            "reservation" to notification.reservation,
            "date" to notification.date
        )
        docRef.document(docRef.id).set(hash)
            .addOnSuccessListener {
                Log.d(TAG, "Notification ${docRef.id} created successfully")
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to create notification ${docRef.id}")
            }
    }


}