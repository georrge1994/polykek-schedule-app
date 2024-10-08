package argument.twins.com.polykekschedule.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import argument.twins.com.polykekschedule.App
import argument.twins.com.polykekschedule.R
import argument.twins.com.polykekschedule.activity.mvi.ActivityAction
import argument.twins.com.polykekschedule.activity.mvi.ActivityIntent
import argument.twins.com.polykekschedule.activity.useCases.SmallFeaturesUiUseCase
import argument.twins.com.polykekschedule.activity.viewModels.MainActivityViewModel
import com.android.core.ui.navigation.ICiceroneHolder
import com.android.core.ui.navigation.polytechCicirone.AnimationType
import com.android.core.ui.navigation.polytechCicirone.PolytechAppNavigator
import com.android.core.ui.navigation.polytechCicirone.PolytechFragmentScreen
import com.android.feature.main.screen.dagger.MainScreenComponentHolder
import com.android.feature.welcome.dagger.WelcomeComponentHolder
import com.android.shared.code.utils.syntaxSugar.createViewModel
import com.android.shared.code.utils.syntaxSugar.hideStatusBar
import com.android.shared.code.utils.syntaxSugar.isPortraitMode
import com.github.terrakok.cicerone.Navigator
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * Single activity.
 *
 * @constructor Create empty constructor for main activity
 */
class MainActivity : AppCompatActivity() {
    private val navigator: Navigator by lazy { PolytechAppNavigator(this, R.id.mainContainer) }
    private lateinit var viewModel: MainActivityViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var smallFeaturesUiUseCase: SmallFeaturesUiUseCase

    @Inject
    lateinit var ciceroneHolder: ICiceroneHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = createViewModel(viewModelFactory)
        viewModel.asyncSubscribe()
        viewModel.action.onEach(::singleAction).launchIn(lifecycleScope)
        if (savedInstanceState == null) {
            viewModel.dispatchIntentAsync(ActivityIntent.ReInitScreen)
        }

        // Specific options/modes.
        smallFeaturesUiUseCase.checkSmallFeatures(this)
        if (!isPortraitMode()) {
            hideStatusBar()
        }
    }

    /**
     * Single action.
     *
     * @param action Action
     */
    private fun singleAction(action: ActivityAction) {
        when (action) {
            is ActivityAction.InitScreen -> {
                initStartScreen(action.isSelectedItem)
                if (action.isSelectedItem) {
                    smallFeaturesUiUseCase.askNotificationPermissionForFMS(this)
                }
            }
            is ActivityAction.ShowMessage -> {
                smallFeaturesUiUseCase.showMessage(this, action.message)
            }
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        ciceroneHolder.getMainCicerone().getNavigatorHolder().setNavigator(navigator)
    }

    override fun onPause() {
        ciceroneHolder.getMainCicerone().getNavigatorHolder().removeNavigator()
        super.onPause()
    }

    /**
     * Init default screen.
     *
     * @param isItemSelected Is group or professor selected
     */
    private fun initStartScreen(isItemSelected: Boolean) = ciceroneHolder.getMainCicerone().router.navigateTo(
        PolytechFragmentScreen(addToBackStack = false, animationType = AnimationType.WITHOUT) {
            if (isItemSelected)
                MainScreenComponentHolder.getApi().getMainFragment()
            else
                WelcomeComponentHolder.getApi().getWelcomeFragment()
        }
    )

    override fun onDestroy() {
        super.onDestroy()
        viewModel.unSubscribe()
    }
}