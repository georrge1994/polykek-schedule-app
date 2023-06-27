package com.android.core.ui.mvi

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.android.core.ui.fragments.BaseFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.plus

/**
 * MVI-fragment promises to implement states for at least one viewModel.
 *
 * @param I Group of input-intents for viewModel
 * @param S Group of output-states from viewModel
 * @param A Group of output-single-actions from viewModel
 * @param VM ViewModel
 * @constructor Create empty constructor for mvi fragment
 */
abstract class MviFragment<I : MviIntent, S : MviState, A : MviAction, VM : MviViewModel<I, S, A>> : BaseFragment() {
    protected lateinit var viewModel: VM
    private var stateAndActionScope: CoroutineScope? = null

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreatedBeforeRendering(view, savedInstanceState)
        (lifecycleScope + Job()).apply {
            viewModel.state.onEach(::invalidateUi).launchIn(this)
            viewModel.action.onEach(::executeSingleAction).launchIn(this)
            stateAndActionScope = this
        }
    }

    /**
     * "On view created before rendering" - need to save the old name, but also need to control the state subscription.
     * Some compromise solution.
     *
     * @param view View
     * @param savedInstanceState Saved instance state
     */
    protected open fun onViewCreatedBeforeRendering(view: View, savedInstanceState: Bundle?) {}

    /**
     * Invalidate UI depends to [state].
     *
     * @param state State
     */
    protected open fun invalidateUi(state: S) {}

    /**
     * Execute single action.
     *
     * @param action Mvi action
     */
    protected open fun executeSingleAction(action: A) {}

    /**
     * Emit intent.
     *
     * @receiver [I]
     */
    protected open fun I.dispatchIntent() = viewModel.dispatchIntentAsync(this)

    override fun onDestroyView() {
        super.onDestroyView()
        stateAndActionScope?.cancel()
    }
}