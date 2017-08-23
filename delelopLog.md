###0823
修改了ServerMaster的select(),基本完成了Reactor模型的编写
定义了handler,但是还没有pipeline之类的东西
随便写了一个Http的handler
一些Test case
jdk风格修改为1.7

selector注册和select()必须在同一线程

为什么我本地能够执行java cn.asens.Starter,但是在服务器不能执行

--已改成jar的方式,http://www.asens.cn:8080/
