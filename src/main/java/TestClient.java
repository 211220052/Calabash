import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class TestClient {

    public void startClient(String clientName) throws IOException {
        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 9093);
        SocketChannel client = SocketChannel.open(hostAddress);
        System.out.println(clientName + " started");

        new Thread(() -> {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            try {
                while (client.isOpen()) {
                    buffer.clear();
                    int bytesRead = client.read(buffer);
                    if (bytesRead > 0) {
                        buffer.flip();
                        byte[] data = new byte[bytesRead];
                        buffer.get(data);
                        System.out.println(clientName + " received: " + new String(data));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String message = scanner.nextLine();
            if (message.equalsIgnoreCase("quit")) {
                break;
            }
            ByteBuffer buffer = ByteBuffer.wrap((clientName + ": " + message).getBytes());
            client.write(buffer);
        }
        client.close();
        scanner.close();
    }

    public static void main(String[] args) {
        Runnable clientA = () -> {
            try {
                new TestClient().startClient("Client-A");
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        Runnable clientB = () -> {
            try {
                new TestClient().startClient("Client-B");
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        new Thread(clientA).start();
        new Thread(clientB).start();
    }
}