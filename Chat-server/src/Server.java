import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private volatile List<Client> clients = new ArrayList<>();

    public void start() {
        new Thread(this::doWork).start();
        try (ServerSocket serverSocket = new ServerSocket(Config.PORT)) {
            success(serverSocket);
        } catch (IOException e) {
            System.out.println("Ошибка сервера.");
        }
    }

    private void success(ServerSocket serverSocket) throws IOException {
        System.out.println("Сервер успешно запущен.");
        while (true) {
            Socket clientSocket = serverSocket.accept();
            Client client = new Client(clientSocket);
            clients.add(client);
        }
    }

    private void doWork() {
        while (true) {
            read();
        }
    }

    private void read() {
        for (int i = 0; i < clients.size(); i++) {
            try {
                if (clients.get(i).ready()) {
                    write(clients.get(i).read(), clients.get(i).getID());
                }
            } catch (IOException e) {
                clients.get(i).close();
                clients.remove(i);
                i--;
            }
        }
    }

    private void write(String input, String id) {
        for (int i = 0; i < clients.size(); i++) {
            try {
                if (!clients.get(i).getID().equals(id)) {
                    clients.get(i).write(input);
                }
            } catch (IOException e) {
                clients.get(i).close();
                clients.remove(i);
                i--;
            }
        }
    }
}
