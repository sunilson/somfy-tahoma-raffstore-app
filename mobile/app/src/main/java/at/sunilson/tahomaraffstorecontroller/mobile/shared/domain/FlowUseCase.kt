package at.sunilson.tahomaraffstorecontroller.mobile.shared.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

abstract class FlowUseCase<in P, out R>(private val dispatcher: CoroutineDispatcher = Dispatchers.Default) {

    operator fun invoke(params: P) = execute(params)

    fun execute(params: P): Flow<R> {
        return doWork(params).flowOn(dispatcher)
    }

    protected abstract fun doWork(params: P): Flow<R>
}