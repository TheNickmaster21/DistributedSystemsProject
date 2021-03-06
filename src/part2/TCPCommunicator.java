package part2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import part1.ProjectConstants;

public class TCPCommunicator {

    // Variables for setting up connection and communication
	
    private Socket socket = null; // socket to connect with ServerRouter
    
    private PrintWriter printWriter = null; // for writing to ServerRouter
    
    private BufferedReader bufferedReader = null; // for reading form ServerRouter
    
    private String address = null;
    
    private int port =  0;
    public Socket getSocket() {
    	return this.socket;
    }
    public TCPCommunicator(String serverAddress, int port){
    	this.address = serverAddress;
    	this.port = port;
    }
    // Tries to connect to the ServerRouter
    public void startOrExit() {
        try {
            socket = new Socket(address, port);
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about address: " + address);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + address);
            System.exit(1);
        }
    }

    public void end() throws IOException {
        printWriter.close();
        bufferedReader.close();
        socket.close();
    }

    public PrintWriter getPrintWriter() {
        return printWriter;
    }

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }
}
