package com.tearas.resizevideo.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.tearas.resizevideo.ui.video_pickers.ViewPagerAdapter

class MainViewPager(private val activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        if (position==0) return HomeFragment()
        return HomeFragment()
    }
}