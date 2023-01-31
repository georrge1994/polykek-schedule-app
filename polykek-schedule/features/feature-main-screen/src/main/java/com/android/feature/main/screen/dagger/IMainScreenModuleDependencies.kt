package com.android.feature.main.screen.dagger

import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.core.ui.dagger.ICoreUiModuleDependencies

/**
 * Main screen module dependencies.
 */
interface IMainScreenModuleDependencies : ICoreUiModuleDependencies {
    val mainScreenNavigationActions: IMainScreenNavigationActions
    val savedItemsRoomRepository: ISavedItemsRoomRepository
}