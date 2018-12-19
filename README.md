# MvpGenerator-plugin
Android mvp 快速创建插件

Mvpgenerator-plugin Kotlin 修改版1.0
1 开发这参照example例子中自定义example格式中的MvpActivity、MvpFragment
2 在helper中有Mvpgenerator-plugin必须类和接口，这里必须放在同一个package下
3 对于创建Activity自动生成的${NAME}ActivitySubComponent 中的方法移动和注解标注类里面有相关说明
4 对于创建Fragment自动生成的${NAME}FragmentSubComponent中也有相关后续操作说明
5 很重要，这是自定义的指定
配置文件mvpgenerator.properties放在project下或者module下 
common.package= example下的mvphelper几个类的存放报名
base.activity.package= MvpActivity类所在package指定
base.fragment.package= MvpFragment类所在package指定

步骤：
1 创建 base di
2 创建activity、fragment
3 根据自动生成的SubComponent提示做出相应后续操作
