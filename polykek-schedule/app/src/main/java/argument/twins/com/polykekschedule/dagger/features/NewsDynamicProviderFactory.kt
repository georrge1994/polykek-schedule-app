package argument.twins.com.polykekschedule.dagger.features

import androidx.fragment.app.Fragment
import argument.twins.com.polykekschedule.dagger.collector.DynamicDependenciesProviderFactory
import com.android.core.retrofit.api.ICoreRetrofitModuleApi
import com.android.core.retrofit.impl.dagger.CoreRetrofitComponentHolder
import com.android.core.ui.dagger.CoreUiComponentHolder
import com.android.core.ui.dagger.ICoreUiDependencies
import com.android.core.ui.dagger.ICoreUiModuleApi
import com.android.module.injector.dependenciesHolders.DependencyHolder2
import com.android.module.injector.dependenciesHolders.DynamicProvider
import com.android.module.injector.dependenciesHolders.IBaseDependencyHolder
import com.example.feature.web.content.dagger.WebContentComponentHolder
import com.example.news.dagger.INewsModuleDependencies
import com.example.news.dagger.INewsNavigationActions
import com.example.news.dagger.NewsComponentHolder
import retrofit2.Retrofit
import javax.inject.Inject

private const val CONTENT_CLASS_NAME = "content_page"

/**
 * News dynamic provider factory.
 *
 * @constructor Create empty news dynamic provider factory
 */
class NewsDynamicProviderFactory @Inject constructor() :
    DynamicDependenciesProviderFactory<NewsComponentHolder, INewsModuleDependencies>(NewsComponentHolder) {
    private class NewsModuleDependenciesHolder(
        coreUiModuleApi: ICoreUiModuleApi,
        coreRetrofitModuleApi: ICoreRetrofitModuleApi,
        override val block: (IBaseDependencyHolder<INewsModuleDependencies>, ICoreUiModuleApi, ICoreRetrofitModuleApi) -> INewsModuleDependencies,
    ) : DependencyHolder2<ICoreUiModuleApi, ICoreRetrofitModuleApi, INewsModuleDependencies>(
        coreUiModuleApi,
        coreRetrofitModuleApi
    )

    private val newsNavigationActions: INewsNavigationActions = object : INewsNavigationActions {
        override fun getWebContentScreen(title: String, url: String): Fragment =
            WebContentComponentHolder.getApi().getWebContentFragment(title, url, CONTENT_CLASS_NAME)
    }

    override fun getDynamicProvider(): DynamicProvider<INewsModuleDependencies> = DynamicProvider {
        NewsModuleDependenciesHolder(
            CoreUiComponentHolder.getApi(),
            CoreRetrofitComponentHolder.getApi(),
        ) { dependencyHolder, coreUiModuleApi, coreRetrofitComponentHolder ->
            object : INewsModuleDependencies {
                override val newsNavigationActions: INewsNavigationActions
                    get() = this@NewsDynamicProviderFactory.newsNavigationActions
                override val retrofit: Retrofit
                    get() = coreRetrofitComponentHolder.rssMediaRetrofit
                override val coreBaseUiDependencies: ICoreUiDependencies
                    get() = coreUiModuleApi.coreUiDependencies
                override val dependencyHolder: IBaseDependencyHolder<out INewsModuleDependencies>
                    get() = dependencyHolder
            }
        }.dependencies
    }
}