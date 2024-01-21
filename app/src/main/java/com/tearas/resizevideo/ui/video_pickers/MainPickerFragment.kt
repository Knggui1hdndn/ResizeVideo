package com.tearas.resizevideo.ui.video_pickers

import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.tearas.resizevideo.R
import com.tearas.resizevideo.core.BaseFragment
import com.tearas.resizevideo.databinding.FragmentMainPickerBinding

class MainPickerFragment : BaseFragment<FragmentMainPickerBinding>(R.layout.fragment_main_picker) {
    override fun getViewBinding(view: View): FragmentMainPickerBinding {
        return FragmentMainPickerBinding.bind(view)
    }

    override fun initView() {
        setToolbar(
            binding.mToolbar,
            "Video Picker",
            AppCompatResources.getDrawable(requireActivity(), R.drawable.baseline_arrow_back_24)!!,
            true
        )
        binding.apply {
            val pagerAdapter = ViewPagerAdapter(requireActivity())
            binding.viewPager.adapter = pagerAdapter
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                if (position == 0) {
                    tab.text = "Videos"
                } else {
                    tab.text = "Folders"
                }
            }.attach()
        }
        binding.mToolbar.setNavigationOnClickListener {
            requireActivity().finish()
        }
    }
}