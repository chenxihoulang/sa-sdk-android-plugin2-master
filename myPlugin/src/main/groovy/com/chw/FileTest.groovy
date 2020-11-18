import com.chw.Student
import groovy.json.JsonSlurper

class FileTest {
    static void main(String[] args) {
        println '`' * 100

        def file = new File("/Users/chaihongwei/android/sa-sdk-android-plugin2-master/gradle.properties")
        file.eachLine {
            println it
        }


        file.withInputStream {
            println '自动关闭流'
        }

        println '拷贝文件'
        def targetFile = new File('/Users/chaihongwei/android/sa-sdk-android-plugin2-master/build/gradle.properties')
        targetFile.withOutputStream { os ->
            file.withInputStream { ins ->
                os << ins //利用 OutputStream 的<<操作符重载，完成从 inputstream 到 OutputStream //的输出
            }
        }

        copy(file.absolutePath, targetFile.parentFile.path + '/gradle1.prop')

        def stu = [1, 2, 3]
        saveObject(stu, targetFile.parentFile.path + '/stu.obj')

        def stu1 = readObject(targetFile.parentFile.path + '/stu.obj')
        println(stu1)


        def reponse = getNetworkData('https://wanandroid.com/wxarticle/list/408/1/json')

        println reponse
        println reponse.data.datas.size()

        println '`' * 100
    }

    static copy(String sourcePath, String destPath) {
        try {
            //首先创建目标文件
            def desFile = new File(destPath)
            if (!desFile.exists()) {
                desFile.createNewFile()
            }

            //开始copy
            new File(sourcePath).withReader { reader ->
                def lines = reader.readLines()
                desFile.withWriter { writer ->
                    lines.each { line ->
                        writer.append(line + "\r\n")
                    }
                }
            }
            return true
        } catch (Exception e) {
            e.printStackTrace()
        }
        return false
    }

    static saveObject(Object object, String path) {
        try {
            //首先创建目标文件
            def desFile = new File(path)
            if (!desFile.exists()) {
                desFile.createNewFile()
            }

            desFile.withObjectOutputStream { out ->
                out.writeObject(object)
            }
            return true
        } catch (Exception e) {
        }
        return false
    }

    static readObject(String path) {
        def obj = null
        try {
            def file = new File(path)
            if (file == null || !file.exists()) return null
            //从文件中读取对象
            file.withObjectInputStream { input ->
                obj = input.readObject()
            }
        } catch (Exception e) {

        }
        return obj
    }


    static getNetworkData(String url) {
        //发送http请求
        def connection = new URL(url).openConnection()
        connection.setRequestMethod('GET')
        connection.connect()
        def response = connection.content.text
        //将 json 转化为实体对象
        def jsonSluper = new JsonSlurper()
        return jsonSluper.parseText(response)
    }
}
