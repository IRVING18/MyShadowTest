package com.wz.host

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.tencent.shadow.dynamic.host.EnterCallback
import com.tencent.shadow.sample.host.R
import com.wz.base_constant.Constant
import com.wz.host.assists.AssistTestActivity
import com.wz.host.base.HostApplication

class MainActivity : Activity() {

    private var ll: LinearLayout? = null
    private val mHandler: Handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ll = findViewById<LinearLayout>(R.id.ll)
    }

    fun start_assist(view: View) {
        startActivity(Intent(this, AssistTestActivity::class.java))
    }

    fun start_plugin(view: View){
        PluginHelper.getInstance().singlePool.execute(Runnable {
            HostApplication.getApp().loadPluginManager(PluginHelper.getInstance().pluginManagerFile)

            /**
             * @param context context
             * @param formId  标识本次请求的来源位置，用于区分入口
             * @param bundle  参数列表, 建议在参数列表加入自己的验证
             * @param callback 用于从PluginManager实现中返回View
             */
            val bundle = Bundle() //插件 zip，这几个参数也都可以不传，直接在 PluginManager 中硬编码
            bundle.putString(
                Constant.KEY_PLUGIN_ZIP_PATH,
                PluginHelper.getInstance().pluginZipFile.getAbsolutePath()
            )
            bundle.putString(
                Constant.KEY_PLUGIN_NAME,
                Constant.PLUGIN_APP_NAME
            ) // partKey 每个插件都有自己的 partKey 用来区分多个插件，如何配置在下面讲到
            bundle.putString(
                Constant.KEY_ACTIVITY_CLASSNAME,
                "com.wz.plugin_app.PluginActivity"
            ) //要启动的插件的Activity页面
            bundle.putBundle(Constant.KEY_EXTRAS, Bundle().apply {
                putString("11111","hhhh")
            }) // 要传入到插件里的参数
            HostApplication.getApp().getPluginManager().enter(
                this@MainActivity,
                Constant.FROM_ID_START_ACTIVITY,
                bundle,
                object : EnterCallback {
                    override fun onShowLoadingView(view: View) {
                        Log.e("PluginLoad", "onShowLoadingView")
                        loading(view)
                        //这里进行加载视图
                    }

                    override fun onCloseLoadingView() {
                        Log.e("PluginLoad", "onCloseLoadingView")
                        mHandler.post {
                            ll!!.removeAllViews()
                        }
                    }

                    override fun onEnterComplete() {
                        // 启动成功
                        Log.e("PluginLoad", "onEnterComplete")
                    }
                })
        })
    }

    private fun loading(view: View) {
        mHandler.post(Runnable {
            ll!!.removeAllViews()
            ll!!.addView(view)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10000) {
            val str = data?.getIntExtra("1111",0)
            Toast.makeText(this, "$str", Toast.LENGTH_SHORT).show()
        }
    }
}