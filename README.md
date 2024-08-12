因为某些极端网络环境，导致我传文件非常非常慢，所以写了这么一个小工具，可以进行点对点的文件传输。
编译出来的jar不到10M，支持服务端和客户端
使用方法：
maven编译出来jar文件，file-transfer-1.0-jar-with-dependencies.jar，该文件包含了所有依赖

启动服务端：
java -jar file-transfer-1.0-jar-with-dependencies.jar

启动服务端会默认绑定7071端口，也可用在后面跟一个端口修改端口

启动客户端：
java -jar file-transfer-1.0-jar-with-dependencies.jar client 127.0.0.1 7071

启动客户端比服务端多3个参数，第一个固定client，第二个是服务端的IP，第三个服务端的端口号

先启动服务端，然后启动客户端，客户端启动后进入一个命令行模式，支持send和get命令

发送文件给服务端：
