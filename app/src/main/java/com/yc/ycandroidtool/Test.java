package com.yc.ycandroidtool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {


    public static class Client {

        private static int DEFAULT_SERVER_PORT = 12345;
        private static String DEFAULT_SERVER_IP = "127.0.0.1";
        private Socket socket = null;

        /**
         * 连接服务
         */
        public void connect() {
            try {
                socket = new Socket(DEFAULT_SERVER_IP, DEFAULT_SERVER_PORT);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 发送消息
         *
         * @param msg
         */
        public void send(String msg) {
            System.out.println("发送的消息为：" + msg);
            BufferedReader in = null;
            PrintWriter out = null;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                out.println(msg);
                System.out.println("返回消息为：" + in.readLine());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        /**
         * 关闭连接
         */
        public void close() {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static void main(String[] args) throws InterruptedException {
            // 运行客户端
            new Thread(new Runnable() {

                @SuppressWarnings("resource")
                public void run() {
                    Client client = new Client();
                    while (true) {

                        client.connect();//连接服务端
                        String scanner = new Scanner(System.in).nextLine();

                        if(!scanner.equals("q")) {
                            client.send(scanner);//发送消息
                        }else {

                            client.close();
                            break;
                        }

                        client.close();

                    }
                }
            }).start();

        }
    }

    public static class Server {

        private static int DEFAULT_PORT = 12345;
        private static ServerSocket server;

        //线程池
        private static ExecutorService executorService = Executors.newFixedThreadPool(60);


        public static void start() throws IOException {
            start(DEFAULT_PORT);
        }

        public synchronized static void start(int port) throws IOException {
            if (server != null) {
                return;
            }

            try {
                //启动服务
                server = new ServerSocket(port);
                System.out.println("服务器已启动，端口号：" + port);

                // 通过无线循环监听客户端连接
                while (true) {
                    Socket socket = server.accept();
                    // 当有新的客户端接入时，会执行下面的代码
                    executorService.execute(new ServerHandler(socket));
                }
            } finally {
                if (server != null) {
                    System.out.println("服务器已关闭。");
                    server.close();
                }

            }

        }

        public static void main(String[] args) throws InterruptedException {

            // 运行服务端
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Server.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }

    public static class ServerHandler implements Runnable{
        private Socket socket;

        public ServerHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            BufferedReader in = null;
            PrintWriter out = null;
            String msg = null;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(),true);
                while ((msg = in.readLine()) != null && msg.length()!=0) {//当连接成功后在此等待接收消息（挂起，进入阻塞状态）
                    System.out.println("server received : " + msg);
                    out.print("received~\n");
                    out.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
