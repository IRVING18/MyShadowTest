package com.wz.host.assists

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ThreadUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.tencent.shadow.sample.host.databinding.ViewMainOverBinding
import com.ven.assists.Assists
import com.ven.assists.AssistsWindowManager
import com.ven.assists.GestureListener
import com.ven.assists.stepper.StepImpl
import com.ven.assists.stepper.StepListener
import com.ven.assists.stepper.StepManager
import com.ven.assists.stepper.StepTag
import com.wz.base_constant.Constant
import com.wz.host.PluginHelper
import com.wz.host.assists.step.GestureBottomTab
import com.wz.host.assists.step.GestureScrollSocial
import com.wz.host.assists.step.OpenWechatSocial
import com.wz.host.assists.step.PublishSocial
import com.wz.host.assists.step.PublishXHSSocial
import com.wz.host.assists.step.ScrollContacts
import com.wz.host.base.HostApplication
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.InputStream

object OverManager : StepListener, GestureListener {
    @SuppressLint("StaticFieldLeak")
    private var viewMainOver: ViewMainOverBinding? = null

    private fun createView(): ViewMainOverBinding? {
        return Assists.service?.let { it ->
            Assists.gestureListeners.add(this)
            StepManager.stepListeners.add(this)
            ViewMainOverBinding.inflate(LayoutInflater.from(it)).apply {
                llOption.isVisible = true
                llLog.isVisible = false
                btnCloseLog.isVisible = false
                btnOpenSocial.setOnClickListener {
                    beginStart(this)
                    StepManager.execute(OpenWechatSocial::class.java, StepTag.STEP_1, begin = true)
                }
                btnPublishSocial.setOnClickListener {
                    beginStart(this)
                    StepManager.execute(PublishSocial::class.java, StepTag.STEP_1, begin = true, data = "字符串数据：1")
                }
                btnPublishXhs.setOnClickListener {
                    beginStart(this)
                    StepManager.execute(PublishXHSSocial::class.java, StepTag.STEP_1, begin = true, data = "字符串数据：1")
                }
                btnStop.setOnClickListener {
                    stop()
                }
                btnCloseLog.setOnClickListener { showOption() }
                btnStopScrollLog.setOnClickListener {
                    isAutoScrollLog = !isAutoScrollLog
                }
                btnLog.setOnClickListener {
                    showLog()
                    btnCloseLog.isVisible = true
                    btnStop.isVisible = false
                }
                btnScrollContacts.setOnClickListener {
                    beginStart(this)
                    StepManager.execute(ScrollContacts::class.java, StepTag.STEP_1, begin = true)
                }
                btnClickBottomTab.setOnClickListener {
                    beginStart(this)
                    StepManager.execute(GestureBottomTab::class.java, StepTag.STEP_1, begin = true)
                }
                btnScrollSocial.setOnClickListener {
                    beginStart(this)
                    StepManager.execute(GestureScrollSocial::class.java, StepTag.STEP_1, begin = true)
                }
                btnAaa.setOnClickListener {
                    beginStart(this)
                    PluginHelper.getInstance().singlePool.execute {
                        HostApplication.getApp()
                            .loadPluginManager(PluginHelper.getInstance().pluginManagerFile)
                        val bundle = Bundle()
                        bundle.putString(
                            Constant.KEY_PLUGIN_ZIP_PATH,
                            PluginHelper.getInstance().pluginZipFile.absolutePath
                        )
                        bundle.putString(
                            Constant.KEY_PLUGIN_PART_KEY,
                            Constant.PART_KEY_PLUGIN_MAIN_APP
                        )
                        bundle.putString(
                            Constant.KEY_OBJECT_CLASSNAME,
                            "com.wz.plugin_app.PluginPublishXHSSocial"
                        )
                        HostApplication.getApp().pluginManager
                            .getObject(
                                it.context,
                                Constant.FROM_ID_GET_OBJECT.toLong(), bundle
                            ) { obj ->
                                if (obj is StepImpl) {
                                    StepManager.execute("com.wz.plugin_app.PluginPublishXHSSocial", obj, StepTag.STEP_1, begin = true)
                                }
                            }
                    }
                }
                btnDex.setOnClickListener {
                    beginStart(this)
                    btnDex.isSelected = !btnDex.isSelected
                    if (btnDex.isSelected) {
                        ToastUtils.showShort("111")
                        loadDexClassLoader(HostApplication.getApp(), "plugin-app-plugin-debug.apk","com.wz.plugin_app.PluginPublishXHSSocial")
                    } else {
                        ToastUtils.showShort("222")
                        loadDexClassLoader(HostApplication.getApp(), "plugin-app-plugin-debug-2.apk","com.wz.plugin_app.PluginPublishXHSSocial2")
                    }
                }
                root.setOnCloseClickListener {
                    clear()
                    return@setOnCloseClickListener false
                }

            }

        }
    }

    /**
     * 使用DexClassLoader方式加载类
     * @param apkName：动态加载的包的文件名，放在assets中模拟的
     */
    private fun loadDexClassLoader(context: Context, apkName: String, className:String) {
        val zipFile = File(context.filesDir, apkName)
        val inS: InputStream = HostApplication.getApp().assets.open(apkName)
        FileUtils.copyInputStreamToFile(inS, zipFile)

        // dex压缩文件的路径（可以是apk,jar,zip格式）
        val dexPath = zipFile.absolutePath

        //指定dexoutputpath为APP自己的缓存目录
        val dexOutputDir = context.getDir("dex$apkName", 0)

        // 定义DexClassLoader
        // 第一个参数：是dex压缩文件的路径
        // 第二个参数：是dex解压缩后存放的目录
        // 第三个参数：是C/C++依赖的本地库文件目录,可以为null
        // 第四个参数：是上一级的类加载器
        //DexClassLoader dexClassLoader = new DexClassLoader(dexPath,dexOutputDirs,null,getClassLoader());
        val dexClassLoader = dalvik.system.DexClassLoader(
            dexPath,
            dexOutputDir.absolutePath,
            null,
            HostApplication.getApp().classLoader
        )

        var pluginSocialClass: Class<*>? = null
        // 使用DexClassLoader加载类
        try {
            pluginSocialClass = dexClassLoader.loadClass(className)
            // 创建dynamic实例
            val stepImpl = pluginSocialClass.newInstance()
            if (stepImpl is StepImpl) {
                StepManager.execute(className, stepImpl, StepTag.STEP_1, begin = true)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun show() {

        viewMainOver ?: let {
            viewMainOver = createView()
            var width = ScreenUtils.getScreenWidth() / 2
            var height = SizeUtils.dp2px(300f)
            viewMainOver?.root?.layoutParams?.width = width
            viewMainOver?.root?.layoutParams?.height = height
            viewMainOver?.root?.minWidth = ScreenUtils.getScreenWidth() / 2
            viewMainOver?.root?.minHeight = height
            viewMainOver?.root?.setCenter()
            AssistsWindowManager.addAssistsWindowLayout(viewMainOver?.root)
        }
    }

    private fun beginStart(view: ViewMainOverBinding) {
        with(view) {
            clearLog()
            showLog()
            isAutoScrollLog = true
            btnCloseLog.isVisible = false
            btnStop.isVisible = true
        }
    }

    override fun onGestureBegin(startLocation: FloatArray, endLocation: FloatArray) {

    }

    override fun onGestureEnd() {
    }

    override fun onStepStop() {
        log("已停止")
    }

    private fun stop() {
        if (StepManager.isStop) {
            showOption()
            return
        }
        StepManager.isStop = true
        isAutoScrollLog = false
        viewMainOver?.btnStop?.isVisible = false
        viewMainOver?.btnCloseLog?.isVisible = true
    }

    fun showLog() {
        viewMainOver?.llOption?.isVisible = false
        viewMainOver?.llLog?.isVisible = true
    }

    fun showOption() {
        viewMainOver?.llOption?.isVisible = true
        viewMainOver?.llLog?.isVisible = false
    }

    fun clear() {
        Assists.gestureListeners.remove(this)
        StepManager.stepListeners.remove(this)
        viewMainOver = null
    }

    private val logStr: StringBuilder = StringBuilder()
    fun log(value: Any) {
        if (logStr.length > 1000) logStr.delete(0, 50)
        if (logStr.isNotEmpty()) logStr.append("\n")
        logStr.append(TimeUtils.getNowString())
        logStr.append("\n")
        logStr.append(value.toString())
        viewMainOver?.tvLog?.text = logStr
    }

    fun clearLog() {
        logStr.delete(0, logStr.length)
        viewMainOver?.tvLog?.text = ""
    }

    var isAutoScrollLog = true
        set(value) {
            if (value) onAutoScrollLog()
            viewMainOver?.btnStopScrollLog?.text = if (value) "停止滚动" else "继续滚动"
            field = value
        }

    private fun onAutoScrollLog() {
        viewMainOver?.scrollView?.fullScroll(NestedScrollView.FOCUS_DOWN)
        ThreadUtils.runOnUiThreadDelayed({
            if (!isAutoScrollLog) return@runOnUiThreadDelayed
            onAutoScrollLog()
        }, 250)
    }
}