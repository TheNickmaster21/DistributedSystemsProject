package part2;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Server implements Runnable {

    private String name;
    private String port;

    private String routerIP;
    private String routerPort;

    @Override
    public void run() {
        register();

        acceptClientCommunication();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getRouterIP() {
        return routerIP;
    }

    public void setRouterIP(String routerIP) {
        this.routerIP = routerIP;
    }

    public String getRouterPort() {
        return routerPort;
    }

    public void setRouterPort(String routerPort) {
        this.routerPort = routerPort;
    }

    private void register() {
        try {
            Socket socket = new Socket(routerIP, Integer.parseInt(routerPort));
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("register");
            printWriter.println(name);
            printWriter.println(InetAddress.getLocalHost().getHostAddress());
            printWriter.println(port);
            printWriter.close();
            socket.close();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about router: " + routerIP + ":" + routerPort);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + routerIP + ":" + routerPort);
            System.exit(1);
        }
    }

    private void acceptClientCommunication() {
        try {
            ServerSocket serverSocket = new ServerSocket(Integer.valueOf(port));
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();

                DataTransferMethods.sendFile(socket, name);

                socket.close();

                System.out.println("Sent File!");
            }
        } catch (IOException e) {
            System.out.println("Server crashed!");
            System.out.println(e.toString());
        }
    }

    public static void main(String[] args) throws IOException {
        Server server1 = new Server();
        server1.setName("Sample Audio.mp3");
        server1.setPort("5600");
        server1.setRouterIP("127.0.0.1");
        server1.setRouterPort("5555");
        new Thread(server1).start();

        Server server2 = new Server();
        server2.setName("Test Image.jpg");
        server2.setPort("5601");
        server2.setRouterIP("127.0.0.1");
        server2.setRouterPort("5555");
        new Thread(server2).start();

        Server server3 = new Server();
        server3.setName("testText.txt");
        server3.setPort("5602");
        server3.setRouterIP("127.0.0.1");
        server3.setRouterPort("5556"); //Different
        new Thread(server3).start();

        System.out.println(InetAddress.getLocalHost().getHostAddress());}
}
