package com.example.feature.web.content.ui

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.core.ui.newUI.toolbar.CustomToolbar
import com.android.core.ui.theme.AppTheme
import com.example.feature.web.content.mvi.WebContentState
import com.example.feature.web.content.viewModels.WebContentViewModel

/**
 * Web Content Screen composable function.
 *
 * @param viewModel ViewModel for the Web Content screen.
 * @param onBack Callback to handle back navigation.
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
internal fun WebContentScreen(
    viewModel: WebContentViewModel,
    onBack: () -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    WebContent(
        state = state,
        onBack = onBack,
    )
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
internal fun WebContent(
    state: WebContentState,
    onBack: () -> Unit = {},
) {
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current
    val webView = remember { WebView(context) }

    LaunchedEffect(webView, state.scrollToElementClass) {
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Scroll to an element after the page has loaded.
                if (state.scrollToElementClass != null) {
                    webView.evaluateJavascript(
                        "document.getElementsByClassName('${state.scrollToElementClass}')" +
                                "[0].scrollIntoView({ behavior: 'smooth' });",
                        null
                    )
                }
            }
        }
        webView.settings.javaScriptEnabled = true
    }

    LaunchedEffect(state.url) {
        if (state.url.isNotBlank()) {
            webView.loadUrl(state.url)
        }
    }

    Scaffold(
        topBar = {
            CustomToolbar(
                title = state.title,
                onNavigationClick = onBack,
            )
        }
    ) { paddingValues ->
        if (isPreview) {
            // Show placeholder content during preview
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "WebView Preview")
            }
        } else {
            // Actual WebView rendering at runtime
            AndroidView(
                factory = { webView },
                modifier = Modifier
                    .testTag("WebViewPreview")
                    .padding(paddingValues)
                    .fillMaxSize()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun WebContentScreenPreview(
    @PreviewParameter(SampleWebContentStateProvider::class) state: WebContentState
) {
    AppTheme {
        WebContent(
            state = state,
            onBack = { /* No-op for preview */ }
        )
    }
}

private class SampleWebContentStateProvider : PreviewParameterProvider<WebContentState> {
    override val values = sequenceOf(
        WebContentState(
            title = "Some Title",
            url = "https://www.example.com/long-article",
        )
    )
}