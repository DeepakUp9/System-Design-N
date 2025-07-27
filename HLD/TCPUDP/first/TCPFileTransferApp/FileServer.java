package HLD.TCPUDP.first.TCPFileTransferApp;

// FileServer.java
import java.io.*;
import java.net.*;

public class FileServer {
    public static void main(String[] args) throws IOException {
        int port = 5000;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("File server listening on port " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress());
            new Thread(() -> receiveFile(clientSocket)).start();
        }
    }

    private static void receiveFile(Socket socket) {
        try (
            DataInputStream dis = new DataInputStream(socket.getInputStream())
        ) {
            String fileName = dis.readUTF(); // Read file name
            long fileSize = dis.readLong();  // Read file size
            File file = new File("received_" + fileName);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                byte[] buffer = new byte[4096];
                int read;
                long totalRead = 0;

                while (totalRead < fileSize && (read = dis.read(buffer, 0, (int)Math.min(buffer.length, fileSize - totalRead))) != -1) {
                    fos.write(buffer, 0, read);
                    totalRead += read;
                }
                System.out.println("File received: " + file.getName());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { socket.close(); } catch (IOException ignore) {}
        }
    }
}
