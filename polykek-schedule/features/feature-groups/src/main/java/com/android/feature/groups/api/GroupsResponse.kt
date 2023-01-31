package com.android.feature.groups.api

import com.android.common.models.groups.GroupResponse
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * List of groups.
 *
 * @property groups Groups
 * @constructor Create [GroupsResponse]
 */
internal data class GroupsResponse(
    @SerializedName("groups") val groups: List<GroupResponse> = Collections.emptyList()
)