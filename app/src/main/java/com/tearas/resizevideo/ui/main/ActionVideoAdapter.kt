package com.tearas.resizevideo.ui.main

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tearas.resizevideo.core.BaseAdapter
import com.tearas.resizevideo.databinding.ItemActionBinding
import com.tearas.resizevideo.model.DetailActionMedia


class ActionVideoAdapter : BaseAdapter<ItemActionBinding, DetailActionMedia>() {
    override fun getViewBinding(inflater: LayoutInflater, parent: ViewGroup) =
        ItemActionBinding.inflate(inflater, parent, false)

    override fun onBind(binding: ItemActionBinding, item: DetailActionMedia) {
        binding.apply {
            mCConstraint.backgroundTintList = ColorStateList.valueOf(item.backGroundAction)
            imageIcon.backgroundTintList = ColorStateList.valueOf(item.backGroundIcon)
            imageIcon.setImageDrawable(item.icon)
            title.text=item.title
        }
    }
}