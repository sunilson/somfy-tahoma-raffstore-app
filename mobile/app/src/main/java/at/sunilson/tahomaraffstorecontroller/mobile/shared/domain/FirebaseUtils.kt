package at.sunilson.tahomaraffstorecontroller.mobile.shared.domain

import at.sunilson.tahomaraffstorecontroller.mobile.shared.data.FirebaseChildEvent
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.entities.EntityWithId
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object FirebaseUtils {

    inline fun <reified T> DatabaseReference.getChildEventsFlow() = callbackFlow {
        val listener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                trySendBlocking(FirebaseChildEvent.Added(snapshot.key!!, snapshot.getValue(T::class.java) ?: return))
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                trySendBlocking(FirebaseChildEvent.Changed(snapshot.key!!, snapshot.getValue(T::class.java) ?: return))
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                trySendBlocking(FirebaseChildEvent.Removed(snapshot.key!!, snapshot.getValue(T::class.java) ?: return))
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Egal
            }

            override fun onCancelled(error: DatabaseError) {
                channel.close()
            }
        }

        addChildEventListener(listener)
        awaitClose { removeEventListener(listener) }
    }

    inline fun <reified T : EntityWithId> DatabaseReference.getChildListFlow() = flow {
        val deviceList = mutableListOf<T>()
        getChildEventsFlow<T>().collect { event ->
            when (event) {
                is FirebaseChildEvent.Added -> deviceList.add(event.value)
                is FirebaseChildEvent.Removed -> deviceList.removeIf { it.id == event.value.id }
                is FirebaseChildEvent.Changed -> deviceList.apply {
                    val indexOfDevice = indexOfFirst { it.id == event.value.id }
                    set(indexOfDevice, event.value)
                }
            }
            emit(deviceList)
        }
    }

    suspend inline fun <reified T> DatabaseReference.getListSuspending() = suspendCancellableCoroutine { cont ->
        get()
            .addOnSuccessListener { cont.resume(it.children.mapNotNull { it.getValue(T::class.java) }) }
            .addOnFailureListener { cont.resumeWithException(it) }
            .addOnCanceledListener { cont.cancel() }
    }

    suspend inline fun <reified T> DatabaseReference.getSuspending() = suspendCancellableCoroutine { cont ->
        get()
            .addOnSuccessListener { cont.resume(it.getValue(T::class.java)) }
            .addOnFailureListener { cont.resumeWithException(it) }
            .addOnCanceledListener { cont.cancel() }
    }

    suspend fun <T> DatabaseReference.setValueSuspending(value: T) = suspendCancellableCoroutine { cont ->
        setValue(value)
            .addOnSuccessListener { cont.resume(Unit) }
            .addOnFailureListener { cont.resumeWithException(it) }
            .addOnCanceledListener { cont.cancel() }
    }

    suspend fun DatabaseReference.removeValueSuspending() = suspendCancellableCoroutine { cont ->
        removeValue()
            .addOnSuccessListener { cont.resume(Unit) }
            .addOnFailureListener { cont.resumeWithException(it) }
            .addOnCanceledListener { cont.cancel() }
    }

    fun String.sanitizeFirebaseId() = filterNot { it == '/' || it == ':' }

}