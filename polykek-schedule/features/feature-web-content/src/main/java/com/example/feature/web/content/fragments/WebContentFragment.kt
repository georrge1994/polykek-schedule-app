package com.example.feature.web.content.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import com.android.core.ui.fragments.NavigationComposeFragment
import com.android.module.injector.moduleMarkers.IModuleComponent
import com.android.shared.code.utils.syntaxSugar.createViewModel
import com.example.feature.web.content.dagger.WebContentComponentHolder
import com.example.feature.web.content.mvi.WebContentAction
import com.example.feature.web.content.mvi.WebContentIntent
import com.example.feature.web.content.mvi.WebContentState
import com.example.feature.web.content.ui.WebContentScreen
import com.example.feature.web.content.viewModels.WebContentViewModel

private const val TITLE = "TITLE"
private const val URL = "URL"
private const val SPECIFIC_CLASS_TO_SCROLL = "SPECIFIC_CLASS_TO_SCROLL"

/**
 * Web content fragment.
 */
internal class WebContentFragment :
    NavigationComposeFragment<WebContentIntent, WebContentState, WebContentAction, WebContentViewModel>() {

    override fun getComponent(): IModuleComponent = WebContentComponentHolder.getComponent()

    override fun injectToComponent(): Unit = WebContentComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel(viewModelFactory)
        WebContentIntent.LoadWebContent(
            title = requireArguments().getString(TITLE) ?: "",
            url = requireArguments().getString(URL) ?: "",
            specificClass = requireArguments().getString(SPECIFIC_CLASS_TO_SCROLL)
        ).let(viewModel::dispatchIntentAsync)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        ComposeView(requireContext()).apply {
            setContent {
                WebContentScreen(
                    viewModel = viewModel,
                    onBack = { tabRouter?.exit() }
                )
            }
        }

    internal companion object {
        /**
         * Create new instance of [WebContentFragment].
         *
         * @param title Title of the web content
         * @param url URL of the web content
         * @param scrollToElementClass Specific class to scroll to
         */
        internal fun newInstance(
            title: String,
            url: String,
            scrollToElementClass: String? = null,
        ): WebContentFragment = WebContentFragment().apply {
            arguments = Bundle().apply {
                putString(TITLE, title)
                putString(URL, url)
                if (scrollToElementClass != null) {
                    putString(SPECIFIC_CLASS_TO_SCROLL, scrollToElementClass)
                }
            }
        }
    }
}