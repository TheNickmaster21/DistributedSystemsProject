import java.io.*;
import java.net.*;

public class TCPClient {

    public static void main(String[] args) throws IOException {
        TCPCommunicator tcpCommunicator = new TCPCommunicator();

        // Tries to connect to the ServerRouter
        tcpCommunicator.startOrExit();

        PrintWriter out = tcpCommunicator.getPrintWriter();
        BufferedReader in = tcpCommunicator.getBufferedReader();

        // Variables for message passing
        Reader reader = new FileReader("file.txt");
        BufferedReader fromFile = new BufferedReader(reader); // reader for the string file
        String fromServer; // messages received from ServerRouter
        String fromUser; // messages sent to ServerRouter
        String address = "10.5.2.109"; // destination IP (Server)
        long t0, t1, t;

        // Communication process (initial sends/receives
        out.println(address);// initial send (IP of the destination Server)
        fromServer = in.readLine();//initial receive from router (verification of connection)
        System.out.println("ServerRouter: " + fromServer);

        String host = InetAddress.getLocalHost().getHostAddress(); // Client machine's IP
        out.println(host); // Client sends the IP of its machine as initial send

        t0 = System.currentTimeMillis();

        // Communication while loop
        while ((fromServer = in.readLine()) != null) {
            System.out.println("Server: " + fromServer);
            t1 = System.currentTimeMillis();
            if (fromServer.equals("Bye.")) // exit statement
                break;
            t = t1 - t0;
            System.out.println("Cycle time: " + t);

            fromUser = fromFile.readLine(); // reading strings from a file
            if (fromUser != null) {
                System.out.println("Client: " + fromUser);
                out.println(fromUser); // sending the strings to the Server via ServerRouter
                t0 = System.currentTimeMillis();
            }
        }

        // closing connections
        tcpCommunicator.end();
    }
}
