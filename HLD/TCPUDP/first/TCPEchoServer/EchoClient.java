package HLD.TCPUDP.first.TCPEchoServer;

// EchoClient.java
import java.io.*;
import java.net.Socket;

public class EchoClient {
    public static void main(String[] args) throws IOException {
        String serverIp = "localhost";
        int port = 12345;

        try (
            Socket socket = new Socket(serverIp, port);
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            System.out.println("Connected to server. Type messages:");

            String line;
            while ((line = console.readLine()) != null) {
                out.write(line + "\n");
                out.flush();
                String response = in.readLine();
                System.out.println("Server says: " + response);
            }
        }
    }
}
