package com.android.core.ui.newUI.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewModelScope
import com.android.core.ui.mvi.MviAction
import com.android.core.ui.mvi.MviIntent
import com.android.core.ui.mvi.MviState
import com.android.core.ui.mvi.MviViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

/**
 * If [condition] is true, apply [ifTrue].
 *
 * @receiver [Modifier]
 * @param condition Condition
 * @param ifTrue If true
 * @return [Modifier]
 */
fun Modifier.conditional(
    condition: Boolean,
    ifTrue: Modifier.() -> Modifier
): Modifier {
    return if (condition) {
        ifTrue()
    } else {
        this
    }
}

/**
 * If [condition] is true, apply [ifTrue] otherwise [ifFalse].
 *
 * @receiver [Modifier]
 * @param condition Condition
 * @param ifTrue If true
 * @param ifFalse If false
 * @return [Modifier]
 */
fun Modifier.conditional(
    condition: Boolean,
    ifTrue: Modifier.() -> Modifier,
    ifFalse: (Modifier.() -> Modifier)? = null
): Modifier {
    return if (condition) {
        ifTrue()
    } else if (ifFalse != null) {
        ifFalse()
    } else {
        this
    }
}

/**
 * Init simple disposable effect.
 *
 * @param lifecycleOwner Lifecycle owner
 * @param onStartAction On start action
 * @param onStopAction On stop action
 * @return [DisposableEffect]
 */
@Composable
fun InitDisposableEffect(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onStartAction: () -> Unit = {},
    onStopAction: () -> Unit = {},
) = DisposableEffect(lifecycleOwner) {
    val observer = LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_START -> onStartAction()
            Lifecycle.Event.ON_STOP -> onStopAction()
            else -> {}
        }
    }
    lifecycleOwner.lifecycle.addObserver(observer)

    onDispose {
        lifecycleOwner.lifecycle.removeObserver(observer)
    }
}

/**
 * This syntax sugar function allows to subscribe and unsubscribe from the view model flows by lifecycle of input owner.
 *
 * @receiver [MviViewModel]
 * @param I Type of intent
 * @param S Type of state
 * @param A Action of state
 * @param lifecycleOwner Lifecycle owner
 * @param onStartCallback Additional action by start
 * @param onStopCallback Additional action by stop
 * @return [DisposableEffect]
 */
@Composable
fun <I : MviIntent, S : MviState, A : MviAction> MviViewModel<I, S, A>.InitDisposableSubscriptionEffect(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onStartCallback: () -> Unit = {},
    onStopCallback: () -> Unit = {},
    mviSingleActionCallback: (A) -> Unit = {},
) = DisposableEffect(lifecycleOwner) {
    var scope: CoroutineScope? = null
    val observer = LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_START -> {
                (viewModelScope + Job()).launch {
                    scope = this
                    action.collect {
                        mviSingleActionCallback(it)
                    }
                }
                onStartCallback()
            }
            Lifecycle.Event.ON_STOP -> {
                scope?.cancel()
                scope = null
                onStopCallback()
            }
            else -> {}
        }
    }
    lifecycleOwner.lifecycle.addObserver(observer)

    onDispose {
        lifecycleOwner.lifecycle.removeObserver(observer)
    }
}