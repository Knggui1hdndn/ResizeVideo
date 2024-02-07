package com.tearas.resizevideo.utils


import android.annotation.SuppressLint
import android.content.Context
import android.provider.MediaStore.Video.Media
import android.util.Log
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.MenuCompat
import androidx.core.view.get
import com.tearas.resizevideo.R
import com.tearas.resizevideo.utils.MyMenu.showPopUpMenuSort


enum class SortOrder(val order: String) {
    ASCENDING(" ASC"), DESCENDING(" DESC"),
}


@SuppressLint("StaticFieldLeak")
object MyMenu {
    private var sortOrder = SortOrder.DESCENDING.order
    private var sortBy = Media.DATE_ADDED
    private lateinit var view: View
    fun View.showPopUpMenuSort(sortBy: (String) -> Unit) {
        if (!::view.isInitialized) {
            view = this
        } else {
            if (this != view) {
                sortOrder = SortOrder.DESCENDING.order
                this@MyMenu.sortBy = Media.DATE_ADDED
            }
        }
        val popupMenu = PopupMenu(context, this)
        popupMenu.menuInflater.inflate(R.menu.menu_sort, popupMenu.menu)
        MenuCompat.setGroupDividerEnabled(popupMenu.menu, true);

        when (MyMenu.sortBy) {
            Media.DATE_ADDED -> popupMenu.menu.findItem(R.id.sortByDateAdded).isChecked = true
            Media.TITLE -> popupMenu.menu.findItem(R.id.sortByTitle).isChecked = true
            Media.SIZE -> popupMenu.menu.findItem(R.id.sortBySize).isChecked = true
            Media.DURATION -> popupMenu.menu.findItem(R.id.sortByDuration).isChecked = true
            else -> popupMenu.menu.findItem(R.id.sortByDateAdded).isChecked = true
        }
        when (sortOrder) {
            SortOrder.DESCENDING.order -> popupMenu.menu.findItem(R.id.sortByDesc).isChecked = true
            SortOrder.ASCENDING.order -> popupMenu.menu.findItem(R.id.sortByAsc).isChecked = true
        }
        popupMenu.setOnMenuItemClickListener { menuItem ->
            menuItem.setChecked(true)
            if (menuItem.itemId == R.id.sortByDesc || menuItem.itemId == R.id.sortByAsc) {
                sortOrder = if (menuItem.itemId == R.id.sortByDesc) {
                    SortOrder.DESCENDING.order
                } else {
                    SortOrder.ASCENDING.order
                }
            } else {
                this@MyMenu.sortBy = when (menuItem.itemId) {
                    R.id.sortByDateAdded -> Media.DATE_ADDED
                    R.id.sortByTitle -> Media.TITLE
                    R.id.sortBySize -> Media.SIZE
                    R.id.sortByDuration -> Media.DURATION
                    else -> Media.DATE_ADDED
                }
            }
            sortBy(this@MyMenu.sortBy + sortOrder)
            true
        }

        popupMenu.show()
    }

    private lateinit var darkModeManager: DarkModeManager
    fun View.showPopupMenuTheme(text: (String) -> Unit) {
        val popupMenu = PopupMenu(context, this)
        if (!::darkModeManager.isInitialized) {
            darkModeManager = DarkModeManager(this.context)
        }
        popupMenu.menuInflater.inflate(R.menu.menu_options_theme, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            menuItem.setChecked(true)
            text(menuItem.title.toString())
            when (menuItem.itemId) {
                R.id.mnDark -> darkModeManager.enableDarkMode()
                R.id.mnLight -> darkModeManager.disableDarkMode()
                R.id.mnSystem -> darkModeManager.darkModeDefault()
            }
            true
        }
        popupMenu.show()

    }
}