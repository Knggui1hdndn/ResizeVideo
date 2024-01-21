package com.knd.duantotnghiep.testsocket.core

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    private var _binding: VB? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getViewBinding()
        setContentView(binding.root)

        initData()
        initView()
        initObserver()
    }

    fun showMessage(message: String, isShort: Boolean = true) =
        Toast.makeText(this, message, if (isShort) Toast.LENGTH_SHORT else Toast.LENGTH_LONG).show()

    fun setToolbar(
        toolbar: Toolbar,
        titleToolbar: String? = null,
        iconDrawable: Drawable,
        enabled: Boolean = true
    ) {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(iconDrawable)
            setDisplayShowHomeEnabled(enabled)
            if (titleToolbar != null) title = titleToolbar
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (menu != null && getMenu() != -1) {
            menuInflater.inflate(getMenu(), menu)
            return true
        }
        return super.onCreateOptionsMenu(menu)
    }

    open fun getMenu(): Int = -1
    open fun initData() {}
    open fun initView() {}
    open fun initObserver() {}
    abstract fun getViewBinding(): VB
}