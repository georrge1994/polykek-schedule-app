package com.android.feature.welcome.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.core.ui.fragments.BaseFragment
import com.android.feature.welcome.R
import com.android.feature.welcome.dagger.WelcomeComponentHolder
import com.android.feature.welcome.databinding.FragmentShootBinding
import com.android.feature.welcome.viewModels.WelcomeViewModel
import com.android.shared.code.utils.syntaxSugar.createSharedViewModelWithParentFragment

private const val FRAGMENT_ID = "FRAGMENT_ID"

/**
 * Shoot fragment on the welcome screen.
 *
 * @constructor Create empty constructor for shoot fragment
 */
internal class ShootFragment : BaseFragment() {
    private val viewBinding by viewBinding(FragmentShootBinding::bind)
    private lateinit var welcomeViewModel: WelcomeViewModel

    override fun injectToComponent() = WelcomeComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        welcomeViewModel = createSharedViewModelWithParentFragment(viewModelFactory)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_shoot, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { bundle ->
            ContextCompat.getDrawable(
                requireContext(),
                welcomeViewModel.getDrawableId(bundle.getInt(FRAGMENT_ID))
            ).let { drawable ->
                viewBinding.image.setImageDrawable(drawable)
            }
        }
    }

    companion object {
        /**
         * New instance.
         *
         * @param fragmentId Fragment id
         * @return [ShootFragment]
         */
        internal fun newInstance(fragmentId: Int) = ShootFragment().apply {
            arguments = Bundle().apply {
                putInt(FRAGMENT_ID, fragmentId)
            }
        }
    }
}
