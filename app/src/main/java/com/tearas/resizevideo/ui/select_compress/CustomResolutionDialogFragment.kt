package com.tearas.resizevideo.ui.select_compress

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.tearas.resizevideo.R
import com.tearas.resizevideo.core.BaseDialogFragment
import com.tearas.resizevideo.databinding.FragmentDialogCustomResolutionBinding

import com.tearas.resizevideo.model.Resolution
import com.tearas.resizevideo.utils.ResolutionUtils


class CustomResolutionDialogFragment :
    BaseDialogFragment<FragmentDialogCustomResolutionBinding>(R.layout.fragment_dialog_custom_resolution) {
    private val viewModel: SelectCompressViewModel by lazy {
        ViewModelProvider(requireActivity())[SelectCompressViewModel::class.java]
    }

    companion object {
        fun getInstance(): CustomResolutionDialogFragment {
            return CustomResolutionDialogFragment()
        }
    }

    fun show(context: FragmentActivity) = this.show(
        context.supportFragmentManager,
        CustomResolutionDialogFragment::class.simpleName
    )

    override fun getViewBinding(view: View): FragmentDialogCustomResolutionBinding {
        return FragmentDialogCustomResolutionBinding.bind(view)
    }

    private lateinit var resolution: Resolution

    @SuppressLint("ResourceAsColor")
    override fun initView() {
        binding.apply {
            resolution = viewModel.resolutionOriginLiveData.value!!

            width.setText(resolution.width.toString())
            height.setText(resolution.height.toString())

            if (resolution.width == 0 && resolution.height == 0){
                aspectRatio.visibility = View.GONE
                aspectRatio.isChecked=false
            }

            save.setOnClickListener {
                viewModel.postCustomResolution(
                    Resolution(
                        width.text.toString().toInt(),
                        height.text.toString().toInt()
                    )
                )
                dismissNow()
            }

            val textWatcherWidth = createTextWatcher(isWidth = true)
            val textWatcherHeight = createTextWatcher(isWidth = false)

            width.setOnFocusChangeListener { view, b ->
                if (b) {
                    width.addTextChangedListener(textWatcherWidth)
                } else {
                    width.removeTextChangedListener(textWatcherWidth)
                }
            }

            height.setOnFocusChangeListener { view, b ->
                if (b) {
                    height.addTextChangedListener(textWatcherHeight)
                } else {
                    height.removeTextChangedListener(textWatcherHeight)
                }
            }

            dismiss.setOnClickListener {
                dismissNow()
            }

        }
    }

    private fun createTextWatcher(isWidth: Boolean): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (binding.aspectRatio.isChecked && p0 != null) {
                    val valueText = p0.toString().trim()
                    if (valueText.isNotEmpty()) {
                        val value = valueText.toInt()
                        val calculatedValue = if (isWidth) {
                            ResolutionUtils.calculateResolutionByRadio(
                                if (binding.aspectRatio.isChecked) resolution.getRatio() else 0f,
                                value,
                                null
                            )
                        } else {
                            ResolutionUtils.calculateResolutionByRadio(
                                if (binding.aspectRatio.isChecked) resolution.getRatio() else 0f,
                                null,
                                value
                            )
                        }
                        if (isWidth) {
                            binding.height.setText(calculatedValue.toString())
                        } else {
                            binding.width.setText(calculatedValue.toString())
                        }
                    }
                }
            }
        }
    }
}
