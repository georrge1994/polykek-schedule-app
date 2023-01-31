package com.android.feature.buildings.adapters

import android.content.Context
import android.view.ViewGroup
import com.android.common.models.map.Building
import com.android.core.ui.adapters.BaseRecyclerViewAdapter

/**
 * Building recycler view adapter.
 *
 * @property context Context
 * @property listener Item action listener
 * @constructor Create [BuildingRecyclerViewAdapter]
 * @author darkt on 1/9/2018.
 */
internal class BuildingRecyclerViewAdapter(
    private val context: Context,
    private var listener: IBuildingActions
) : BaseRecyclerViewAdapter<BuildingViewHolder, Building>() {
    override fun onBindViewHolder(viewHolder: BuildingViewHolder, position: Int) =
        viewHolder.bindTo(items[position])

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BuildingViewHolder =
        BuildingViewHolder(BuildingItemView(context), listener)
}