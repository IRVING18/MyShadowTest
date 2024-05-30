package com.wz.plugin_app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * 有道领世
 * MyShadowTest
 * Description:
 * Created by wangzheng on 2024/5/30 15:08
 * Copyright @ 2024 网易有道. All rights reserved.
 **/
class PluginActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val str = intent.getStringExtra("11111")
        Toast.makeText(this, "$str", Toast.LENGTH_SHORT).show()
        findViewById<View>(R.id.bt).setOnClickListener {
            setResult(10000, Intent().apply {
                putExtra("1111",1111)
            })
            finish()
        }
    }
}