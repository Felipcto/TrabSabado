import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ChatServer {
    private static final int PORT = 1234;
    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    private static AtomicInteger clientIdGenerator = new AtomicInteger();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Servidor iniciado na porta " + PORT);

        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                int clientId = clientIdGenerator.getAndIncrement();
                ClientHandler clientThread = new ClientHandler(clientSocket, clientId, clients);
                clients.add(clientThread);
                clientThread.start();
                System.out.println("Novo cliente conectado: " + clientId);
            }
        } finally {
            serverSocket.close();
        }
    }
}
