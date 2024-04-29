import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

public class Client implements Closeable {

    private final String id;
    private final Connector connector;

    public Client(Socket socket) throws IOException {
        id = UUID.randomUUID().toString();
        connector = new Connector(socket);
    }

    public boolean ready() throws IOException {
        return connector.ready();
    }

    public String read() throws IOException {
        return connector.read();
    }

    public void write(String output) throws IOException {
        connector.write(output);
    }

    public String getID() {
        return id;
    }

    @Override
    public void close() {
        connector.close();
    }
}
