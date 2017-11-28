package part2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class Router implements Runnable {

    private Boolean active = false;

    private String port;

    private Queue<Request> queue;
    
    private String companionRouterIP;
    private String companionRouterPort;

    private ConcurrentHashMap<String, RoutingTableEntry> routingTable = new ConcurrentHashMap<>();
    public Router() {
    	this.queue = new LinkedList<Request>();
    }
    @Override
    public void run() {
        ServerSocket serverSocket = null; // server socket for accepting connections
        try {
            serverSocket = new ServerSocket(Integer.parseInt(port));
            System.out.println("ServerRouter is Listening on port: " + port + " on " + InetAddress.getLocalHost());
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

    private class ActionHandler implements Runnable {

        private Socket socket;

        ActionHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
        	
            try {
                System.out.println("ServerRouter" + socket.getLocalPort() + " connected with someone: " + socket.getInetAddress().getHostAddress() );
                System.out.println("Router at port" + socket.getLocalPort() + ", Queue Size: " + queue.size());
                
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                String command = bufferedReader.readLine();
                System.out.println("Someone Said:" + command );
                while(command != null) {
                switch (command) {
                    case "register":
                        register(bufferedReader);
                        break;
                    case "request":
                        request(bufferedReader, printWriter);
                        break;
                    case "service":
                        service(bufferedReader, printWriter);
                }
                command = bufferedReader.readLine();
                
               }
                bufferedReader.close();
                printWriter.close();
            } catch (IOException e) {
                System.err.println("Something went wrong when communicating with a node!");
            }
            
        }

        private void register(BufferedReader bufferedReader) throws IOException {
            String[] registrationArgs =
                    {bufferedReader.readLine(), bufferedReader.readLine(), bufferedReader.readLine(), bufferedReader.readLine()};
            routingTable.put(
                    registrationArgs[0],
                    new RoutingTableEntry(registrationArgs[1], registrationArgs[2], registrationArgs[3]));
            
        }

        private void request(
                BufferedReader requestBufferedReader,
                PrintWriter requestPrintWriter) throws IOException {
        	System.out.println("Someone requested a file!");
        	
            String[] Args = {requestBufferedReader.readLine(), requestBufferedReader.readLine(), requestBufferedReader.readLine()};
            
            queue.add(new Request(Args[0],Args[1],Args[2]));
            
        }
        
        private void service(
                BufferedReader requestBufferedReader,
                PrintWriter requestPrintWriter) throws IOException {
        	
        	System.out.println("Someone wants to service !");
        	
        	String[] Args = {};
        	if(!queue.isEmpty()) {
        		Request r = queue.remove();
                System.out.println("Found a client to service !");
                requestPrintWriter.println(routingTable.get(r.requestKey).getIP());

                requestPrintWriter.println(routingTable.get(r.requestKey).getPort());
                
                requestPrintWriter.println(r.requestName);
                requestPrintWriter.println(r.requestType);
                
                
        	}
	
            	
        }

     
        
    }

    private static class RoutingTableEntry {

        private String IP;
        private String port;
        private String dataType;
        

        RoutingTableEntry(String IP, String port, String dataType) {
            this.IP = IP;
            this.port = port;
            this.dataType= dataType;
        }

        String getIP() {
            return IP;
        }

        String getPort() {
            return port;
        }
        
        String getDataType() {
        	return dataType;
        }
        
    }
    
    private class Request{
    	String requestType;
    	String requestName;
    	String requestKey;
    	
    	Request(String t, String r, String k){
    		requestType = t;
    		requestName = r;
    		requestKey = k;
    	}
    }

}