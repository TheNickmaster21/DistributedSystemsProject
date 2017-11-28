package part2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;


public class ReceiverThread implements Runnable {
    private Boolean active = false;

    private String port;
    public Boolean isActive() {
        return active;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

 

    private void startActionHandler(final Socket socket) {
        new Thread(new ActionHandler(socket)).start();
    }

	public ReceiverThread(String port) {
		this.port = port;
	}//new Thread to handle request. 
	   @Override
	    public void run() {
	        ServerSocket serverSocket = null; // server socket for accepting connections
	        try {
	            serverSocket = new ServerSocket(Integer.parseInt(port)+1);
	            System.out.println("Receiver"+serverSocket.getInetAddress() + " is listening on port: " + serverSocket.getLocalPort());
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

	private class ActionHandler implements Runnable {

        private Socket socket;

        ActionHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
        	DataTransferMethods.receiveFile(socket, "testText.txt");     
        }

       
     
        
    }

    
}
