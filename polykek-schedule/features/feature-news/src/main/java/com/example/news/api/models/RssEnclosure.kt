package com.example.news.api.models

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root

/**
 * Enclosure of some news.
 *
 * @property url Url of the image
 * @property type Type of the image
 * @property length Length of the image
 */
@Root(name = "enclosure", strict = false)
data class RssEnclosure(
    @field:Attribute(name = "url", required = false)        var url: String? = null,
    @field:Attribute(name = "type", required = false)       var type: String? = null,
    @field:Attribute(name = "length", required = false)     var length: Long? = null
)
