package at.sunilson.tahomaraffstorecontroller.mobile.shared.domain

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

suspend fun doParallel(vararg blocks: suspend () -> Any) = coroutineScope {
    blocks
        .map { async { it() } }
        .forEach { it.await() }
}

suspend fun <T> Iterable<T>.forEachParallel(block: suspend (T) -> Unit) = coroutineScope {
    map { async { block(it) } }.forEach { it.await() }
}

suspend fun <T, R> Iterable<T>.mapParallel(block: suspend (T) -> R): Iterable<R> = coroutineScope {
    map { async { block(it) } }.map { it.await() }
}
