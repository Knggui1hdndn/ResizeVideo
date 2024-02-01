package com.tearas.resizevideo.ui.select_compress

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.tearas.resizevideo.core.BaseActivity
import com.tearas.resizevideo.R
import com.tearas.resizevideo.databinding.ActivitySelectCompressBinding
import com.tearas.resizevideo.model.OptionMedia
import com.tearas.resizevideo.model.Resolution
import com.tearas.resizevideo.ui.process.ProcessActivity
import com.tearas.resizevideo.utils.IntentUtils.getOptionMedia
import com.tearas.resizevideo.utils.IntentUtils.passOptionMedia
import com.tearas.resizevideo.utils.ResolutionUtils.calculateResolution

class SelectCompressActivity : BaseActivity<ActivitySelectCompressBinding>() {

    private var resolutionSelected: Int = 0
    private var resolutions: Array<String> = arrayOf()
    private lateinit var newResolution: Resolution
    private var listSize: Array<String> =
        arrayOf(
            Resolution.SMALL,
            Resolution.MEDIUM,
            Resolution.LARGE,
            Resolution.ORIGIN,
            Resolution.CUSTOM
        )
    private val viewModel: SelectCompressViewModel by lazy {
        ViewModelProvider(this)[SelectCompressViewModel::class.java]
    }

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
    private lateinit var map: HashMap<ConstraintLayout, List<AppCompatTextView>>
    private fun setupData() {
        mediaOption = intent.getOptionMedia()!!
        val array = mediaOption.data
        resolutions = if (array.size == 1) listSize.map {
            viewModel.postResolutionOrigin(array[0].resolution)
            array[0].resolution?.calculateResolution(it).toString()
        }.toTypedArray()
        else {
            viewModel.postResolutionOrigin(Resolution())
            emptyArray()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initObserver() {
        viewModel.resolutionLiveData.observe(this) {
            this.newResolution = it
            createOptionMedia()
            applyItemStyle(binding.optionCustom, map)
            val resolution = if (it.height == 0) it.width else it.toString()
            binding.descriptionTextView5.text =
                "${getString(R.string.description_custom_resolution)} - $resolution"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupUI() {
        binding.apply {
            setTextResolution()

            onClickOptions()

            setUpViewPager()

            compress.setOnClickListener {
                val intent = Intent(this@SelectCompressActivity, ProcessActivity::class.java)
                val optionMedia = createOptionMedia()
                intent.passOptionMedia(optionMedia)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setTextResolution() {
        val listSize = resources.getStringArray(R.array.list_size)

        for (i in 0 until 4) {
            val resolutionText = if (mediaOption.data.size == 1) {
                " - " + "${listSize[i]} - ${resolutions[i]}"
            } else {
                " - " + listSize[i]
            }
            when (i) {
                0 -> binding.resolutionTextView1.text = resolutionText
                1 -> binding.resolutionTextView2.text = resolutionText
                2 -> binding.resolutionTextView3.text = resolutionText
                3 -> binding.resolutionTextView4.text = resolutionText
            }
        }
    }

    private fun setUpViewPager() {
        val videoCompressAdapter = VideoCompressAdapter(this@SelectCompressActivity)
        videoCompressAdapter.submitData = mediaOption.data
        binding.viewPager.adapter = videoCompressAdapter
        binding.indicator.setViewPager(binding.viewPager)
    }

    private fun onClickOptions() {
        binding.apply {

            map = hashMapOf(
                optionSmall to listOf(labelTextView1, descriptionTextView1, resolutionTextView1),
                optionMedium to listOf(labelTextView2, descriptionTextView2, resolutionTextView2),
                optionLarge to listOf(labelTextView3, descriptionTextView3, resolutionTextView3),
                optionBest to listOf(labelTextView4, descriptionTextView4, resolutionTextView4),
                optionCustom to listOf(labelTextView5, descriptionTextView5, resolutionTextView5),
            )

            optionSmall.setOnClickListener { view ->
                resolutionSelected = 0
                applyItemStyle(optionSmall, map)
            }
            optionSmall.performClick()

            optionMedium.setOnClickListener { view ->
                resolutionSelected = 1
                applyItemStyle(optionMedium, map)
            }

            optionLarge.setOnClickListener { view ->
                resolutionSelected = 2
                applyItemStyle(optionLarge, map)
            }

            optionBest.setOnClickListener { view ->
                resolutionSelected = 3
                applyItemStyle(optionBest, map)
            }

            optionCustom.setOnClickListener { view ->
                resolutionSelected = 4
                ChoseResolutionDialogFragment.getInstance().show(this@SelectCompressActivity)
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
         return mediaOption.copy(
            data = mediaOption.data,
            size = listSize[resolutionSelected],
            mimetype = "mp4",
            newResolution = if (listSize[resolutionSelected] == Resolution.CUSTOM) newResolution else Resolution()
        )
    }
}
