package com.chw.plugin

import org.codehaus.groovy.antlr.treewalker.VisitorAdapter
import org.objectweb.asm.ClassWriter

/**
 *
 * @author ChaiHongwei* @date 2020-11-26 09:08
 */
class MyCustomClassVisitor extends VisitorAdapter {
    private ClassWriter cw

    MyCustomClassVisitor(ClassWriter classWriter) {
        this.cw = classWriter
    }


}
