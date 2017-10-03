import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketWrapper {

    // Variables for setting up connection and communication
    private Socket socket = null; // socket to connect with ServerRouter
    private InputStream inputStream = null;
    private OutputStream outputStream = null;

    // Tries to connect to the ServerRouter
    public void startOrExit() {
        try {
            socket = new Socket(ProjectConstants.ROUTER_ADDRESS, ProjectConstants.PORT);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about router: " + ProjectConstants.ROUTER_ADDRESS);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + ProjectConstants.ROUTER_ADDRESS);
            System.exit(1);
        }
    }

    public void end() throws IOException {
        inputStream.close();
        outputStream.close();
        socket.close();
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }
}
