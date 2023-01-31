package com.android.schedule.controller.impl.support.dataGenerator

import com.android.common.models.map.BuildingResponse
import com.android.schedule.controller.api.week.lesson.AudienceResponse

/**
 * Audience response data generator.
 *
 * @constructor Create empty constructor for audience response data generator
 */
class AudienceResponseDataGenerator {
    val audienceResponseNoName = AudienceResponse(
        id = 967,
        name = "",
        building = BuildingResponse(
            id = 33,
            name = "DL",
            abbr = "DL",
            address = ""
        )
    )

    val audienceResponseDL = AudienceResponse(
        id = 968,
        name = "Дистанционно",
        building = BuildingResponse(
            id = 33,
            name = "DL",
            abbr = "DL",
            address = ""
        )
    )

    val audienceResponse1 = AudienceResponse(
        id = 969,
        name = "401",
        building = BuildingResponse(
            id = 18,
            name = "3-й учебный корпус",
            abbr = "3 к.",
            address = ""
        )
    )

    val audienceResponse2 = AudienceResponse(
        id = 970,
        name = "102",
        building = BuildingResponse(
            id = 33,
            name = "Гидротехнический корпус-1",
            abbr = "ГК. 1",
            address = ""
        )
    )

    val audienceResponse3 = AudienceResponse(
        id = 971,
        name = "103",
        building = BuildingResponse(
            id = 33,
            name = "Гидротехнический корпус-1",
            abbr = "ГК. 1",
            address = ""
        )
    )
}