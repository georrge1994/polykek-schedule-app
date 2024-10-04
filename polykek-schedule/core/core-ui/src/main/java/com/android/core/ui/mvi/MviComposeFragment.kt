package com.android.core.ui.mvi

import androidx.lifecycle.lifecycleScope
import com.android.core.ui.fragments.BaseFragment
import com.android.core.ui.navigation.ICiceroneHolder
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * MVI-fragment promises to implement states for at least one viewModel.
 *
 * This fragment is designed to work with Compose UI.
 *
 * @param I Group of input-intents for viewModel
 * @param S Group of output-states from viewModel
 * @param A Group of output-single-actions from viewModel
 * @param VM ViewModel
 * @constructor Create empty constructor for mvi fragment
 */
abstract class MviComposeFragment<I : MviIntent, S : MviState, A : MviAction, VM : MviViewModel<I, S, A>> :
    BaseFragment() {
    protected lateinit var viewModel: VM

    override fun onStart() {
        super.onStart()
        viewModel.action.onEach(::executeSingleAction).launchIn(lifecycleScope)
    }

    override fun onStop() {
        super.onStop()
        lifecycleScope.coroutineContext.cancelChildren()
    }

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
}