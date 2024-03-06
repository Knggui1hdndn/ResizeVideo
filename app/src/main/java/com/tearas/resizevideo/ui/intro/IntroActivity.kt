package com.tearas.resizevideo.ui.intro

import android.content.Intent
import com.tearas.resizevideo.R
import com.tearas.resizevideo.core.BaseActivity
import com.tearas.resizevideo.databinding.ActivityIntroBinding
import com.tearas.resizevideo.model.IntroModel
import com.tearas.resizevideo.ui.sup_vip.SubVipActivity

class IntroActivity : BaseActivity<ActivityIntroBinding>() {
    override fun getViewBinding() = ActivityIntroBinding.inflate(layoutInflater)
    override fun initView() {
        showBannerAds(binding.bannerAds)
        val listIntro = arrayListOf<IntroModel>(
            IntroModel(
                getString(R.string.title_intro_1),
                getString(R.string.description_intro_1),
                R.drawable.intro_1
            ),
            IntroModel(
                getString(R.string.title_intro_2),
                getString(R.string.description_intro_2),
                R.drawable.intro_2
            ),
            IntroModel(
                getString(R.string.title_intro_3),
                getString(R.string.description_intro_3),
                R.drawable.intro_3
            ),
            IntroModel(
                getString(R.string.title_intro_4),
                getString(R.string.description_intro_4),
                R.drawable.intro_4
            )
        )
        binding.apply {
            val introAdapter = IntroAdapter()
            introAdapter.submitData = listIntro
            viewPager2.adapter = introAdapter
            circleIndicator3.setViewPager(viewPager2)

            btnNext.setOnClickListener {
                val currentItem = viewPager2.currentItem
                if (currentItem == 3) {
                    startActivity(Intent(this@IntroActivity, SubVipActivity::class.java))
                    finish()
                    return@setOnClickListener
                }
                viewPager2.setCurrentItem(currentItem + 1, true)

            }
        }

    }
}