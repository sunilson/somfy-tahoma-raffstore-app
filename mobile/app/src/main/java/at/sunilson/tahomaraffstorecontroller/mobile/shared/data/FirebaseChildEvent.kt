package at.sunilson.tahomaraffstorecontroller.mobile.shared.data

sealed interface FirebaseChildEvent<T : Any> {
    val key: String
    val value: T

    data class Added<T : Any>(override val key: String, override val value: T) : FirebaseChildEvent<T>
    data class Removed<T : Any>(override val key: String, override val value: T) : FirebaseChildEvent<T>
    data class Changed<T : Any>(override val key: String, override val value: T) : FirebaseChildEvent<T>
}