package bg.sofia.uni.fmi.mjt.poll.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8080; // Същият порт като сървъра
    private static final int BUFFER_SIZE = 2000;

    private static ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

    public static void main(String[] args) {
        Client client = new Client();
        System.out.println("Starting Client...");
        client.start();
    }

    public void start() {
        try (SocketChannel socketChannel = SocketChannel.open();
             Scanner scanner = new Scanner(System.in)) {

            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));

            System.out.println("Connected to the server.");

            while (true) {
                System.out.print("Enter message: ");
                String message = scanner.nextLine();

                if ("disconnect".equalsIgnoreCase(message)) {
                    sendToServer(socketChannel, message, buffer);
                    System.out.println("Disconnected from the server.");
                    break;
                }

                System.out.println("Sending message <" + message + "> to the server...");

                sendToServer(socketChannel, message, buffer);
                String reply = receiveFromServer(socketChannel, buffer);

                System.out.println("The server replied <" + reply + ">");
            }

        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the network communication", e);
        }
    }

    private static void sendToServer(SocketChannel socketChannel, String command, ByteBuffer buffer) throws IOException {
        buffer.clear();
        buffer.put(command.getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        socketChannel.write(buffer);
    }

    private static String receiveFromServer(SocketChannel socketChannel, ByteBuffer buffer) throws IOException {
        buffer.clear(); // switch to writing mode
        socketChannel.read(buffer); // buffer fill
        buffer.flip(); // switch to reading mode

        byte[] byteArray = new byte[buffer.remaining()];
        buffer.get(byteArray); // buffer drain
        return new String(byteArray, StandardCharsets.UTF_8);
    }
}