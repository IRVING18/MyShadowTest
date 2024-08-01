package com.wz.plugin_app.lib;

import android.text.TextUtils;
import com.tencent.shadow.sample.host.lib.inter.ObjectFactory;
import com.wz.plugin_app.PluginPublishXHSSocial;

/**
 * @description : ObjectFactoryImpl 
 * @date : 2020/6/9 11:43 AM 
 * @author : qilufei 
 * @version :
 */
public final class ObjectFactoryImpl implements ObjectFactory {

    @Override
    public Object getObject(String className) {
        return new PluginPublishXHSSocial();
    }
}
