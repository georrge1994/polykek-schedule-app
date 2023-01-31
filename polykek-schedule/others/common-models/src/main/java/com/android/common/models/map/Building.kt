package com.android.common.models.map

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

const val BUILDING_ITEM = "BUILDING_ITEM"

/**
 * Building.
 *
 * @property name Building name
 * @property nameWithAbbr Name with abbr
 * @property address Address
 * @constructor Create [Building]
 */
@Parcelize
data class Building(
    val name: String,
    val nameWithAbbr: String,
    val address: String?,
) : Parcelable {
    companion object {
        /**
         * Get yandex map item.
         *
         * @receiver [Bundle]
         * @return [Building]
         */
        @Suppress("DEPRECATION")
        fun Bundle.getBuilding(): Building? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getParcelable(BUILDING_ITEM, Building::class.java)
        } else {
            getParcelable(BUILDING_ITEM) as Building?
        }
    }
}