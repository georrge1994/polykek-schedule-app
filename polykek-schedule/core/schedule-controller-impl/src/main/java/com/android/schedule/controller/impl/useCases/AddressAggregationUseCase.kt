package com.android.schedule.controller.impl.useCases

import android.app.Application
import com.android.schedule.controller.api.week.lesson.AudienceResponse
import com.android.schedule.controller.impl.R
import com.android.shared.code.utils.markers.IUseCase
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Address aggregation use case.
 *
 * @property application Application object to get context
 * @constructor Create [AddressAggregationUseCase]
 */
@Singleton
internal class AddressAggregationUseCase @Inject constructor(private val application: Application) : IUseCase {
    /**
     * Get correct address.
     *
     * @param auditorium Auditorium
     * @return Correct place name. If [AudienceResponse] contains all information, will provide complex name, else more simplified variant
     */
    internal fun getCorrectedAddress(auditorium: AudienceResponse?): String =
        if (auditorium?.name.isNullOrEmpty()) {
            application.getString(R.string.schedule_fragment_chamber_of_Secret)
        } else {
            val address = StringBuilder()
            address.appendBuilding(auditorium)
            auditorium!!.name!!.toIntOrNull()?.let {
                address.append(application.getString(R.string.schedule_fragment_auditorium, it))
            } ?: kotlin.run {
                address.append(auditorium.name)
            }
            address.toString()
        }

    /**
     * Get address with one building, but two auditoriums inside.
     *
     * @param auditorium1 Auditorium 1
     * @param auditorium2 Auditorium 2
     * @return Corrected address
     */
    internal fun getCombinedCorrectAddress(auditorium1: AudienceResponse?, auditorium2: AudienceResponse?): String {
        val address = StringBuilder()
        address.appendBuilding(auditorium1)
        address.appendAuditorium(FIRST, auditorium1)
        address.append("; ")
        address.appendAuditorium(SECOND, auditorium2)
        return if (address.length <= 2) // Two symbols for semicolon and space.
            application.getString(R.string.schedule_fragment_chamber_of_Secret)
        else
            address.toString()
    }

    /**
     * Append building to address.
     *
     * @receiver [StringBuilder]
     * @param auditorium Auditorium
     */
    private fun StringBuilder.appendBuilding(auditorium: AudienceResponse?) {
        auditorium ?: return
        // Build name.
        auditorium.building?.name?.let { name ->
            append(name)
        } ?: auditorium.building?.abbr?.let { abbr ->
            append(abbr)
        }
        // Comma fix - some addresses doesn't contain it.
        if (isNotEmpty() && lastOrNull() != ',') {
            append(", ")
        }
    }

    /**
     * Append auditorium to address string builder.
     *
     * @receiver [StringBuilder]
     * @param alias Alias
     * @param auditorium [AudienceResponse]
     */
    private fun StringBuilder.appendAuditorium(alias: String, auditorium: AudienceResponse?) {
        auditorium ?: return
        auditorium.name?.toIntOrNull()?.let {
            append(alias)
            append(application.getString(R.string.schedule_fragment_auditorium, it))
        } ?: auditorium.name?.let {
            append(alias)
            append(auditorium.name)
        }
    }
}