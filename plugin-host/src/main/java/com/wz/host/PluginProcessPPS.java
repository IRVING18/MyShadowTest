/*
 * Tencent is pleased to support the open source community by making Tencent Shadow available.
 * Copyright (C) 2019 THL A29 Limited, a Tencent company.  All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *     https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.wz.host;

import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.util.Log;

import com.tencent.shadow.dynamic.host.PluginProcessService;

/**
 * 加载插件用的service，必须注册
 * 用于加载插件，需要在plugin-manager的 SamplePluginManager中注册
 *
 * 每个插件对应一个
 */
public class PluginProcessPPS extends PluginProcessService {
//    public PluginProcessPPS() {
//        LoadPluginCallback.setCallback(new LoadPluginCallback.Callback() {
//
//            @Override
//            public void beforeLoadPlugin(String partKey) {
//                Log.d("PluginProcessPPS", "beforeLoadPlugin(" + partKey + ")");
//            }
//
//            @Override
//            public void afterLoadPlugin(String partKey, ApplicationInfo applicationInfo, ClassLoader pluginClassLoader, Resources pluginResources) {
//                Log.d("PluginProcessPPS", "afterLoadPlugin(" + partKey + "," + applicationInfo.className + "{metaData=" + applicationInfo.metaData + "}" + "," + pluginClassLoader + ")");
//            }
//        });
//    }
}
