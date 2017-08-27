### 0823
修改了ServerMaster的select(),基本完成了Reactor模型的编写
定义了handler,但是还没有pipeline之类的东西
随便写了一个Http的handler
一些Test case
jdk风格修改为1.7

selector注册和select()必须在同一线程

为什么我本地能够执行java cn.asens.Starter,但是在服务器不能执行

--已改成jar的方式,http://www.asens.cn:8080/


#### 0827
1 SocketChannelWrapper的write和flush逻辑,在response中
调用该方法而不是在response中write

2 Http中读取文件并写出的处理,使用fileChannel的transferTo
方法,FileMessage中的position和totalCount共同判断文件是
否写出完成,一次写不完就注册write事件,在write中flush

next 支持HTTP/1.1

