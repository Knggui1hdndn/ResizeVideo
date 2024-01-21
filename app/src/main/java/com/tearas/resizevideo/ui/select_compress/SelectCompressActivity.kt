package com.tearas.resizevideo.ui.select_compress

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.util.Log
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.knd.duantotnghiep.testsocket.core.BaseActivity
import com.tearas.resizevideo.R
import com.tearas.resizevideo.databinding.ActivitySelectCompressBinding
import com.tearas.resizevideo.ffmpeg.MediaAction
import com.tearas.resizevideo.model.OptionMedia
import com.tearas.resizevideo.model.Resolution
import com.tearas.resizevideo.ui.process.ProcessActivity
import com.tearas.resizevideo.utils.IntentUtils.getOptionMedia
import com.tearas.resizevideo.utils.IntentUtils.passOptionMedia

class SelectCompressActivity : BaseActivity<ActivitySelectCompressBinding>() {

    private var resolutionSelected: Int = 0
    private var resolutions: Array<String> = arrayOf()
    private var listSize: Array<String> = arrayOf()

    override fun getViewBinding(): ActivitySelectCompressBinding {
        return ActivitySelectCompressBinding.inflate(layoutInflater)
    }

    override fun initData() {
        setupData()
    }

    override fun initView() {
        setupUI()
    }

    private lateinit var mediaOption: OptionMedia
    private fun setupData() {
        mediaOption = intent.getOptionMedia()!!
        val array = mediaOption.data
        listSize = arrayOf(Resolution.SMALL, Resolution.MEDIUM, Resolution.LARGE, Resolution.ORIGIN)
        val resolution = mediaOption.resolution
        resolutions = if (array.size == 1 && resolution != null) {
            listSize.map { resolution.calculateResolution(it).toString() }
                .toTypedArray()
        } else {
            emptyArray()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupUI() {
        binding.apply {
            val listSize = resources.getStringArray(R.array.list_size)

            for (i in 0 until 4) {
                val resolutionText = if (mediaOption.data.size == 1) {
                    " - " + "${listSize[i]} - ${resolutions[i]}"
                } else {
                    " - " + listSize[i]
                }
                when (i) {
                    0 -> resolutionTextView1.text = resolutionText
                    1 -> resolutionTextView2.text = resolutionText
                    2 -> resolutionTextView3.text = resolutionText
                    3 -> resolutionTextView4.text = resolutionText
                }
            }

            onClickOptions()

            val videoCompressAdapter = VideoCompressAdapter(this@SelectCompressActivity)
            videoCompressAdapter.submitData = mediaOption.data
            viewPager.adapter = videoCompressAdapter
            indicator.setViewPager(viewPager)

            compress.setOnClickListener {
                val intent = Intent(this@SelectCompressActivity, ProcessActivity::class.java)
                val optionMedia = createOptionMedia()
                intent.passOptionMedia(optionMedia)
                Log.d("Ngueyn duy kahng", optionMedia.resolution.toString())

                startActivity(intent)
            }
        }
    }

    private fun onClickOptions() {
        binding.apply {
            val map = hashMapOf(
                optionSmall to listOf(labelTextView1, descriptionTextView1, resolutionTextView1),
                optionMedium to listOf(labelTextView2, descriptionTextView2, resolutionTextView2),
                optionLarge to listOf(labelTextView3, descriptionTextView3, resolutionTextView3),
                optionBest to listOf(labelTextView4, descriptionTextView4, resolutionTextView4),
                optionCustom to listOf(labelTextView5, descriptionTextView5, resolutionTextView5),
            )
            map.keys.forEachIndexed { index, constraintLayout ->
                if (index == 0) constraintLayout.performClick()
                constraintLayout.setOnClickListener { view ->
                    resolutionSelected = index
                    applyItemStyle(constraintLayout, map)
                }
            }
        }

    }

    private fun applyItemStyle(
        item: ConstraintLayout, map: HashMap<ConstraintLayout, List<AppCompatTextView>>
    ) {
        map.forEach { (key, value) ->
            key.setBackgroundResource(if (key == item) R.drawable.corners_troke_blu_2 else R.drawable.corners_troke_gray_2)
            value.forEach {
                it.setTextColor(if (key == item) getColor(R.color.maintream) else Color.BLACK)
            }
        }
    }

    private fun createOptionMedia(): OptionMedia {
        val resolution = if (resolutions.isNotEmpty()) {
            resolutions[resolutionSelected].split(" x ")
        } else {
            emptyList()
        }

        return OptionMedia(
            mediaOption.data,
            listSize[resolutionSelected],
            "mp4",
            if (resolution.isEmpty()) null else Resolution(
                resolution[0].trim().toInt(),
                resolution[1].trim().toInt()
            ),
            MediaAction.CompressVideo
        )
    }
}
