package com.example.news.api.models

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

/**
 * Rss feed of some news.
 *
 * @property channel Channel of the news
 */
@Root(name = "rss", strict = false)
data class RssFeed(
    @field:Element(name = "channel")
    var channel: RssChannel? = null
)