package part2;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class RequestService implements Runnable {

	private ServerClient serverClient;
	private Socket socket;
	private PrintWriter printWriter;
	public RequestService(ServerClient serverClient) {
		this.serverClient = serverClient;
	}
	
	
	@Override
	public void run() {
		try {
    		socket = new Socket(serverClient.getRouterIP(), Integer.parseInt(serverClient.getRouterPort()));
    		printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("register");
            printWriter.println(serverClient.getName());
            printWriter.println(InetAddress.getLocalHost().getHostAddress());
            printWriter.println(serverClient.getPort());
		} catch (UnknownHostException e) {
	        System.err.println("Don't know about router: " + serverClient.getRouterIP() + ":" + serverClient.getRouterPort());
	        System.exit(1);
	    } catch (IOException e) {
	        System.err.println("Couldn't get I/O for the connection to: " + serverClient.getRouterIP() + ":" + serverClient.getRouterPort());
	        System.exit(1);
	    }
	}
	
	
	
	
}
