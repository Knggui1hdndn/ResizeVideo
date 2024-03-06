package com.tearas.resizevideo.ui.select_compress

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

class ItemSpacingDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {

    constructor(
        context: Context,
        @DimenRes spacingRes: Int
    ) : this(context.resources.getDimensionPixelSize(spacingRes))

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = spacing
    }


}
