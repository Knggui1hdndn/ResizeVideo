package com.tearas.resizevideo.ui

import android.app.Notification.InboxStyle
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import com.tearas.resizevideo.R
import com.tearas.resizevideo.databinding.TabCustomBinding

interface OnTabSelectedListener {
    fun onTabSelected(position: Int)
}

class CustomTab : ConstraintLayout {
    private var binding: TabCustomBinding =
        TabCustomBinding.inflate(LayoutInflater.from(context), this, true)
    private var onTabSelectedListener: OnTabSelectedListener? = null

    init {
        val tabClickListener = OnClickListener { view ->
            val position = when (view) {
                binding.tab1 -> 0
                binding.tab2 -> 1
                else -> -1
            }
            onTabSelectedListener?.onTabSelected(position)
            handleButtonClick(view)
        }
        binding.tab1.setOnClickListener(tabClickListener)
        binding.tab2.setOnClickListener(tabClickListener)
    }


    constructor(context: Context) : super(context) {
        removeAllViews()
        addView(binding.root)
    }

    constructor(context: Context, attr: AttributeSet?, style: Int) : super(context, attr, style) {
        removeAllViews()
        addView(binding.root)

    }

    constructor(context: Context, attr: AttributeSet?) : super(context, attr) {
        removeAllViews()
        addView(binding.root)

    }

    fun setOnTabSelectedListener(listener: OnTabSelectedListener) {
         this.onTabSelectedListener = listener
    }

    private fun handleButtonClick(clickedButton: View) {
        val activeButton = clickedButton as MaterialButton
        val inactiveButton = if (clickedButton == binding.tab1) binding.tab2 else binding.tab1

        val transitionResId =
            if (clickedButton == binding.tab1) R.anim.trim_transition else R.anim.cut_transition
        val animation = AnimationUtils.loadAnimation(context, transitionResId)
        binding.overlay.startAnimation(animation)

        activeButton.setTextColor(ContextCompat.getColor(context, R.color.text_home_click))
        inactiveButton.setTextColor(ContextCompat.getColor(context, R.color.text_home_unclick))
    }

    fun setTabSelected(position: Int) {
        when (position) {
            0 -> handleButtonClick(binding.tab1)
            1 -> handleButtonClick(binding.tab2)
        }
    }

    fun attach(viewPager2: ViewPager2?, tab1Name: String, tab2Name: String) {
        binding.tab1.text = tab1Name
        binding.tab2.text = tab2Name

        viewPager2?.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setTabSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }
        })
     }
}
