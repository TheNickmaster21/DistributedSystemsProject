package part2;

import com.sun.tools.internal.ws.wsdl.document.Output;

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
            Socket socket = serverSocket.accept();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            PrintWriter printWriter = new PrintWriter(outputStreamWriter);
            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            switch (name) {
                case ("text"):
                    String input = bufferedReader.readLine();
                    while (!input.equals("end")) {
                        printWriter.write(input.toUpperCase());
                        input = bufferedReader.readLine();
                    }
                    break;

                case ("audio"):
                    for (byte b : Files.readAllBytes(Paths.get("Sample Audio.mp3"))) {
                        outputStreamWriter.write(b);
                    }
                    break;

                case ("image"):
                    for (byte b : Files.readAllBytes(Paths.get("Test Image.png"))) {
                        outputStreamWriter.write(b);
                    }
                    break;

                default:
                    System.out.println("Server created with invalid name: " + name);
            }

            socket.close();
        } catch (IOException e) {
            System.out.println("Server crashed!");
            System.out.println(e.toString());
        }

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
