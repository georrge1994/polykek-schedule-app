package com.android.core.ui.navigation

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import com.android.core.ui.dagger.CoreUiGeneralModule

private const val MAIN_CICERONE = "MAIN_CICERONE"

/**
 * [CiceroneHolder] is provided in [CoreUiGeneralModule], which connects to different components with different scopes. In the result we
 * can not to use Singleton annotation, because every each of them will have an own singleton. The simplest and clearest way to resolve it
 * to make [CiceroneHolder] as object.
 */
internal object CiceroneHolder : ICiceroneHolder {
    private val containers = HashMap<String, Cicerone<Router>>()

    override fun getMainCicerone(): Cicerone<Router> = getCicerone(MAIN_CICERONE)

    override fun getCicerone(containerTag: String): Cicerone<Router> = containers.getOrPut(containerTag) {
        Cicerone.create()
    }
}