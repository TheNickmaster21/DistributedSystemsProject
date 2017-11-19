package part2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;

public class Router implements Runnable {

    private Boolean active = false;

    private String port;

    private String companionRouterIP;
    private String companionRouterPort;

    private ConcurrentHashMap<String, RoutingTableEntry> routingTable = new ConcurrentHashMap<>();

    @Override
    public void run() {
        ServerSocket serverSocket = null; // server socket for accepting connections
        try {
            serverSocket = new ServerSocket(Integer.parseInt(port));
            System.out.println("ServerRouter is Listening on port: " + port);
            active = true;
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port);
            active = false;
        }

        while (active) {
            try {
                startActionHandler(serverSocket.accept());
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

    private void startActionHandler(final Socket socket) {
        new Thread(new ActionHandler(socket)).start();
    }

    public static void main(String[] args) throws IOException {
        Router router1 = new Router();
        router1.setPort("5555");
        router1.setCompanionRouterIP("127.0.0.1");
        router1.setCompanionRouterPort("5556");
        new Thread(router1).start();

        Router router2 = new Router();
        router2.setPort("5556");
        router2.setCompanionRouterIP("127.0.0.1");
        router2.setCompanionRouterPort("5555");
        new Thread(router2).start();
    }

    private class ActionHandler implements Runnable {

        private Socket socket;

        ActionHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                System.out.println("ServerRouter connected with someone: " + socket.getInetAddress().getHostAddress());
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
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
                socket.close();
            } catch (IOException e) {
                System.err.println("Something when wrong when communicating with a node!");
            }
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
    }

    private static class RoutingTableEntry {

        private String IP;
        private String port;

        RoutingTableEntry(String IP, String port) {
            this.IP = IP;
            this.port = port;
        }

        String getIP() {
            return IP;
        }

        String getPort() {
            return port;
        }
    }
}