package part2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class DataTransferMethods {

    public static final int BUFFER_SIZE = 32;

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
            OutputStream outputStream = new FileOutputStream(file);

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

            csvWriter.close();

            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            System.out.println("Client failed to receive and save file!");
            System.out.println(e.toString());
        }
    }

    //Test Region

    static void server() throws IOException {
        ServerSocket ss = new ServerSocket(3434);
        Socket socket = ss.accept();
        sendFile(socket, "Test Image.jpg");
    }

    static void client() throws IOException {
        Socket socket = new Socket("localhost", 3434);
        receiveFile(socket, "Test Image.jpg");
    }

    public static void main(String[] args) throws IOException {
        new Thread(() -> {
            try {
                server();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        client();
    }

}
