package part2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
            Socket socket = new Socket(routerIP, Integer.parseInt(routerPort));
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //TODO Talk to client
            socket.close();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about router: " + routerIP + ":" + routerPort);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + routerIP + ":" + routerPort);
            System.exit(1);
        }

        //Logic for the client talking to the server goes here
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.setServerName("Test Server");
        client.setRouterIP("127.0.0.1");
        client.setRouterPort("5555");
        new Thread(client).start();
    }
}