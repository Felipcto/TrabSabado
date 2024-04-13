import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
    private final Socket clientSocket;
    private final int clientID;
    private final String nickname;
    private final ArrayList<ClientHandler> clients;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket socket, int clientID, ArrayList<ClientHandler> clients) {
        this.clientSocket = socket;
        this.clientID = clientID;
        this.clients = clients;
        this.nickname = ""; // Inicialmente vazio, definido durante o setup
    }

    @Override
    public void run() {
        try {
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Processa a entrada do cliente
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.startsWith("/quit")) {
                    break;
                }
                // Distribuir mensagens
                for (ClientHandler client : clients) {
                    client.sendMessage(inputLine);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                clientSocket.close();
                clients.remove(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    // Getters e Setters conforme necessário
}
