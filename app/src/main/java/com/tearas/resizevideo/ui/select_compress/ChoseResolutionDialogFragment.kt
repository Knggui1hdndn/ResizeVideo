package com.tearas.resizevideo.ui.select_compress

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.tearas.resizevideo.R
import com.tearas.resizevideo.core.BaseDialogFragment
import com.tearas.resizevideo.databinding.FragmentDialogChoseResolutionBinding

import com.tearas.resizevideo.model.Resolution
import com.tearas.resizevideo.utils.ResolutionUtils.calculateResolutionByRadio


class ChoseResolutionDialogFragment :
    BaseDialogFragment<FragmentDialogChoseResolutionBinding>(R.layout.fragment_dialog_chose_resolution) {

    private val viewModel: SelectCompressViewModel by lazy {
        ViewModelProvider(requireActivity())[SelectCompressViewModel::class.java]
    }

    fun show(context: FragmentActivity) = this.show(
        context.supportFragmentManager,
        ChoseResolutionDialogFragment::class.simpleName
    )

    companion object {
        fun getInstance(): ChoseResolutionDialogFragment {
            return ChoseResolutionDialogFragment()
        }
    }

    override fun getViewBinding(view: View): FragmentDialogChoseResolutionBinding {
        return FragmentDialogChoseResolutionBinding.bind(view)
    }

    private lateinit var arrayRadio: Array<RadioButton>
    private lateinit var resolution: Resolution
    private var resolutionOptions: ArrayList<Resolution> = ArrayList()

    @SuppressLint("ResourceAsColor")
    override fun initView() {
        binding.apply {
            arrayRadio = arrayOf(option1, option2, option3, option4, option5, custom)
            resolution = viewModel.resolutionOriginLiveData.value!!
            setTextOptions()

            onClickRadio()

            viewModel.resolutionCustomLiveData.observe(this@ChoseResolutionDialogFragment) {
                binding.custom.text = it.toString()
                unChecked(binding.custom)
                if (resolutionOptions.size == 6) resolutionOptions[5] =
                    it else resolutionOptions.add(5, it)
                resolution = resolutionOptions[5]
            }

            save.setOnClickListener {
                viewModel.postResolution(resolution)
                dismissNow()
            }

            dismiss.setOnClickListener {
                dismissNow()
            }
        }
    }

    private fun onClickRadio() {
        arrayRadio.forEach { radio ->
            radio.setOnClickListener {
                when (it.id) {
                    R.id.custom -> CustomResolutionDialogFragment.getInstance()
                        .show(requireActivity())

                    R.id.option1 -> resolution = resolutionOptions[0]
                    R.id.option2 -> resolution = resolutionOptions[1]
                    R.id.option3 -> resolution = resolutionOptions[2]
                    R.id.option4 -> resolution = resolutionOptions[3]
                    R.id.option5 -> resolution = resolutionOptions[4]
                }
                Log.d("SCAAAAAAAAAAAAAA", "onClickRadio" + resolution)
                unChecked(radio)
            }
        }
    }

    private fun setTextOptions() {
        val widthDefault = arrayOf(240, 360, 480, 640, 800)
        var count = 0

        widthDefault.forEachIndexed { index, width ->
            val replaceWidth = if (resolution.width < width) {
                count += 1
                resolution.width
            } else width

            if (resolution.width > 0 && resolution.height > 0) {
                resolutionOptions.add(
                    Resolution().calculateResolutionByRadio(
                        resolution.getRatio(), replaceWidth, null
                    )
                )
                arrayRadio[index].text = resolutionOptions[index].toString()
            } else {
                resolutionOptions.add(
                    Resolution(replaceWidth, 0)
                )
                arrayRadio[index].text = resolutionOptions[index].width.toString()
            }


            if (count > 1 && arrayRadio[index].id != R.id.custom) {
                arrayRadio[index].visibility = View.GONE
            }
        }
        resolution = resolutionOptions[0]
    }

    private fun unChecked(radio: RadioButton) {
        arrayRadio.forEach {
            if (radio.id != it.id) it.isChecked = false
        }
    }
}