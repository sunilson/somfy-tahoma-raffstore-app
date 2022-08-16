package at.sunilson.tahomaraffstorecontroller.base

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Suppress("MemberVisibilityCanBePrivate")
abstract class UseCase<in P, out R>(private val dispatcher: CoroutineDispatcher = Dispatchers.Default) {

    suspend operator fun invoke(params: P) = execute(params)

    suspend fun execute(params: P): Result<R> {
        return withContext(dispatcher) {
            try {
                Result.success(doWork(params))
            } catch (error: Throwable) {
                if (error is CancellationException) throw error
                Result.failure(error)
            }
        }
    }

    protected abstract suspend fun doWork(params: P): R

    object NoParams
}