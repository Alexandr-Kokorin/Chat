import java.io.*;
import java.net.Socket;

public class Connector implements Closeable {

    private final BufferedReader reader;
    private final BufferedWriter writer;

    public Connector(Socket socket) throws IOException {
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public boolean ready() throws IOException {
        return reader.ready();
    }

    public String read() throws IOException {
        return reader.readLine() + "\n";
    }

    public void write(String str) throws IOException {
        writer.write(str);
        writer.newLine();
        writer.flush();
    }

    @Override
    public void close() {
        try {
            reader.close();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
