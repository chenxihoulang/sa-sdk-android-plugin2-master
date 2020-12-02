/*
 * Created by wangzhuozhou on 2015/08/12.
 * Copyright 2015－2020 Sensors Data Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sensorsdata.analytics.android.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.reflect.Instantiator
import org.gradle.invocation.DefaultGradle

/**
 * 神策数据分析Gradle插件
 * 1.创建扩展属性
 * 2.获取配置信息
 * 3.添加自定义Transform
 * 
 * apply plugin: 'com.sensorsdata.analytics.android'
 */
class SensorsAnalyticsPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        Instantiator ins = ((DefaultGradle) project.getGradle()).getServices().get(Instantiator)
        def args = [ins] as Object[]

        //创建配置参数扩展属性
        SensorsAnalyticsExtension extension = project.extensions.create("sensorsAnalytics", SensorsAnalyticsExtension, args)

        boolean disableSensorsAnalyticsPlugin = false
        boolean disableSensorsAnalyticsMultiThreadBuild = false
        boolean disableSensorsAnalyticsIncrementalBuild = false
        boolean isHookOnMethodEnter = false

        Properties properties = new Properties()
        //从gradle.properties文件中获取配置属性
        if (project.rootProject.file('gradle.properties').exists()) {
            properties.load(project.rootProject.file('gradle.properties').newDataInputStream())
            disableSensorsAnalyticsPlugin = Boolean.parseBoolean(properties.getProperty("sensorsAnalytics.disablePlugin", "false")) ||
                    Boolean.parseBoolean(properties.getProperty("disableSensorsAnalyticsPlugin", "false"))
            disableSensorsAnalyticsMultiThreadBuild = Boolean.parseBoolean(properties.getProperty("sensorsAnalytics.disableMultiThreadBuild", "false"))
            disableSensorsAnalyticsIncrementalBuild = Boolean.parseBoolean(properties.getProperty("sensorsAnalytics.disableIncrementalBuild", "false"))
            isHookOnMethodEnter = Boolean.parseBoolean(properties.getProperty("sensorsAnalytics.isHookOnMethodEnter", "false"))
        }

        //没有禁用神策插件
        if (!disableSensorsAnalyticsPlugin) {
            AppExtension appExtension = project.extensions.findByType(AppExtension.class)

            SensorsAnalyticsTransformHelper transformHelper = new SensorsAnalyticsTransformHelper(extension, appExtension)
            transformHelper.disableSensorsAnalyticsIncremental = disableSensorsAnalyticsIncrementalBuild
            transformHelper.disableSensorsAnalyticsMultiThread = disableSensorsAnalyticsMultiThreadBuild
            transformHelper.isHookOnMethodEnter = isHookOnMethodEnter

            //在app模块注册我们自定义的 Transform
            appExtension.registerTransform(new SensorsAnalyticsTransform(transformHelper))
        } else {
            Logger.error("------------您已关闭了神策插件--------------")
        }

    }
}