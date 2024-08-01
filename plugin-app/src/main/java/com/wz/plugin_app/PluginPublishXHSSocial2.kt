package com.wz.plugin_app

import android.text.TextUtils
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.ven.assists.Assists
import com.ven.assists.Assists.click
import com.ven.assists.Assists.findById
import com.ven.assists.Assists.paste
import com.ven.assists.stepper.Step
import com.ven.assists.stepper.StepCollector
import com.ven.assists.stepper.StepImpl
import com.ven.assists.stepper.StepTag
import kotlinx.coroutines.delay

/**
 * 有道领世
 * MyShadowTest
 * Description:
 * Created by wangzheng on 2024/8/1 15:07
 * Copyright @ 2024 网易有道. All rights reserved.
 **/
class PluginPublishXHSSocial2 : StepImpl() {
    override fun onImpl(collector: StepCollector) {
        ToastUtils.showLong("新包下但是类名需要换")
    }
}