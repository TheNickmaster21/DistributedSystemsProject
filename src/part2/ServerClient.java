package part2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerClient implements Runnable {
	
    private String name;
    private String port;
    private Socket socket;
    private String routerIP;
    private String routerPort;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;
    
    @Override
    public void run() {

    	register();
    	
    	request();
        
    	service();
    	
        acceptClientCommunication();
        
    }

    public PrintWriter getPrintWriter() {
		return printWriter;
	}

	public Socket getSocket() {
		return socket;
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

    public void register() {
    	try {
    		socket = new Socket(routerIP, Integer.parseInt(routerPort, 10));
    		printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("register");
            printWriter.println(InetAddress.getLocalHost().getHostAddress()+":"+port);
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
    
    public void request() {
    	try {
    	TCPCommunicator comm = new TCPCommunicator(routerIP, Integer.parseInt(routerPort));
    	
    	comm.startOrExit();
    	
    	printWriter = comm.getPrintWriter();
    	
    	printWriter.println("request");
    	printWriter.println("x");
    	printWriter.println("2");
        printWriter.println(InetAddress.getLocalHost().getHostAddress()+":"+port);
    	
    	
    	comm.end();
    	
    	new Thread(new ReceiverThread(port)).start();
  
    	}
    	catch( IOException e){
    		
    	}
    }
    public void service(){
    	try {
    	TCPCommunicator comm = new TCPCommunicator(routerIP, Integer.parseInt(routerPort));
    	
    	comm.startOrExit();
    	
    	printWriter = comm.getPrintWriter();
    	
    	bufferedReader = comm.getBufferedReader();
    	
    	printWriter.println("service");
    	
    	String destination = bufferedReader.readLine();
    	String destinationPort = bufferedReader.readLine();
    	
    	String fileName = bufferedReader.readLine();
    	
    	String requestType = bufferedReader.readLine();
    	
    	System.out.println("Server said" + destination + ":"+ destinationPort + ", wants " + fileName + "." + requestType);
    	
    	comm.end();
    	
    	new Thread( new SenderThread(destination, Integer.parseInt(destinationPort))).start();
    	
    	//Send it.
	}
	catch( IOException e){
		
	}
    	
    }
    private void acceptClientCommunication() {
        //TODO Logic for the server actually accepting client connections and doing stuff goes here
    }

//    public static void main(String[] args) throws IOException {
//        ServerClient server = new ServerClient();
//        server.setName("Test Server");
//        server.setPort("5655");
//        server.setRouterIP("192.168.1.108");
//        server.setRouterPort("5555");
//        new Thread(server).start();
//    }
}
