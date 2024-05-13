import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        final String Host = "127.0.0.1";
        final int PORT_NUMBER = 8081;

        try (Socket socket = new Socket(Host, PORT_NUMBER);
             InputStream inputStream = socket.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String readLine;
            while ((readLine = reader.readLine()) != null) {
                System.out.println(readLine);
            }
            System.out.println("File received successfully!");
        } catch (IOException e) {
            System.err.println("Error: Failed to establish connection or read data from the server.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error: An unexpected error occurred.");
            e.printStackTrace();
        }
    }
}
