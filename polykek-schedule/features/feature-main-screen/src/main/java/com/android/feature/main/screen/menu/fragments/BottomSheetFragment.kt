package com.android.feature.main.screen.menu.fragments

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.core.ui.fragments.BaseFragment
import com.android.feature.main.screen.R
import com.android.feature.main.screen.dagger.MainScreenComponentHolder
import com.android.feature.main.screen.databinding.FragmentBottomSheetBinding
import com.android.feature.main.screen.menu.viewModels.BottomAnimationViewModel
import com.android.feature.main.screen.menu.wrappers.OwnBottomSheetCallback
import com.android.shared.code.utils.syntaxSugar.createViewModel
import com.android.shared.code.utils.syntaxSugar.isPortraitMode
import com.google.android.material.bottomsheet.BottomSheetBehavior

/**
 * Bottom sheet fragment.
 *
 * @constructor Create empty constructor for bottom sheet fragment
 */
internal class BottomSheetFragment : BaseFragment() {
    private val viewBinding by viewBinding(FragmentBottomSheetBinding::bind)

    @DrawableRes
    private var iconChevron: Int = R.drawable.ic_more_vertical_grey_24dp
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var viewModel: BottomAnimationViewModel

    private val moreClickListener = View.OnClickListener {
        viewModel.updateBottomAnimation(bottomSheetBehavior.state)
    }

    private val sheetSlideListener = object : OwnBottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            viewModel.updateUiByOffset(slideOffset)
        }
    }

    private val groupNameObserver = Observer<String> { viewBinding.groupName.text = it }

    private val groupToolbarColorObserver = Observer<Int> { viewBinding.groupToolbar.setBackgroundColor(it) }

    private val groupNameColorObserver = Observer<Int> { viewBinding.groupName.setTextColor(it) }

    private val bottomSheetStateObserver = Observer<Int> { bottomSheetBehavior.state = it }

    private val slideTopPositionObserver = Observer<Pair<Float, Float>> {
        viewBinding.moreBtn.setImageResource(iconChevron)
        viewBinding.moreBtn.alpha = it.second
        if (!context.isPortraitMode())
            viewBinding.moreBtn.setColorFilter(Color.WHITE)
    }

    private val slideMiddlePositionObserver = Observer<Pair<Float, Float>> {
        viewBinding.moreBtn.setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp)
        viewBinding.moreBtn.alpha = it.second
    }

    private val slideBottomPositionObserver = Observer<Pair<Float, Float>> {
        viewBinding.moreBtn.alpha = 0f
    }

    override fun injectToComponent() = MainScreenComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as AppCompatActivity).createViewModel(viewModelFactory)
        iconChevron = viewModel.getChevron()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_bottom_sheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheetBehavior = BottomSheetBehavior.from(viewBinding.groupToolbar)

        viewModel.title.observe(viewLifecycleOwner, groupNameObserver)
        viewModel.bottomSheetState.observe(viewLifecycleOwner, bottomSheetStateObserver)

        viewModel.slideTopPosition.observe(viewLifecycleOwner, slideTopPositionObserver)
        viewModel.slideMiddlePosition.observe(viewLifecycleOwner, slideMiddlePositionObserver)
        viewModel.slideBottomPosition.observe(viewLifecycleOwner, slideBottomPositionObserver)

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            viewModel.groupToolbarColor.observe(viewLifecycleOwner, groupToolbarColorObserver)
            viewModel.groupNameColor.observe(viewLifecycleOwner, groupNameColorObserver)
        }

        viewBinding.moreBtn.setOnClickListener(moreClickListener)
        bottomSheetBehavior.addBottomSheetCallback(sheetSlideListener)
    }
}