import com.chw.Person
import com.chw.Student
import com.chw.Teacher
import jdk.internal.org.objectweb.asm.tree.analysis.Value

class GroovyTest {
    static void main(String[] args) {
        println "*" * 100

        println sayHello()
        println !"android"

        def name = "chw"
        println name ?: "default"

        def gName = """name is $name"""
        println gName
        println gName.class

        // 输出 ok
        def num = 5.21
        switch (num) {
            case [5.21, 4, "list"]:
                println num.properties
                println "ok"
                break
            default:
                break
        }

        def a = 1
        println a.properties

        println '"\"'
        println """
ssss
sfds
    ddd
"""

        //如果闭包没定义参数的话，则隐含有一个参数，这个参数名字叫 it，和 this 的作用类似。it 代表闭包的参数
        def closure1 = {
            println "closure1:$it"
        }
        closure1(100)

        def closure = { aa ->
            println aa
        }

        closure.call(1)
        closure()

        println "*" * 100

        //this 还会指向我们闭包定义处的类或者实例本身，而 owner、delegate 则会指向离它最近的那个闭包对象
        def scriptClouser = {
            // 代表闭包定义处的类
            println "scriptClouser this:" + this
            // 代表闭包定义处的类或者对象
            println "scriptClouser owner:" + owner
            // 代表任意对象，默认与 ownner 一致
            println "scriptClouser delegate:" + delegate
        }

        // 输出都是 scrpitClouse 对象
        scriptClouser.call()

        def nestClouser = {
            def innnerClouser = {
                // 代表闭包定义处的类
                println "innnerClouser this:" + this
                // 代表闭包定义处的类或者对象
                println "innnerClouser owner:" + owner
                // 代表任意对象，默认与 ownner 一直
                println "innnerClouser delegate:" + delegate
            }

            // 修改默认的 delegate
            innnerClouser.delegate = scriptClouser

            innnerClouser.call()
        }

        // this 输出的是 nestClouser 对象，而 owner 与 delegate 输出的都是 innnerClouser 对象
        nestClouser.call()


//        def stu = new Student()
//        def tea = new Teacher()
//        stu.pretty.delegate = tea
//        // 要想使 pretty 闭包的 delegate 修改生效，必须选择其委托策略为 Closure.DELEGATE_ONLY，默认是 Closure.OWNER_FIRST。
//        stu.pretty.resolveStrategy = Closure.DELEGATE_ONLY
//        println stu.toString()


        def arr = [1, 2, 3] as int[]
        int[] array2 = [1, 2, 3, 4, 5]
        println arr
        println array2

        def test = [100, "hello", true]
        // 左移位表示向List中添加新元素
        test << 200
        println test
        println test[0]

        // list 定义
        def list = [1, -2, -3, 4, -5]
        // 排序
//        list.sort()
        // 使用自己的排序规则,绝对值从大到小排序
        list.sort { a1, b1 ->
            a1 == b1 ? 0 : Math.abs(a1) < Math.abs(b1) ? 1 : -1
        }

        println list
        println list.max {
            Math.abs(it)
        }

        println list.groupBy {
            it > 0 ? "正数" : "负数"
        }

        def aMap = [:]
        println aMap
        println aMap['name']
        aMap['name'] = 'chw'
        println aMap['name']

        aMap['key1'] = ['a': 1, 'b': 2]
        println aMap

        aMap.each { key, value ->
            println "key:$key,value:$value"
        }

        aMap.eachWithIndex { Map.Entry<Object, Object> entry, int index ->
            println "index:$index,entry:$entry"
        }

        def a12 = 1..<5
        println a12

        a12.each {
            println it
        }
        println '*' * 100

//        def person = new Person(name: 'android', age: 26)
//        println person.cry()


        ExpandoMetaClass.enableGlobally()
        //为类动态的添加一个属性
        Person.metaClass.sex = 'male'
        def person = new Person(name: 'android', age: 26)
        println person.sex
        person.sex = 'female'
        println "the new sex is:" + person.sex

        //为类动态的添加方法
        Person.metaClass.sexUpperCase = { -> sex.toUpperCase() }
        def person2 = new Person(name: 'android', age: 26)
        println person2.sexUpperCase()

        //为类动态的添加静态方法
        Person.metaClass.static.createPerson = {
            String name1, int age -> new Person(name: name1, age: age)
        }
        def person3 = Person.createPerson('android', 26)
        println person3.name + " and " + person3.age

        println '*' * 100
    }

    static sayHello() {
        return "hello groovy"
    }
}