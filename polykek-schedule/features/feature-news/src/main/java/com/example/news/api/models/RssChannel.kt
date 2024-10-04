package com.example.news.api.models

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

/**
 * Rss channel of some news.
 *
 * @property item List of news
 * @property title Title of the news
 */
@Root(name = "channel", strict = false)
data class RssChannel(
    @field:Element(name = "title", required = false)            var title: String? = null,
    @field:ElementList(name = "item", inline = true)            var item: List<RssItem>? = null,
)