package argument.twins.com.polykekschedule.activity

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import argument.twins.com.polykekschedule.App
import argument.twins.com.polykekschedule.R
import argument.twins.com.polykekschedule.activity.useCases.SmallFeaturesUiUseCase
import argument.twins.com.polykekschedule.activity.viewModels.MainActivityViewModel
import argument.twins.com.polykekschedule.dagger.features.MAIN_SCREEN_DYNAMIC_DEPENDENCIES_PROVIDER
import argument.twins.com.polykekschedule.dagger.features.WELCOME_DYNAMIC_DEPENDENCIES_PROVIDER
import com.android.core.ui.navigation.ICiceroneHolder
import com.android.core.ui.navigation.polytechCicirone.AnimationType
import com.android.core.ui.navigation.polytechCicirone.PolytechAppNavigator
import com.android.core.ui.navigation.polytechCicirone.PolytechFragmentScreen
import com.android.feature.main.screen.dagger.IMainScreenModuleDependencies
import com.android.feature.main.screen.dagger.MainScreenComponentHolder
import com.android.feature.welcome.dagger.IWelcomeModuleDependencies
import com.android.feature.welcome.dagger.WelcomeComponentHolder
import com.android.module.injector.dependenciesHolders.DynamicProvider
import com.android.shared.code.utils.syntaxSugar.createViewModel
import com.android.shared.code.utils.syntaxSugar.hideStatusBar
import com.android.shared.code.utils.syntaxSugar.isPortraitMode
import com.github.terrakok.cicerone.Navigator
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Named

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

    @Inject
    @Named(WELCOME_DYNAMIC_DEPENDENCIES_PROVIDER)
    lateinit var dynamicDependencyProviderForWelcome: DynamicProvider<IWelcomeModuleDependencies>

    @Inject
    @Named(MAIN_SCREEN_DYNAMIC_DEPENDENCIES_PROVIDER)
    lateinit var dynamicDependencyProviderForMainScreen: DynamicProvider<IMainScreenModuleDependencies>

    private val messageFromBusObserver = Observer<String> { smallFeaturesUiUseCase.showMessage(this, it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = createViewModel(viewModelFactory)
        viewModel.asyncSubscribe()
        viewModel.messageFromBus.observe(this, messageFromBusObserver)

        if (savedInstanceState == null) {
            initStartScreen()
            if (viewModel.isItemSelected)
                smallFeaturesUiUseCase.askNotificationPermissionForFMS(this)
        }

        // Specific options/modes.
        smallFeaturesUiUseCase.checkSmallFeatures(this)
        if (!isPortraitMode())
            hideStatusBar()
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
     */
    private fun initStartScreen() = ciceroneHolder.getMainCicerone().router.navigateTo(
        PolytechFragmentScreen(addToBackStack = false, animationType = AnimationType.WITHOUT) {
            if (viewModel.isItemSelected)
                MainScreenComponentHolder.initAndGet(dynamicDependencyProviderForMainScreen).getMainFragment()
            else
                WelcomeComponentHolder.initAndGet(dynamicDependencyProviderForWelcome).getWelcomeFragment()
        }
    )

    override fun onDestroy() {
        super.onDestroy()
        viewModel.unSubscribe()
    }
}