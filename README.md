# AsensServer

AsensServer will be a high performance,strong server and easy to use.

Project start at 2017-07-29 
 

### 开发日志
#### 0823
完成Reactor模型,handler接口,简单http接收和响应

各种tcp,socket测试

修改代码风格到jdk1.7

jar部署到服务器

#### 0827
1 SocketChannelWrapper的write和flush

2 Http中读取文件并写出的处理,更合理且健壮的文件写出,
一次写不完就注册write事件,
在write中flush

