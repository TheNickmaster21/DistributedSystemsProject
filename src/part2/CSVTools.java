package part2;

import com.sun.istack.internal.NotNull;

import java.io.*;

public class CSVTools {

    private CSVTools() {

    }

    public static CSVWriter getCSVWriter(String outputFileUri) throws IOException {
        File file = new File(outputFileUri);
        if (!file.exists()) {
            file.createNewFile();
        }
        return new CSVWriter(new FileOutputStream(file));
    }

    public static CSVWriter getCSVWriterIfFileIsNew(String outputFileUri) throws IOException {
        File file = new File(outputFileUri);
        if (!file.exists()) {
            file.createNewFile();
            return new CSVWriter(new FileOutputStream(file));
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
            printWriter.flush();
            printWriter.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        }
    }
}
