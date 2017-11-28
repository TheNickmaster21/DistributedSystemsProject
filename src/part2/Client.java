package part2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable {

    private String serverName;
    private String serverIP;
    private String serverPort;

    private String routerIP;
    private String routerPort;

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
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

    @Override
    public void run() {
        getServerData();

        doClientOperation();
    }

    private void getServerData() {
        try {
            Socket socket = new Socket(routerIP, Integer.parseInt(routerPort));
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter.println("find");
            printWriter.println(serverName);
            setServerIP(bufferedReader.readLine());
            setServerPort(bufferedReader.readLine());
            socket.close();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about router: " + routerIP + ":" + routerPort);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + routerIP + ":" + routerPort);
            System.exit(1);
        }

    }

    private void doClientOperation() {
        try {
            Socket socket = new Socket(serverIP, Integer.parseInt(serverPort));

            DataTransferMethods.receiveFile(socket, serverName);

            socket.close();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about router: " + routerIP + ":" + routerPort);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + routerIP + ":" + routerPort);
            System.exit(1);
        }

        System.out.println("Received file!");
    }

    public static void main(String[] args) throws IOException {
        Client client1 = new Client();
        client1.setServerName("Sample Audio.mp3");
        client1.setRouterIP("127.0.0.1");
        client1.setRouterPort("5556"); //Different
        new Thread(client1).start();

        Client client2 = new Client();
        client2.setServerName("Test Image.jpg");
        client2.setRouterIP("127.0.0.1");
        client2.setRouterPort("5555");
        new Thread(client2).start();

        Client client3 = new Client();
        client3.setServerName("testText.txt");
        client3.setRouterIP("127.0.0.1");
        client3.setRouterPort("5555");
        new Thread(client3).start();

        System.out.println(InetAddress.getLocalHost().getHostAddress());
    }
}