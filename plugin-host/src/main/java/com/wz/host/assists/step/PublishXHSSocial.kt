package com.wz.host.assists.step

import android.text.TextUtils
import android.view.accessibility.AccessibilityNodeInfo
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.TimeUtils
import com.ven.assists.Assists
import com.ven.assists.Assists.click
import com.ven.assists.Assists.findById
import com.ven.assists.Assists.paste
import com.ven.assists.stepper.Step
import com.ven.assists.stepper.StepBean
import com.ven.assists.stepper.StepCollector
import com.ven.assists.stepper.StepImpl
import com.ven.assists.stepper.StepTag
import com.wz.host.assists.OverManager
import kotlinx.coroutines.delay

class PublishXHSSocial : StepImpl() {
    override fun onImpl(collector: StepCollector) {
        val list = arrayListOf<StepBean>()
        list.forEach {item->
            collector.next(item.id) {
                when(item.action?.name) {
                    "launchApp" -> {
                        item.action?.params?.let {
                            AppUtils.launchApp(it)
                            return@next Step.get(item.nextId, data = "字符串数据：2")
                        }
                        return@next Step.none
                    }
                    "findById" -> {
                        Assists.findById(item.action?.params ?: "").forEach {

                        }
                        return@next Step.none
                    }
                    else-> {
                        return@next Step.none
                    }
                }

            }
        }


        collector.next(StepTag.STEP_1) { it ->
            OverManager.log("启动小红书")
            AppUtils.launchApp("com.xingin.xhs")
            it.data?.let {
                OverManager.log("PublishSocial STEP_1 收到数据：$it")
            }
            return@next Step.get(StepTag.STEP_2, data = "字符串数据：2")
        }.next(StepTag.STEP_2) {
            runIO { delay(1000) }

            it.data?.let {
                OverManager.log("收到数据：$it")
            }
            Assists.findById("com.xingin.xhs:id/e4v").forEach {
//                val screen = it.getBoundsInScreen()
//                if (screen.left > 630 && screen.top > 1850) {
                    OverManager.log("已打开小红书主页，点击【+号】")
                    it.parent.click()
                    return@next Step.get(StepTag.STEP_7, data = "字符串数据：3333")

//                }
            }

//            if (it.repeatCount == 5) {
//                return@next Step.get(StepTag.STEP_1, data = "字符串数据：1111111")
//            }

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
            OverManager.log("选择第一张相片")
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
            OverManager.log("点击下一步")
            Assists.findById("com.xingin.xhs:id/a_v").forEach {
                it.click()
                return@next Step.get(StepTag.STEP_10)

            }
            return@next Step.none

        }.next(StepTag.STEP_10) {
            runIO { delay(1000) }
            OverManager.log("点击下一步")
            Assists.findByText("下一步").forEach {
                it.click()
                return@next Step.get(StepTag.STEP_11)
            }
            return@next Step.none
        }.next(StepTag.STEP_11) {
            OverManager.log("输入发表内容")
            Assists.findById("com.xingin.xhs:id/h5p").forEach {
                it.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
                it.paste("${TimeUtils.getNowString()}: 蒸蒸日上")
                return@next Step.get(StepTag.STEP_12)
            }
            return@next Step.none

        }.next(StepTag.STEP_12) {
            OverManager.log("点击发布")
            Assists.findByText("发布笔记").forEach {
                it.click()
            }
            return@next Step.none

        }
    }
}