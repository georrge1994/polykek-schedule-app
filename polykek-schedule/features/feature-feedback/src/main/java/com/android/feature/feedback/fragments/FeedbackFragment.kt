package com.android.feature.feedback.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.core.ui.fragments.ToolbarFragment
import com.android.feature.feedback.R
import com.android.feature.feedback.dagger.FeedbackComponentHolder
import com.android.feature.feedback.databinding.FragmentFeedbackBinding
import com.android.feature.feedback.models.FeedbackType
import com.android.feature.feedback.viewModels.FeedbackViewModel
import com.android.module.injector.moduleMarkers.IModuleComponent
import com.android.shared.code.utils.syntaxSugar.createViewModel
import com.android.shared.code.utils.syntaxSugar.hideSoftwareKeyboard

/**
 * Feedback fragment.
 *
 * @constructor Create empty constructor for feed back fragment
 */
internal class FeedbackFragment : ToolbarFragment() {
    private val viewBinding by viewBinding(FragmentFeedbackBinding::bind)
    private lateinit var viewModel: FeedbackViewModel

    private val submitClickListener = View.OnClickListener {
        viewModel.tryToSendFeedback(viewBinding.textField.text.toString(), getFeedbackType())
    }

    private val resetNavigationObserver = Observer<Boolean> { mainRouter.exit() }

    private val isAnimatingObserver = Observer<Boolean> { viewBinding.submit.changeAnimationState(it) }

    override fun getComponent(): IModuleComponent = FeedbackComponentHolder.getComponent()

    override fun injectToComponent() = FeedbackComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel(viewModelFactory)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_feedback, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.toolbarLayout.toolbar.updateToolbar(R.string.feedback_fragment_title, true)

        viewModel.resetNavigation.observe(viewLifecycleOwner, resetNavigationObserver)
        viewModel.isLoading.observe(viewLifecycleOwner, isAnimatingObserver)
        viewBinding.submit.setOnClickListener(submitClickListener)
    }

    override fun onPause() {
        super.onPause()
        viewBinding.textField.hideSoftwareKeyboard()
    }

    /**
     * Get feedback type.
     */
    private fun getFeedbackType() = when (viewBinding.radioBtnGroup.checkedRadioButtonId) {
        R.id.bugRadioBtn -> FeedbackType.BUG
        R.id.ideaRadioBtn -> FeedbackType.IDEA
        R.id.otherRadioBtn -> FeedbackType.OTHER
        else -> FeedbackType.OTHER
    }
}