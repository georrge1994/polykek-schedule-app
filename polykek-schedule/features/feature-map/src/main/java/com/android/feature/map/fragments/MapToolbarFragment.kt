package com.android.feature.map.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.core.ui.fragments.NavigationFragment
import com.android.core.ui.navigation.polytechCicirone.PolytechFragmentScreen
import com.android.feature.map.R
import com.android.feature.map.dagger.IMapNavigationActions
import com.android.feature.map.dagger.MapComponentHolder
import com.android.feature.map.databinding.FragmentToolbarMapBinding
import com.android.feature.map.models.Content
import com.android.feature.map.useCases.ContentAnimationUiUseCase
import com.android.feature.map.viewModels.MapViewModel
import com.android.shared.code.utils.syntaxSugar.createSharedViewModelWithParentFragment
import javax.inject.Inject

/**
 * Map toolbar fragment.
 *
 * @constructor Create empty constructor for map toolbar fragment
 */
internal class MapToolbarFragment : NavigationFragment() {
    private val viewBinding by viewBinding(FragmentToolbarMapBinding::bind)
    private lateinit var mapViewModel: MapViewModel

    @Inject
    lateinit var contentAnimationUiUseCase: ContentAnimationUiUseCase

    @Inject
    lateinit var mapNavigationActions: IMapNavigationActions

    private val searchBtnListener = View.OnClickListener { showBuildingSearch() }

    private val focusBtnListener = View.OnClickListener { mapViewModel.refreshMapFocusByBoundingBox() }

    private val selectedMapItemObserver = Observer<Content?> { content ->
        if (content != null) {
            bindContentWithAnimation(content)
        } else {
            contentAnimationUiUseCase.hideContent(viewBinding.mapToolbar, viewBinding.contentLayout.root)
        }
    }

    override fun injectToComponent() = MapComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapViewModel = createSharedViewModelWithParentFragment(viewModelFactory)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_toolbar_map, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapViewModel.selectedMapItem.observe(viewLifecycleOwner, selectedMapItemObserver)
        viewBinding.searchBtn.setOnClickListener(searchBtnListener)
        viewBinding.focusBtn.setOnClickListener(focusBtnListener)
    }

    /**
     * Bind content with animation.
     *
     * @param content Content
     */
    private fun bindContentWithAnimation(content: Content) = with(viewBinding.contentLayout) {
        title.setTextOrHideNull(content.title)
        opposingTitle.setTextOrHideNull(content.opposingTitle)
        subTitle1.setTextOrHideNull(content.subTitle1)
        subTitle2.setTextOrHideNull(content.subTitle2)
        subTitle3.setTextOrHideNull(content.subTitle3)
        contentAnimationUiUseCase.showContent(viewBinding.mapToolbar, viewBinding.contentLayout.root)
    }

    /**
     * Set text or hide null.
     *
     * @receiver [TextView]
     * @param text Text
     */
    private fun TextView.setTextOrHideNull(text: String?) {
        this.isVisible = text != null
        this.text = text
    }

    /**
     * Show building search.
     */
    private fun showBuildingSearch() = tabRouter?.navigateTo(
        PolytechFragmentScreen {
            mapNavigationActions.getBuildingsScreen()
        }
    )
}