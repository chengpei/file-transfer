package top.chengpei.filetransfer;

import top.chengpei.filetransfer.client.ClientSession;
import top.chengpei.filetransfer.console.ConsoleManager;
import top.chengpei.filetransfer.client.FileTransferClient;
import top.chengpei.filetransfer.server.FileTransferServer;

import java.util.Scanner;

public class Main {

    private static FileTransferServer fileTransferServer;

    private static FileTransferClient fileTransferClient;

    private static ClientSession session;

    public static void main(String[] args) {
        String mode;
        if (args.length > 0 && "client".equals(args[0])) {
            mode = "client";
        } else {
            mode = "server";
        }
        String port = "7071";
        if ("server".equals(mode)) {
            fileTransferServer = new FileTransferServer();
            if (args.length == 1) {
                port = args[0];
            }
            String finalPort = port;
            new Thread(() -> {
                System.out.println("启动服务端。。。。端口:" + finalPort);
                fileTransferServer.start(Integer.parseInt(finalPort));
            }).start();
        } else {
            String serverIp;
            if (args.length == 1) {
                throw new RuntimeException("客户端模式请输入服务端的IP");
            } else if (args.length == 2) {
                serverIp = args[1];
            } else if (args.length == 3) {
                serverIp = args[1];
                port = args[2];
            } else {
                serverIp = args[1];
                port = args[2];
            }
            System.out.println("启动客户端。。。。");
            System.out.println("服务器地址：" + serverIp + ":" + port);
            fileTransferClient = new FileTransferClient(serverIp, Integer.parseInt(port));
            session = fileTransferClient.connect();
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("> ");
        ConsoleManager consoleManager = ConsoleManager.init(session);
        while (scanner.hasNext()) {
            String cmd = scanner.nextLine();
            if ("stop".equals(cmd)) {
                break;
            } else {
                consoleManager.exec(cmd);
            }
            System.out.println();
            System.out.print("> ");
        }
        System.out.println("正在退出程序。。。");
        if (session != null) {
            session.close();
        }
        if (fileTransferClient != null) {
            fileTransferClient.stop();
        }
        if (fileTransferServer != null) {
            fileTransferServer.stop();
        }
    }
}
