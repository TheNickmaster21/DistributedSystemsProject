package part2;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServiceRequest implements Runnable {

	private ServerClient serverClient;
	
	public ServiceRequest(ServerClient serverClient) {
		this.serverClient = serverClient;
	}
	
	
	@Override
	public void run() {
			System.out.println("Serviced a Request");
	}
	
	
	
	
}
