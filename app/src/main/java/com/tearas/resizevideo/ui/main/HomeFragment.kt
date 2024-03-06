package com.tearas.resizevideo.ui.main

import android.content.Context
import android.view.View
import com.tearas.resizevideo.R
import com.tearas.resizevideo.core.BaseFragment
import com.tearas.resizevideo.databinding.FragmentHomeBinding
import com.tearas.resizevideo.ffmpeg.MediaAction
import com.tearas.resizevideo.model.getListDetailsActionMedia

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    override fun getViewBinding(view: View) = FragmentHomeBinding.bind(view)
    private lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun initView() {
        binding.apply {
            compressVideo.setOnClickListener {
                mainActivity.startPickerVideo(MediaAction.CompressVideo)
            }
            mainActivity.showNativeAds(container,{})
        }
        setUpAdapter()
    }

    private fun setUpAdapter() {
        val actionAdapter = ActionVideoAdapter()
        actionAdapter.submitData = ArrayList(requireContext().getListDetailsActionMedia())
        actionAdapter.setOnClickListener { detailActionMedia, i ->
            mainActivity.startPickerVideo(detailActionMedia.media)
        }
        binding.rcyAction.adapter=actionAdapter
    }

}