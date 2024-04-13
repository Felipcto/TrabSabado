import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Scanner scanner;

    public ChatClient(String serverAddress, int serverPort) throws IOException {
        this.socket = new Socket(serverAddress, serverPort);
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.print("Enter your nickname: ");
        String nickname = scanner.nextLine();
        out.println(nickname); // Enviar nickname para o servidor

        // Iniciar thread para receber mensagens do servidor
        new Thread(this::receiveMessages).start();

        // Menu de opções para o usuário
        while (true) {
            System.out.println("\nOptions:");
            System.out.println("1. View list of connected clients");
            System.out.println("2. Send message to a specific client");
            System.out.println("3. Send message to all clients");
            System.out.println("4. Quit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consumir a nova linha

            switch (choice) {
                case 1:
                    out.println("/list");
                    break;
                case 2:
                    System.out.print("Enter client ID and message: ");
                    String clientAndMessage = scanner.nextLine();
                    out.println("/msg " + clientAndMessage);
                    break;
                case 3:
                    System.out.print("Enter message: ");
                    String message = scanner.nextLine();
                    out.println("/all " + message);
                    break;
                case 4:
                    out.println("/quit");
                    return; // Sair do loop e encerrar a aplicação
            }
        }
    }

    private void receiveMessages() {
        try {
            String fromServer;
            while ((fromServer = in.readLine()) != null) {
                System.out.println("Server: " + fromServer);
            }
        } catch (IOException e) {
            System.out.println("Connection to server lost.");
        }
    }

    public static void main(String[] args) {
        try {
            ChatClient client = new ChatClient("localhost", 1234);
            client.start();
        } catch (IOException e) {
            System.out.println("Unable to connect to the server.");
            e.printStackTrace();
        }
    }
}
