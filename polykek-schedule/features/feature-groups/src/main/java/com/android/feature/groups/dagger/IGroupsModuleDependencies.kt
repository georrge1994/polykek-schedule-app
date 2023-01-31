package com.android.feature.groups.dagger

import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.core.ui.dagger.ICoreUiModuleDependencies
import retrofit2.Retrofit

/**
 * Groups module dependencies.
 */
interface IGroupsModuleDependencies : ICoreUiModuleDependencies {
    val groupsNavigationActions: IGroupsNavigationActions
    val savedItemsRoomRepository: ISavedItemsRoomRepository
    val retrofit: Retrofit
}