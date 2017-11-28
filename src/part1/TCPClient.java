package part1;

import java.io.*;
import java.net.*;

import part2.TCPCommunicator;

public class TCPClient {

    private static final Boolean DEBUG_OUTPUT = true;

    public static void main(String[] args) throws IOException {
        TCPCommunicator tcpCommunicator = new TCPCommunicator();

        // Tries to connect to the ServerRouter
        tcpCommunicator.startOrExit();

        PrintWriter out = tcpCommunicator.getPrintWriter();
        BufferedReader in = tcpCommunicator.getBufferedReader();

        // Variables for message passing
        Reader reader = new FileReader(ProjectConstants.INPUT_TEXT_FILE_NAME);
        BufferedReader fromFile = new BufferedReader(reader); // reader for the string file
        String fromServer; // messages received from ServerRouter
        String fromUser; // messages sent to ServerRouter
        long t0, t1, t;

        // Communication process (initial sends/receives
        out.println(ProjectConstants.SERVER_ADDRESS);// initial send (IP of the destination Server)
        fromServer = in.readLine();//initial receive from router (verification of connection)
        println("ServerRouter: " + fromServer);

        String host = InetAddress.getLocalHost().getHostAddress(); // Client machine's IP
        out.println(host); // Client sends the IP of its machine as initial send

        t0 = System.nanoTime();

        // Communication while loop
        while ((fromServer = in.readLine()) != null) {
            println("Server: " + fromServer);
            t1 = System.nanoTime();
            if (fromServer.equals(ProjectConstants.EXIT_MESSAGE)) // exit statement
                break;
            t = t1 - t0;
            System.out.println("Cycle time: " + t);

            fromUser = fromFile.readLine(); // reading strings from a file
            if (fromUser != null) {
                println("Client: " + fromUser);
                out.println(fromUser); // sending the strings to the Server via ServerRouter
                t0 = System.nanoTime();
            }
        }

        // closing connections
        tcpCommunicator.end();
    }

    public static void println(final String s) {
        if (DEBUG_OUTPUT) {
            System.out.println(s);
        }
    }
}
