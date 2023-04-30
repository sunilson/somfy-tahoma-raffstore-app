package at.sunilson.tahomaraffstorecontroller.mobile.shared.domain

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

@Suppress("MemberVisibilityCanBePrivate")
abstract class UseCase<in P, out R>(private val dispatcher: CoroutineDispatcher = Dispatchers.Default) {

    suspend operator fun invoke(params: P) = execute(params)

    suspend fun execute(params: P): Result<R> {
        return withContext(dispatcher) {
            try {
                Result.success(doWork(params))
            } catch (error: Exception) {
                if (error is CancellationException) throw error
                Timber.e(error)
                Result.failure(error)
            }
        }
    }

    protected abstract suspend fun doWork(params: P): R

    object NoParams
}