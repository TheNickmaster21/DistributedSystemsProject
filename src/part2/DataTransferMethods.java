package part2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class DataTransferMethods {

    public static final int BUFFER_SIZE = 16;

    public static void sendFile(Socket socket, String fileName) {
        try {
            InputStream inputStream = new FileInputStream(fileName);
            OutputStream outputStream = socket.getOutputStream();

            byte[] buffer = new byte[BUFFER_SIZE];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
                outputStream.flush();
            }

            System.out.println("Sender sent File. Closing socket.");
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            System.out.println("Server failed to read and send file!");
            System.out.println(e.toString());
        }
    }

    public static void receiveFile(Socket socket, String fileName) {
        try {
            CSVTools.CSVWriter csvWriter = CSVTools.getCSVWriter("LOG_" + fileName + ".csv");

            File file = new File("RECEIVED_" + fileName);
            Boolean writingNewFile = false;
            if (!file.exists()) {
                writingNewFile = file.createNewFile();
            }

            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = new FileOutputStream(file, true);

            StopWatch stopWatch = new StopWatch();

            byte[] buffer = new byte[BUFFER_SIZE];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                csvWriter.writeLine(String.valueOf(BUFFER_SIZE), String.valueOf(stopWatch.restartAndGetDifferance()));
                if (writingNewFile) {
                    outputStream.write(buffer, 0, length);
                    outputStream.flush();
                }
            }
            System.out.println("Receiver received File. Closing socket.");
            csvWriter.close();

            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            System.out.println("Client failed to receive and save file!");
            System.out.println(e.toString());
        }
    }

    //Test Region

    public static void main(String[] args) throws IOException {
        int port = 3434;
        String fileName = "testText.txt";

        new Thread(() -> {
            try {
                ServerSocket ss = new ServerSocket(port);
                Socket socket = ss.accept();
                sendFile(socket, fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        Socket socket = new Socket("localhost", port);
        receiveFile(socket, fileName);
    }


}
