package part2;

import java.io.*;

public class CSVTools {

    private CSVTools() {

    }

    public static CSVWriter getCSVWriter(String outputFileUri) {
        File file = new File(outputFileUri);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            return new CSVWriter(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class CSVWriter {

        private FileOutputStream fileOutputStream;
        private PrintWriter printWriter;

        protected CSVWriter(FileOutputStream fileOutputStream) {
            this.fileOutputStream = fileOutputStream;
            this.printWriter = new PrintWriter(fileOutputStream);
        }

        public void write(String s) {
            printWriter.write(s);
        }

        public void writeLine(String... values) {
            String line = String.join(",", values);
            printWriter.write(line + "\n");
        }

        public void close() throws IOException {
            fileOutputStream.flush();
            fileOutputStream.close();
        }
    }
}
