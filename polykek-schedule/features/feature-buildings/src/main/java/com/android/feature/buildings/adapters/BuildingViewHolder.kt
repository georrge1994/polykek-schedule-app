package com.android.feature.buildings.adapters

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.android.common.models.map.Building

/**
 * Building view holder.
 *
 * @property buildingItemView [BuildingItemView]
 * @property listener Item action listener
 * @constructor Create [BuildingViewHolder]
 */
internal class BuildingViewHolder(
    private val buildingItemView: BuildingItemView,
    private val listener: IBuildingActions
) : RecyclerView.ViewHolder(buildingItemView) {
    /**
     * Bind to.
     *
     * @param building Building
     */
    internal fun bindTo(building: Building) = with(building) {
        buildingItemView.name.text = nameWithAbbr
        buildingItemView.address.text = address
        buildingItemView.address.isVisible = !address.isNullOrEmpty()
        buildingItemView.setOnClickListener { listener.onClick(building) }
    }
}