package com.chw.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class CustomGradlePlugin implements Plugin<Project> {
    /**
     * 插件被引入时要执行的方法
     * @param project 引入当前插件的 project
     */
    @Override
    void apply(Project project) {
        println "hello CustomGradlePlugin:" + project.name

        // 创建用于设置版本信息的扩展属性
        project.extensions.create("releaseInfo", ReleaseInfoExtension.class)

        // 创建用于更新版本信息的 task
        project.tasks.create("releaseInfoTask", ReleaseInfoTask.class)

        // 注册我们自定义的 Transform
        def appExtension = project.extensions.findByType(LibraryExtension.class)
        appExtension.registerTransform(new MyCustomTransform())
    }
}