package HLD.TCPUDP.first.TCPFileTransferApp;

// FileClient.java
import java.io.*;
import java.net.*;

public class FileClient {
    public static void main(String[] args) throws IOException {
        String serverIp = "localhost";
        int port = 5000;
        File file = new File("sample.txt");

        try (
            Socket socket = new Socket(serverIp, port);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            FileInputStream fis = new FileInputStream(file)
        ) {
            dos.writeUTF(file.getName());           // Send file name
            dos.writeLong(file.length());           // Send file size

            byte[] buffer = new byte[4096];
            int read;

            while ((read = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, read);
            }

            System.out.println("File sent: " + file.getName());
        }
    }
}
