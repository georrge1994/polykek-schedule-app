package com.android.feature.schools.useCases

import com.android.core.retrofit.api.common.CatchResourceUseCase
import com.android.core.ui.dagger.BACKGROUND_MESSAGE_BUS
import com.android.feature.schools.api.SchoolApiRepository
import com.android.feature.schools.models.School
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Named

/**
 * School use case is representing request model to UI-school-model.
 *
 * @property schoolListRepository Provides api-request to get school list
 * @constructor Create [SchoolUseCase]
 *
 * @param backgroundMessageBus Common background message bus with snack-bar output
 */
internal class SchoolUseCase @Inject constructor(
    private val schoolListRepository: SchoolApiRepository,
    @Named(BACKGROUND_MESSAGE_BUS) backgroundMessageBus: MutableSharedFlow<String>
) : CatchResourceUseCase(backgroundMessageBus) {
    /**
     * Get schools.
     *
     * @return List of [School] or null
     */
    internal suspend fun getSchools(): List<School>? = schoolListRepository.getSchools().catchRequestError { data ->
        data.faculties.map { requestSchool ->
            with(requestSchool) {
                School(
                    id = id,
                    name = name,
                    abbr = abbr
                )
            }
        }
    }
}
