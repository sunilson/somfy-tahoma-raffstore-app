package at.sunilson.tahomaraffstorecontroller.mobile.features.groups.domain

import at.sunilson.tahomaraffstorecontroller.mobile.features.database.TahomaLocalDatabase
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FirebaseUtils.removeValueSuspending
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase
import com.google.firebase.database.FirebaseDatabase

class DeleteActionGroup(private val firebaseDatabase: FirebaseDatabase) : UseCase<String, Unit>() {
    override suspend fun doWork(params: String) {
        firebaseDatabase.reference.child("groups/$params").removeValueSuspending()
    }
}