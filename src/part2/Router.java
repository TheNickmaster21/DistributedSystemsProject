package part2;

import part1.ProjectConstants;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class Router implements Runnable {

    private Boolean active = false;

    private String port;

    private String companionRouterIP;
    private String companionRouterPort;

    private HashMap<String, RoutingTableEntry> routingTable = new HashMap<>();

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
                switch (command) {
                    case "register":
                        register(bufferedReader);
                        break;
                    case "find":
                        find(bufferedReader, printWriter, true);
                        break;
                    case "extra find":
                        find(bufferedReader, printWriter, false);
                }
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

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getCompanionRouterIP() {
        return companionRouterIP;
    }

    public void setCompanionRouterIP(String companionRouterIP) {
        this.companionRouterIP = companionRouterIP;
    }

    public String getCompanionRouterPort() {
        return companionRouterPort;
    }

    public void setCompanionRouterPort(String companionRouterPort) {
        this.companionRouterPort = companionRouterPort;
    }

    private void register(BufferedReader bufferedReader) throws IOException {
        String[] registrationArgs =
                {bufferedReader.readLine(), bufferedReader.readLine(), bufferedReader.readLine()};
        routingTable.put(
                registrationArgs[0],
                new RoutingTableEntry(registrationArgs[1], registrationArgs[2]));
    }

    private void find(
            BufferedReader requestBufferedReader,
            PrintWriter requestPrintWriter,
            Boolean checkOtherRouter) throws IOException {
        String[] findArgs = {requestBufferedReader.readLine()};
        if (routingTable.containsKey(findArgs[0])) {
            requestPrintWriter.println(routingTable.get(findArgs[0]).getIP());
            requestPrintWriter.println(routingTable.get(findArgs[0]).getPort());
            return;
        } else if (checkOtherRouter) {
            if (findFromOtherRouter(requestPrintWriter, findArgs[0])) {
                return;
            }
        }
        requestPrintWriter.println();
        requestPrintWriter.println();
    }

    private Boolean findFromOtherRouter(
            PrintWriter requestPrintWriter,
            String key) throws IOException {
        Boolean success = false;
        try {
            Socket socket = new Socket(companionRouterIP, Integer.parseInt(companionRouterPort));
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter.println("extra find");
            printWriter.println(key);
            String[] findResults = {bufferedReader.readLine(), bufferedReader.readLine()};

            if (findResults[0] != null && !"".equals(findResults[0])) {
                requestPrintWriter.println(findResults[0]);
                requestPrintWriter.println(findResults[1]);
                success = true;
            }

            printWriter.close();
            bufferedReader.close();
            socket.close();
        } catch (UnknownHostException e) {
            System.err.println("Failed to contact the other router!");
        }
        return success;
    }

    public static void main(String[] args) throws IOException {
        Router router = new Router();
        router.setPort("5555");
        new Thread(router).start();
    }

    class RoutingTableEntry {

        private String IP;
        private String port;

        RoutingTableEntry(String IP, String port) {
            this.IP = IP;
            this.port = port;
        }

        public String getIP() {
            return IP;
        }

        public String getPort() {
            return port;
        }
    }
}