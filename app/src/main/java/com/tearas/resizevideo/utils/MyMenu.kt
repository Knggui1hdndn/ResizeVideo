package com.tearas.resizevideo.utils


import android.content.Context
import android.provider.MediaStore.Video.Media
import android.util.Log
import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.tearas.resizevideo.R


enum class SortOrder(val order: String) {
    ASCENDING(" ASC"), DESCENDING(" DESC"),
}


object MyMenu {
    private var sortOrder = SortOrder.DESCENDING.order
    private var orderBy = Media.DATE_ADDED

    fun View.showPopUpMenuSort(orderBy: (String) -> Unit) {
        val popupMenu = PopupMenu(context, this)
        popupMenu.menuInflater.inflate(R.menu.menu_sort, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.sortByDesc || menuItem.itemId == R.id.sortByAsc) {
                sortOrder = if (menuItem.itemId == R.id.sortByDesc) {
                    SortOrder.DESCENDING.order
                } else {
                    SortOrder.ASCENDING.order
                }
            } else {
                this@MyMenu.orderBy = when (menuItem.itemId) {
                    R.id.sortByDateAdded -> Media.DATE_ADDED
                    R.id.sortByTitle -> Media.TITLE
                    R.id.sortBySize -> Media.SIZE
                    R.id.sortByDuration -> Media.DURATION
                    else -> Media.DATE_ADDED
                }
            }
            orderBy(this@MyMenu.orderBy+sortOrder)
            true
        }

        popupMenu.show()
    }

}