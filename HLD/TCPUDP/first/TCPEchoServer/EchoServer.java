package HLD.TCPUDP.first.TCPEchoServer;

// EchoServer.java
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class EchoServer {
    public static void main(String[] args) throws IOException {
        int port = 12345;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Echo server is running on port " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept(); // Accept client connection
            System.out.println("Client connected: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());

            // Handle each client in a new thread
            new Thread(() -> handleClient(clientSocket)).start();
        }
    }

    private static void handleClient(Socket socket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("Received: " + line + " from " + socket.getInetAddress());
                out.write("Echo: " + line + "\n");
                out.flush();
            }
        } catch (IOException e) {
            System.err.println("Client disconnected: " + socket.getInetAddress());
        } finally {
            try {
                socket.close();
            } catch (IOException ignore) {}
        }
    }
}
