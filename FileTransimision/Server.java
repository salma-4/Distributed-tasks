import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        final int PORT_NUMBER = 8081;
        try (ServerSocket serverSocket = new ServerSocket(PORT_NUMBER)) {
            System.out.println("Server started. Waiting for clients...");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket.getInetAddress());

                try {
                    sendFile(socket);
                } catch (IOException e) {
                    System.err.println("Error handling client request: " + e.getMessage());
                } finally {
                    socket.close();
                }
            }
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
        }
    }

    private static void sendFile(Socket socket) throws IOException {
        File file = new File("trans.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             OutputStream outputStream = socket.getOutputStream()) {

            String line;
            while ((line = reader.readLine()) != null) {
                outputStream.write((line + "\n").getBytes());
            }
            System.out.println("File sent successfully.");
        } catch (IOException e) {
            System.err.println("Error sending file: " + e.getMessage());
            throw e;
        }
    }
}
