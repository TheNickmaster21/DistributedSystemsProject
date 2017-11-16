package part1;

import java.io.*;

public class TCPServer {

    public static void main(String[] args) throws IOException {
        TCPCommunicator tcpCommunicator = new TCPCommunicator();

        // Tries to connect to the ServerRouter
        tcpCommunicator.startOrExit();

        PrintWriter out = tcpCommunicator.getPrintWriter();
        BufferedReader in = tcpCommunicator.getBufferedReader();

        // Variables for message passing
        String fromServer; // messages sent to ServerRouter
        String fromClient; // messages received from ServerRouter

        // Communication process (initial sends/receives)
        out.println(ProjectConstants.CLIENT_ADDRESS);// initial send (IP of the destination Client)
        fromClient = in.readLine();// initial receive from router (verification of connection)
        System.out.println("ServerRouter: " + fromClient);

        // Communication while loop
        while ((fromClient = in.readLine()) != null) {
            System.out.println("Client said: " + fromClient);
            if (fromClient.equals(ProjectConstants.EXIT_MESSAGE)) // exit statement
                break;
            fromServer = fromClient.toUpperCase(); // converting received message to upper case
            System.out.println("Server said: " + fromServer);
            out.println(fromServer); // sending the converted message back to the Client via ServerRouter
        }

        // closing connections
        tcpCommunicator.end();
    }
}
