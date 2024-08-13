因为某些极端网络环境，导致我传文件非常非常不方便，所以想到写了这么一个小工具，进行点对点的文件传输。
编译出来的jar不到10M，使用netty零拷贝FileRegion，定义了简单的文件传输协议

| 指令 | 描述 |
| ------------- | ------------- |
| 请求下载文件（AcquireFileRequestCommand） | 请求下载文件，一般是客户端发给服务端，请求下载服务端的某文件 |
| 预发送文件（PrepareSendFileCommand） | 准备发送文件给对端 |
| 准备接收文件（BeReadyReceiveFileCommand） | 收到预发送文件后，回复该指令 |
| 接收文件完成（CompleteFileReceiveCommand） | 接收完文件内容后恢复此指令 |

使用方法：
maven编译出来jar文件，file-transfer-1.0-jar-with-dependencies.jar，该文件包含了所有依赖

启动服务端：
```
java -jar file-transfer-1.0-jar-with-dependencies.jar
```
启动服务端会默认绑定7071端口，也可用在后面跟一个端口修改端口

启动客户端：
```
java -jar file-transfer-1.0-jar-with-dependencies.jar client 127.0.0.1 7071
```
启动客户端比服务端多3个参数，第一个固定client，第二个是服务端的IP，第三个服务端的端口号

先启动服务端，然后启动客户端，客户端启动后进入一个命令行模式，支持send和get命令

发送文件给服务端：
```
send /Users/chengpei/Desktop/test.jar
```
获取服务端的文件：
```
get /root/test.jar
```

客户端发送文件时序图

| 客户端  |                                                   | 服务端         |
|------|---------------------------------------------------|-------------|
| send | ----------发送PrepareSendFileCommand---------->     | 接收到预发送文件    |
|      | <----------回复BeReadyReceiveFileCommand----------  |             |
|      | ----------发送文件内容---------->                       | 写文件         |
|      | ----------发送文件内容---------->                       | 写文件         |
|      | <----------发送CompleteFileReceiveCommand---------- | 检查字节数和文件大小对上 |

客户端获取文件时序图

| 客户端 |                                                   | 服务端       |
|-----|---------------------------------------------------|-----------|
| get | ----------发送AcquireFileRequestCommand---------->  | 接收到获取文件指令 |
|  接收到预发送文件  | <----------发送PrepareSendFileCommand----------     |           |
|     | ----------回复BeReadyReceiveFileCommand---------->  |           |
|  写文件  | <----------发送文件内容----------                       |           |
|  写文件  | <----------发送文件内容----------                       |           |
|  检查字节数和文件大小对上 | ----------发送CompleteFileReceiveCommand----------> |  |
