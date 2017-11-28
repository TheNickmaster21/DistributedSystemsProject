package part1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPCommunicator {

    // Variables for setting up connection and communication
    private Socket socket = null; // socket to connect with ServerRouter
    private PrintWriter printWriter = null; // for writing to ServerRouter
    private BufferedReader bufferedReader = null; // for reading form ServerRouter

    // Tries to connect to the ServerRouter
    public void startOrExit() {
        try {
            socket = new Socket(ProjectConstants.ROUTER_ADDRESS, ProjectConstants.PORT);
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about router: " + ProjectConstants.ROUTER_ADDRESS);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + ProjectConstants.ROUTER_ADDRESS);
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
