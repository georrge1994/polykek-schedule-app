package com.example.news.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.core.ui.newUI.toolbar.CustomToolbar
import com.android.core.ui.newUI.utils.InitDisposableSubscriptionEffect
import com.android.core.ui.theme.AppTheme
import com.android.core.ui.theme.ColorPrimary
import com.android.feature.news.R
import com.example.news.models.NewsItem
import com.example.news.mvi.NewsIntent
import com.example.news.mvi.NewsState
import com.example.news.viewModels.NewsViewModel

/**
 * News Screen composable function.
 *
 * @param viewModel ViewModel for the News screen.
 * @param dispatchIntent Dispatches an intent to the ViewModel.
 */
@Composable
internal fun NewsScreen(
    viewModel: NewsViewModel,
    dispatchIntent: NewsIntent.() -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    viewModel.InitDisposableSubscriptionEffect(
        onStartCallback = { NewsIntent.LoadContent.dispatchIntent() },
    )

    NewsContent(state = state, dispatchIntent = dispatchIntent)
}

/**
 * News Content composable function.
 *
 * @param state State of the News screen.
 * @param dispatchIntent Dispatches an intent to the ViewModel.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun NewsContent(
    state: NewsState,
    dispatchIntent: NewsIntent.() -> Unit = {},
) {
    val isRefreshing = state.isLoading
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            NewsIntent.LoadContent.dispatchIntent()
        }
    )

    AppTheme {
        Scaffold(
            topBar = {
                CustomToolbar(
                    title = stringResource(id = R.string.news_fragment_title),
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .pullRefresh(pullRefreshState)
                    .testTag("NewsContentBox")
            ) {
                if (!state.isLoading) {
                    when {
                        state.news.isEmpty() -> {
                            // Show a message when there are no news items
                            Box(
                                modifier = Modifier.fillMaxSize()
                                    .testTag("NewsContentBox"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(id = R.string.news_fragment_empty_list),
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .testTag("EmptyListMessage")
                                )
                            }
                        }

                        else -> {
                            LazyColumn {
                                itemsIndexed(state.news) { index, newsItem ->
                                    val position = index + 1
                                    NewsListItem(
                                        position,
                                        newsItem,
                                        onClick = { url ->
                                            NewsIntent.OpenNewsDetail(
                                                position = position,
                                                url = url,
                                            ).dispatchIntent()
                                        }
                                    )
                                    Divider()
                                }
                            }
                        }
                    }
                }

                PullRefreshIndicator(
                    refreshing = isRefreshing,
                    contentColor = ColorPrimary,
                    state = pullRefreshState,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .testTag("PullRefreshIndicator")
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun NewsContentPreview(
    @PreviewParameter(SampleNewsStateProvider::class) state: NewsState
) {
    AppTheme {
        NewsContent(
            state = state,
        )
    }
}

private class SampleNewsStateProvider : PreviewParameterProvider<NewsState> {
    override val values = sequenceOf(
        NewsState(
            isLoading = false,
            news = listOf(
                NewsItem(
                    title = "Sample News Title 1",
                    link = "https://example.com/news1",
                    imageUrl = "https://www.spbstu.ru/upload/resize_cache/iblock/f8c/183_122_2/1.jpg",
                    description = "This is a sample description for news item 1.",
                    category = "Sample Category",
                    pubDate = "25 September, 16:06"
                ),
                NewsItem(
                    title = "Sample News Title 2",
                    link = "https://example.com/news2",
                    imageUrl = "https://www.spbstu.ru/upload/resize_cache/iblock/f8c/183_122_2/1.jpg",
                    description = "This is a sample description for news item 2. This is a sample description for news item 2. This is a sample description for news item 2.",
                    category = "Sample Category",
                    pubDate = "25 September, 16:06"
                )
            )
        ),
        NewsState(
            isLoading = true,
            news = emptyList()
        )
    )
}