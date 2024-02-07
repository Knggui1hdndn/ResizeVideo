package com.tearas.resizevideo.ui.select_compress

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.util.Log
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.tearas.resizevideo.core.BaseActivity
import com.tearas.resizevideo.R
import com.tearas.resizevideo.databinding.ActivitySelectCompressBinding
import com.tearas.resizevideo.model.OptionCompress
import com.tearas.resizevideo.model.OptionMedia
import com.tearas.resizevideo.model.Resolution
import com.tearas.resizevideo.ui.process.ProcessActivity
import com.tearas.resizevideo.utils.IntentUtils.getOptionMedia
import com.tearas.resizevideo.utils.IntentUtils.passOptionMedia
import com.tearas.resizevideo.utils.ResolutionUtils.calculateResolution
import com.tearas.resizevideo.utils.Utils
import com.tearas.resizevideo.utils.Utils.isDarkMode

class SelectCompressActivity : BaseActivity<ActivitySelectCompressBinding>() {

    private var resolutionSelected: Int = 0
    private var resolutions: Array<String> = arrayOf()
    private lateinit var newResolution: Resolution
    private var listSize: Array<OptionCompress> =
        arrayOf(
            OptionCompress.Small,
            OptionCompress.Medium,
            OptionCompress.Large,
            OptionCompress.Origin,
            OptionCompress.Custom,
            OptionCompress.CustomFileSize,
            OptionCompress.AdvanceOption

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

    private var fileSize: Long? = 0
    private lateinit var mediaOption: OptionMedia
    private lateinit var map: HashMap<ConstraintLayout, List<AppCompatTextView>>
    private fun setupData() {
        mediaOption = intent.getOptionMedia()!!
        val array = mediaOption.dataOriginal
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
            showBannerAds(bannerAds)
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
            val resolutionText = if (mediaOption.dataOriginal.size == 1) {
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
        videoCompressAdapter.submitData = mediaOption.dataOriginal
        binding.viewPager.adapter = videoCompressAdapter
        binding.indicator.setViewPager(binding.viewPager)
    }

    @SuppressLint("SetTextI18n")
    private fun onClickOptions() {
        binding.apply {

            map = hashMapOf(
                optionSmall to listOf(labelTextView1, descriptionTextView1, resolutionTextView1),
                optionMedium to listOf(labelTextView2, descriptionTextView2, resolutionTextView2),
                optionLarge to listOf(labelTextView3, descriptionTextView3, resolutionTextView3),
                optionBest to listOf(labelTextView4, descriptionTextView4, resolutionTextView4),
                optionCustom to listOf(labelTextView5, descriptionTextView5, resolutionTextView5),
                optionCustomFileSize to listOf(
                    labelTextView6,
                    descriptionTextView6,
                    resolutionTextView6
                ),
                optionSelectRBF to listOf(
                    labelTextView7,
                    descriptionTextView7,
                    resolutionTextView7
                ),
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

            optionCustomFileSize.setOnClickListener {
                resolutionSelected = 5
                CustomFileSizeDialogFragment(::handleCustomFileSize)
                    .show(supportFragmentManager, CustomFileSizeDialogFragment::class.simpleName)
            }

            optionSelectRBF.setOnClickListener { view ->
                showAdvancedCompression()
            }
        }
    }

    private fun showAdvancedCompression() {
        AdvanceCompressionBottomSheetFragment(mediaOption.dataOriginal[0].bitrate,
            object : ChoseAdvance {
                override fun onChoseAdvance(
                    optionCompress: OptionCompress,
                    bitRate: Long,
                    frameRate: Int
                ) {
                    mediaOption.optionCompress = optionCompress
                    mediaOption.bitrate = bitRate
                    mediaOption.frameRate = frameRate
                    resolutionSelected = 6
                    applyItemStyle(binding.optionSelectRBF, map)
                }
            }).show(supportFragmentManager, AdvanceCompressionBottomSheetFragment::class.simpleName)
    }

    private fun handleCustomFileSize(size: Long, unit: String) {
        applyItemStyle(binding.optionCustomFileSize, map)
        binding.descriptionTextView6.text =
            getString(R.string.description_custom_file_size) + " - $size $unit"
        fileSize =
            if (unit == "MB") Utils.convertMBtoBit(size) else Utils.convertKBtoBit(size)
     }

    private fun applyItemStyle(
        item: ConstraintLayout, map: HashMap<ConstraintLayout, List<AppCompatTextView>>
    ) {
        map.forEach { (key, value) ->
            key.setBackgroundResource(if (key == item) R.drawable.corners_troke_blu_2 else R.drawable.corners_troke_gray_2)
            value.forEach {
                it.setTextColor(
                    if (key == item) getColor(R.color.maintream) else if (
                        !isDarkMode())
                        Color.BLACK else Color.WHITE
                )
            }
        }
    }

    private fun createOptionMedia(): OptionMedia {
        return mediaOption.copy(
            dataOriginal = mediaOption.dataOriginal,
            optionCompress = listSize[resolutionSelected],
            mimetype = "mp4",
            newResolution = if (
                listSize[resolutionSelected] == OptionCompress.Custom
            ) newResolution else Resolution(),
            fileSize = this.fileSize!!
        )
    }
}
