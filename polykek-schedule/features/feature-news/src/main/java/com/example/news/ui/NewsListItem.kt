package com.example.news.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.android.core.ui.newUI.label.BoldLabelText
import com.android.core.ui.newUI.label.LabelText
import com.android.core.ui.newUI.label.SmallLabelText
import com.android.core.ui.newUI.utils.forwardingPainter
import com.android.core.ui.theme.AppTheme
import com.android.feature.news.R
import com.example.news.models.NewsItem

/**
 * News List Item composable function.
 *
 * @param position Position of the news item in the list.
 * @param newsItem Item to display.
 * @param onClick Click listener for the news item.
 */
@Composable
internal fun NewsListItem(
    position: Int,
    newsItem: NewsItem,
    onClick: (url: String?) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick(newsItem.link)
            }
            .padding(16.dp)
            .testTag("NewsListItem_$position")
    ) {
        if (newsItem.title?.isNotBlank() == true) {
            BoldLabelText(text = "# $position ${newsItem.title}")
            Spacer(modifier = Modifier.height(8.dp))
        }
        if (newsItem.category?.isNotBlank() == true) {
            LabelText(text = newsItem.category)
            Spacer(modifier = Modifier.height(8.dp))
        }
        AsyncImage(
            model = newsItem.imageUrl,
            contentDescription = newsItem.title,
            placeholder = forwardingPainter(
                painter = painterResource(R.drawable.creative_student),
            ),
            error = forwardingPainter(
                painter = painterResource(R.drawable.creative_student),
            ),
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (newsItem.pubDate?.isNotBlank() == true) {
            SmallLabelText(text = newsItem.pubDate)
            Spacer(modifier = Modifier.height(8.dp))
        }
        if (newsItem.description?.isNotBlank() == true) {
            LabelText(text = newsItem.description)
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun NewsListItemPreview(
    @PreviewParameter(SampleNewsItemProvider::class) newsItem: NewsItem
) {
    AppTheme {
        NewsListItem(
            position = 1,
            newsItem = newsItem,
        )
    }
}

private class SampleNewsItemProvider : PreviewParameterProvider<NewsItem> {
    override val values = sequenceOf(
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
}