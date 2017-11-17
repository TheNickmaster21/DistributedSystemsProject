package part2;

import part1.ProjectConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

    private Boolean active = false;

    private String name;
    private String port;

    private String routerIP;
    private String routerPort;

    @Override
    public void run() {
        ServerSocket serverSocket = null; // server socket for accepting connections
        try {
            serverSocket = new ServerSocket(Integer.parseInt(port));
            System.out.println("ServerRouter is Listening on port: " + ProjectConstants.PORT);
            active = true;
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + ProjectConstants.PORT);
            active = false;
        }

        while (active) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("ServerRouter connected with someone: " + clientSocket.getInetAddress().getHostAddress());
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
                String command = bufferedReader.readLine();
                bufferedReader.close();
                printWriter.close();
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Someone went wrong.");
                active = false;
            }
        }
    }

    public Boolean isActive() {
        return active;
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

    private void register(BufferedReader bufferedReader) throws IOException {
        //Connect to router and give my name
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.setName("Text Server");
        new Thread(server).start();
    }
}
