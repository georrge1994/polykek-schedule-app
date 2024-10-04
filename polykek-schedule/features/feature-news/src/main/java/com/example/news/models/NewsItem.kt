package com.example.news.models

/**
 * News item.
 *
 * @param title The title
 * @param description The description
 * @param category The category
 * @param link The link to the news on the website
 * @param pubDate The publication date
 * @param imageUrl The image-preview URL
 */
data class NewsItem(
    val title: String?,
    val description: String?,
    val category: String?,
    val link: String?,
    val pubDate: String?,
    val imageUrl: String?,
)