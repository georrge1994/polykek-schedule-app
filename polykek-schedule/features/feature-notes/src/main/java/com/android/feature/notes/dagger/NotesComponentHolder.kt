package com.android.feature.notes.dagger

import com.android.module.injector.ComponentHolder
import com.android.module.injector.WeakComponentHolderDelegate

/**
 * Notes screen component holder.
 */
object NotesComponentHolder : ComponentHolder<INotesModuleApi, INotesModuleDependencies>() {
    private val componentHolderDelegate = WeakComponentHolderDelegate<INotesModuleApi,
            INotesModuleDependencies, NotesComponent> { NotesComponent.create(it) }

    override fun getApi(): INotesModuleApi = componentHolderDelegate.getApi(dependenciesProvider)

    /**
     * Get component.
     *
     * @return [NotesComponent]
     */
    internal fun getComponent(): NotesComponent = getApi() as NotesComponent
}