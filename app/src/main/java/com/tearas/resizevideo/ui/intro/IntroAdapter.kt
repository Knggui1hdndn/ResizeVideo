package com.tearas.resizevideo.ui.intro

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tearas.resizevideo.core.BaseAdapter
import com.tearas.resizevideo.databinding.ItemIntroBinding
import com.tearas.resizevideo.model.IntroModel

class IntroAdapter :BaseAdapter<ItemIntroBinding,IntroModel> (){
    override fun getViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemIntroBinding {
       return ItemIntroBinding.inflate(inflater,parent,false)
    }

    override fun onBind(binding: ItemIntroBinding, item: IntroModel) {
         binding.apply {
             description=item.description
             title=item.title
             imageIntro.setImageResource(item.image)
         }
    }
}