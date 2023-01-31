package com.android.shared.code.utils.syntaxSugar

import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

/**
 * Create view model for activity.
 *
 * @receiver [AppCompatActivity]
 * @param VM View model type
 * @param viewModelFactory View model factory
 * @return [VM]
 */
@MainThread
inline fun <reified VM : ViewModel> AppCompatActivity.createViewModel(
    viewModelFactory: ViewModelProvider.Factory
): VM = ViewModelProvider(
    store = viewModelStore,
    factory = viewModelFactory,
    defaultCreationExtras = defaultViewModelCreationExtras
)[VM::class.java]

/**
 * Create view model for fragment.
 *
 * @receiver [Fragment]
 * @param VM View model type
 * @param viewModelFactory View model factory
 * @return [VM]
 */
@MainThread
inline fun <reified VM : ViewModel> Fragment.createViewModel(
    viewModelFactory: ViewModelProvider.Factory
): VM = ViewModelProvider(
    store = viewModelStore,
    factory = viewModelFactory,
    defaultCreationExtras = defaultViewModelCreationExtras
)[VM::class.java]

/**
 * Create shared view model and link it to the parent fragment. This custom mechanism allows to avoid linking shared View model to single
 * activity.
 *
 * @receiver [Fragment]
 * @param VM View model type
 * @param viewModelFactory View model factory
 * @return [VM]
 */
@MainThread
inline fun <reified VM : ViewModel> Fragment.createSharedViewModelWithParentFragment(
    viewModelFactory: ViewModelProvider.Factory
): VM = ViewModelProvider(
    store = parentFragment!!.viewModelStore,
    factory = viewModelFactory,
    defaultCreationExtras = parentFragment!!.defaultViewModelCreationExtras
)[VM::class.java]

/**
 * Create shared view model and link it to the specific ancestor fragment. This custom mechanism allows to avoid linking shared view model
 * to single activity. If you have simple case, will use [createSharedViewModelWithParentFragment].
 *
 * @receiver [Fragment]
 * @param AN An
 * @param VM Vm
 * @param extrasProducer Extras producer
 * @param factoryProducer Factory producer
 * @return [VM]
 */
@MainThread
inline fun <reified AN : Fragment, reified VM : ViewModel> Fragment.createSharedViewModelWithSpecificAncestorFragment(
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): VM {
    val ancestorFragment = getAncestorFragment<AN>()
    return ViewModelProvider(
        store = ancestorFragment.viewModelStore,
        factory = factoryProducer?.invoke() ?: defaultViewModelProviderFactory,
        defaultCreationExtras = extrasProducer?.invoke() ?: ancestorFragment.defaultViewModelCreationExtras
    )[VM::class.java]
}

/**
 * Get target ancestor fragment.
 *
 * @receiver [Fragment]
 * @param AN Ancestor fragment type
 * @return [Fragment]
 */
inline fun <reified AN : Fragment> Fragment.getAncestorFragment(): Fragment {
    val heirs = ArrayList<String>()
    heirs.add(this::class.java.simpleName)
    var ancestorFragment: Fragment? = parentFragment
    do {
        heirs.add(this::class.java.simpleName)
        when {
            // If ancestor doesn't contain in ancestor line, will throw an exception.
            ancestorFragment == null -> throw Exception(
                "${AN::class.java.simpleName} is not ancestor of ${this::class.java.simpleName}! " +
                        "Inherits: ${heirs.joinToString(" -> ")}"
            )
            // If we found the ancestor -> break the loop.
            ancestorFragment::class.java.simpleName == AN::class.java.simpleName -> break
            // Else, will check next.
            else -> ancestorFragment = ancestorFragment.parentFragment
        }
    } while (ancestorFragment != null)
    return ancestorFragment!!
}