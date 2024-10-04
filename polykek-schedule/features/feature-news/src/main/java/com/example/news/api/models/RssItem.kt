package com.example.news.api.models

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

/**
 * Rss item of some new.
 *
 * @property title Title of the news
 * @property link Link to the news
 * @property enclosure Image of the news
 * @property category Category of the news
 * @property description Description of the news
 * @property pubDate Date of the news
 */
@Root(name = "item", strict = false)
data class RssItem(
    @field:Element(name = "title")                                  var title: String? = null,
    @field:Element(name = "link")                                   var link: String? = null,
    @field:Element(name = "enclosure", required = false)            var enclosure: RssEnclosure? = null,
    @field:Element(name = "category")                               var category: String? = null,
    @field:Element(name = "description", required = false)          var description: String? = null,
    @field:Element(name = "pubDate", required = false)              var pubDate: String? = null
)