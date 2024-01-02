import java.io.*;
import java.net.*;

public class FileServer {
    int port = 54321; // Port on which the server listens
    String directoryPath = "./sendfile"; // Path to the directory containing the files
    public FileServer() {
        try (ServerSocket serverSocket = new ServerSocket(port);) {
            System.out.println("Server started. Listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Create input and output streams for client communication
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                // List files in the directory
                File directory = new File(directoryPath);
                File[] files = directory.listFiles();
                for (File file : files) {
                    if (file.isFile()) {
                        out.println(file.getName());
                    }
                }
                out.println("END_OF_LIST"); // Signal the end of the file list

                // Read the selected file name from the client
                String selectedFileName = in.readLine();
                File selectedFile = new File(directoryPath, selectedFileName);
                if (!selectedFile.exists()) {
                    out.println("File not found: " + selectedFileName);
                } else {
                    // Send the selected file to the client
                    System.out.println("Sending " + selectedFileName + " to client");
                    long start = System.currentTimeMillis();
                    FileInputStream fileInputStream = new FileInputStream(selectedFile);
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        clientSocket.getOutputStream().write(buffer, 0, bytesRead);
                    }
                    fileInputStream.close();
                    System.out.println("Time taken in MS--" + (System.currentTimeMillis() - start));
                }
                System.out.println("Client disconnected: " + clientSocket.getInetAddress());
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new FileServer();
    }
}
