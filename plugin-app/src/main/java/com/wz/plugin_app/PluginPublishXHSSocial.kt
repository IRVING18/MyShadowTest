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
class PluginPublishXHSSocial : StepImpl() {
    override fun onImpl(collector: StepCollector) {
        collector.next(StepTag.STEP_1) { it ->
            AppUtils.launchApp("com.xingin.xhs")
            return@next Step.get(StepTag.STEP_2, data = "字符串数据：2")
        }.next(StepTag.STEP_2) {
            runIO { delay(1000) }

            Assists.findById("com.xingin.xhs:id/e4v").forEach {
                it.parent.click()
                return@next Step.get(StepTag.STEP_7, data = "字符串数据：3333")
            }

            if (it.repeatCount == 5) {
                return@next Step.none
            }

            return@next Step.repeat
        }.next(StepTag.STEP_7) {
            Assists.findByText("我知道了").forEach {
                it.click()
                return@next Step.get(StepTag.STEP_7)
            }
            Assists.findByText("权限申请").forEach {
                Assists.findByText("确定").forEach {
                    it.click()
                    return@next Step.get(StepTag.STEP_7)
                }
            }
            Assists.findByText("允许").forEach {
                it.click()
                return@next Step.get(StepTag.STEP_8)
            }
            return@next Step.get(StepTag.STEP_8)
        }.next(StepTag.STEP_8) {
            Assists.findByTags("androidx.recyclerview.widget.RecyclerView").forEach {
                for (index in 0 until it.childCount) {
                    if (TextUtils.equals("android.widget.FrameLayout", it.getChild(index).className)) {
                        it.getChild(index).let { child ->
                            child.findById("com.xingin.xhs:id/e1s").forEach {
                                it.parent.parent.click()
                                return@next Step.get(StepTag.STEP_9)
                            }
                        }
                    }
                }
            }
            return@next Step.none
        }.next(StepTag.STEP_9) {
            runIO { delay(1000) }
            Assists.findById("com.xingin.xhs:id/a_v").forEach {
                it.click()
                return@next Step.get(StepTag.STEP_10)

            }
            return@next Step.none

        }.next(StepTag.STEP_10) {
            runIO { delay(1000) }
            Assists.findByText("下一步").forEach {
                it.click()
                return@next Step.get(StepTag.STEP_11)
            }
            return@next Step.none
        }.next(StepTag.STEP_11) {
            Assists.findById("com.xingin.xhs:id/h5p").forEach {
                it.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
                it.paste("${TimeUtils.getNowString()}: 蒸蒸日上")
                return@next Step.get(StepTag.STEP_12)
            }
            return@next Step.none

        }.next(StepTag.STEP_12) {
            Assists.findByText("发布笔记").forEach {
                it.click()
            }
            return@next Step.none

        }
    }
}