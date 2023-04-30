package at.sunilson.tahomaraffstorecontroller.mobile.shared.presentation.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

@Composable
fun OnResume(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onResume: () -> Unit
) {
    OnLifecycleEvent(
        lifecycleOwner = lifecycleOwner,
        onEvent = { if (it == Lifecycle.Event.ON_RESUME) onResume() }
    )
}

@Composable
fun OnPause(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onPause: () -> Unit
) {
    OnLifecycleEvent(
        lifecycleOwner = lifecycleOwner,
        onEvent = { if (it == Lifecycle.Event.ON_PAUSE) onPause() }
    )
}

@Composable
fun OnLifecycleEvent(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onEvent: (Lifecycle.Event) -> Unit
) {
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event -> onEvent(event) }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
}
