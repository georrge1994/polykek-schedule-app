package com.android.feature.main.screen.menu.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.core.ui.mvi.MviFragment
import com.android.feature.main.screen.R
import com.android.feature.main.screen.dagger.MainScreenComponentHolder
import com.android.feature.main.screen.databinding.FragmentBottomSheetBinding
import com.android.feature.main.screen.menu.mvi.MenuAction
import com.android.feature.main.screen.menu.mvi.MenuIntent
import com.android.feature.main.screen.menu.mvi.MenuState
import com.android.feature.main.screen.menu.viewModels.BottomAnimationViewModel
import com.android.feature.main.screen.menu.wrappers.OwnBottomSheetCallback
import com.android.module.injector.moduleMarkers.IModuleComponent
import com.android.shared.code.utils.syntaxSugar.createViewModel
import com.android.shared.code.utils.syntaxSugar.isPortraitMode
import com.google.android.material.bottomsheet.BottomSheetBehavior

/**
 * Bottom sheet fragment.
 *
 * @constructor Create empty constructor for bottom sheet fragment
 */
internal class BottomSheetFragment : MviFragment<MenuIntent, MenuState, MenuAction, BottomAnimationViewModel>() {
    private val viewBinding by viewBinding(FragmentBottomSheetBinding::bind)
    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null
    private var green: Int = 0
    private var grey500: Int = 0
    private var white: Int = 0

    private val moreClickListener = View.OnClickListener {
        MenuIntent.ChangeStateOfBottomBar(getReverseState()).dispatchIntent()
    }

    private val sheetSlideListener = object : OwnBottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            MenuIntent.UpdateUiByOffset(slideOffset).dispatchIntent()
        }
    }

    override fun getComponent(): IModuleComponent = MainScreenComponentHolder.getComponent()

    override fun injectToComponent() = MainScreenComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as AppCompatActivity).createViewModel(viewModelFactory)
        green = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        grey500 = ContextCompat.getColor(requireContext(), R.color.grey_500)
        white = ContextCompat.getColor(requireContext(), R.color.white)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_bottom_sheet, container, false)

    override fun onViewCreatedBeforeRendering(view: View, savedInstanceState: Bundle?) {
        super.onViewCreatedBeforeRendering(view, savedInstanceState)
        bottomSheetBehavior = BottomSheetBehavior.from(viewBinding.groupToolbar)
        viewModel.asyncSubscribe()
        if (!context.isPortraitMode())
            viewBinding.moreBtn.setColorFilter(Color.WHITE)

        viewBinding.moreBtn.setOnClickListener(moreClickListener)
        bottomSheetBehavior?.addBottomSheetCallback(sheetSlideListener)
    }

    override fun invalidateUi(state: MenuState) {
        super.invalidateUi(state)
        viewBinding.groupName.text = state.title
        viewBinding.moreBtn.setImageResource(state.moreBtnChevron)
        viewBinding.moreBtn.alpha = state.moreBtnAlpha
        if (context.isPortraitMode()) {
            viewBinding.groupToolbar.setBackgroundColor(ColorUtils.blendARGB(white, green, state.colorMixCoefficient))
            viewBinding.groupName.setTextColor(ColorUtils.blendARGB(grey500, white, state.colorMixCoefficient))
        }
    }

    override fun executeSingleAction(action: MenuAction) {
        super.executeSingleAction(action)
        if (action is MenuAction.ChangeMenuState) {
            bottomSheetBehavior?.state = action.state
        }
    }

    override fun onResume() {
        super.onResume()
        // After rotation the bottom sheet state resets to STATE_COLLAPSED. If before it was the STATE_EXPANDED, the
        // colors of the bottom sheet will be wrong. The standard call of "MenuIntent.ChangeStateOfBottomBar" will not
        // help, because the state already in STATE_COLLAPSED. Therefore, we need to call "MenuIntent.UpdateUiByOffset".
        MenuIntent.UpdateUiByOffset(0f).dispatchIntent()
    }

    /**
     * Get reverse state.
     *
     * @return [BottomSheetBehavior]
     */
    private fun getReverseState() = if (bottomSheetBehavior?.state == BottomSheetBehavior.STATE_COLLAPSED) {
        BottomSheetBehavior.STATE_EXPANDED
    } else {
        BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onDestroyView() {
        bottomSheetBehavior = null
        super.onDestroyView()
        viewModel.unSubscribe()
    }
}