package com.android.feature.faq.fragments

import android.os.Bundle
import android.view.*
import androidx.transition.ChangeBounds
import androidx.transition.TransitionSet
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.core.ui.fragments.ToolbarFragment
import com.android.core.ui.navigation.polytechCicirone.PolytechFragmentScreen
import com.android.feature.faq.R
import com.android.feature.faq.adapters.FaqRecyclerViewAdapter
import com.android.feature.faq.adapters.ItemClickListener
import com.android.feature.faq.dagger.FaqComponentHolder
import com.android.feature.faq.dagger.IFaqNavigationActions
import com.android.feature.faq.databinding.FragmentFAQBinding
import com.android.feature.faq.mvi.FaqAction
import com.android.feature.faq.mvi.FaqIntent
import com.android.feature.faq.mvi.FaqState
import com.android.feature.faq.viewModels.FaqViewModel
import com.android.module.injector.moduleMarkers.IModuleComponent
import com.android.shared.code.utils.syntaxSugar.createViewModel
import javax.inject.Inject

/**
 * FAQ fragment.
 *
 * @constructor Create empty constructor for faq fragment
 */
internal class FaqFragment : ToolbarFragment<FaqIntent, FaqState, FaqAction, FaqViewModel>() {
    private val viewBinding by viewBinding(FragmentFAQBinding::bind)
    private val transitionAnimation = TransitionSet().addTransition(ChangeBounds())
    private lateinit var adapter: FaqRecyclerViewAdapter

    private val itemClickListener = object : ItemClickListener {
        override fun onClick(position: Int, isOpen: Boolean) {
            FaqIntent.ClickByItem(position, isOpen).dispatchIntent()
        }
    }

    @Inject
    lateinit var faqInnerNavigation: IFaqNavigationActions

    override fun getComponent(): IModuleComponent = FaqComponentHolder.getComponent()

    override fun injectToComponent() = FaqComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel(viewModelFactory)
        adapter = FaqRecyclerViewAdapter(requireContext(), itemClickListener)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_f_a_q, container, false)

    override fun onViewCreatedBeforeRendering(view: View, savedInstanceState: Bundle?) {
        super.onViewCreatedBeforeRendering(view, savedInstanceState)
        viewBinding.toolbarWhiteBackground.toolbar.updateToolbar(R.string.faq_fragment_title, true)
        viewBinding.recyclerView.adapter = adapter
        viewBinding.recyclerView.itemAnimator = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) =
        menuInflater.inflate(R.menu.f_a_q_menu, menu)

    override fun onMenuItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.feedback) {
            FaqIntent.OpenFeedback.dispatchIntent()
            return true
        }
        return super.onMenuItemSelected(item)
    }

    override fun invalidateUi(state: FaqState) {
        super.invalidateUi(state)
        adapter.updateItems(state.items, transitionAnimation)
    }

    override fun executeSingleAction(action: FaqAction) {
        super.executeSingleAction(action)
        if (action is FaqAction.OpenFeedback) {
            openFeedbackFragment()
        }
    }

    /**
     * Open feedback fragment.
     */
    private fun openFeedbackFragment() = mainRouter.navigateTo(
        PolytechFragmentScreen {
            faqInnerNavigation.getFeedbackScreen()
        }
    )

    override fun onDestroyView() {
        adapter.detachRecyclerView()
        viewBinding.recyclerView.adapter = null
        super.onDestroyView()
    }
}