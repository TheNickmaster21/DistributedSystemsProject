package part2;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

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
        //TODO Logic for the server actually accepting client connections and doing stuff goes here
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.setName("Test Server");
        server.setPort("5655");
        server.setRouterIP("127.0.0.1");
        server.setRouterPort("5555");
        new Thread(server).start();
    }
}
