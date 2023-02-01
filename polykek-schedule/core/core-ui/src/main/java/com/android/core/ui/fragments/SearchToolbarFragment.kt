package com.android.core.ui.fragments

import android.animation.LayoutTransition
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.android.core.ui.R
import com.android.core.ui.viewModels.SearchViewModel
import com.android.shared.code.utils.syntaxSugar.hideSoftwareKeyboard
import com.android.shared.code.utils.syntaxSugar.isPortraitMode
import kotlin.reflect.KClass

/**
 * Toolbar fragment with searchbar.
 *
 * @param T Sub-type of [SearchViewModel]
 * @constructor Create [SearchToolbarFragment]
 *
 * @param clazz [KClass]
 */
abstract class SearchToolbarFragment<T : SearchViewModel>(private val clazz: KClass<T>) : ToolbarFragment() {
    lateinit var viewModel: T

    open val menuId: Int = R.menu.menu_search

    private val queryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            viewModel.updateKeyWordAsync(query)
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            viewModel.updateKeyWordAsync(newText)
            return true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            store = viewModelStore,
            factory = viewModelFactory,
            defaultCreationExtras = defaultViewModelCreationExtras
        )[clazz.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.asyncSubscribe()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        super.onCreateMenu(menu, menuInflater)
        menuInflater.inflate(menuId, menu)
        initSearch(menu)
    }

    override fun onPause() {
        super.onPause()
        view?.findViewById<SearchView.SearchAutoComplete>(R.id.search_src_text)?.hideSoftwareKeyboard()
    }

    /**
     * Init search.
     *
     * @param menu Menu
     */
    private fun initSearch(menu: Menu) {
        val searchMenuItem = menu.findItem(R.id.search)
        val searchView = searchMenuItem.actionView as SearchView? ?: return
        val searchAutoComplete = searchView.findViewById<SearchView.SearchAutoComplete>(R.id.search_src_text)
        val searchCloseIcon = searchView.findViewById<ImageView>(R.id.search_close_btn)
        val searchPlate = searchView.findViewById<View>(R.id.search_plate)

        context?.searchAutoCompleteSettings(searchAutoComplete)
        searchCloseIconSettings(searchCloseIcon)
        searchPlateSettings(searchPlate)

        searchViewSettings(searchMenuItem, searchView)
    }

    /**
     * Set colors and hint text.
     *
     * @param searchAutoComplete Search auto complete
     */
    private fun Context.searchAutoCompleteSettings(searchAutoComplete: SearchView.SearchAutoComplete) {
        searchAutoComplete.setHintTextColor(ContextCompat.getColor(this, R.color.grey_100))
        searchAutoComplete.setTextColor(ContextCompat.getColor(this, R.color.textColor))
        searchAutoComplete.hint = getString(R.string.general_word_searching)
    }

    /**
     * Set close image.
     *
     * @param searchCloseIcon Search close icon
     */
    private fun searchCloseIconSettings(searchCloseIcon: ImageView) {
        searchCloseIcon.setImageResource(R.drawable.ic_close_24dp)
    }

    /**
     * Color for searchField background.
     *
     * @param searchPlate Search plate
     */
    private fun searchPlateSettings(searchPlate: View) {
        searchPlate.setBackgroundResource(R.drawable.rounded_white_rectangle)
        if (!context.isPortraitMode())
            searchPlate.layoutParams.height = resources.getDimensionPixelOffset(R.dimen.landscape_search_plate_height)
    }

    /**
     * Search view settings.
     *
     * @param searchMenuItem Search menu item
     * @param searchView Search view
     */
    private fun searchViewSettings(searchMenuItem: MenuItem, searchView: SearchView) {
        if (viewModel.keyWord.isNotEmpty()) {
            // Fixing search view after recreating.
            searchView.clearFocus()
            searchView.isIconified = false
            searchMenuItem.expandActionView()
            searchView.setQuery(viewModel.keyWord, false)
        }
        (searchView.findViewById(R.id.search_bar) as LinearLayout).layoutTransition = LayoutTransition()
        searchView.maxWidth = Resources.getSystem().displayMetrics.widthPixels
        searchView.setQuery(viewModel.keyWord, true)
        searchView.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
        searchView.setOnQueryTextListener(queryTextListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.unSubscribe()
    }
}