import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Connector connector;

    public void start() {
        try {
            Socket socket = new Socket(Config.HOST, Config.PORT);
            success(socket);
        } catch (IOException e) {
            retry();
        }
    }

    private void success(Socket socket) throws IOException {
        System.out.println("Соединение с сервером успешно установлено.");
        connector = new Connector(socket);
        new Thread(this::read).start();
        new Thread(this::write).start();
    }

    private void retry() {
        System.out.println("Ошибка соединения с сервером.");
        try {
            Thread.sleep(10_000);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        start();
    }

    private void read() {
        try {
            readMessage();
        } catch (IOException e) {
            System.out.println("Потерено соединение с сервером.");
            connector.close();
            start();
        }
    }

    private void readMessage() throws IOException {
        while (true) {
            System.out.print(connector.read());
        }
    }

    private void write() {
        try {
            writeMessage();
        } catch (IOException e) {
            System.out.println("Потерено соединение с сервером, сообщение не было отправлено.");
            connector.close();
            start();
        }
    }

    private void writeMessage() throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            System.out.println();
            connector.write(input);
        }
    }
}
