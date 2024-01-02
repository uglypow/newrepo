import java.io.*;
import java.net.*;

public class FileClient {
    String serverAddress = "127.0.0.1"; // Server IP address
    int serverPort = 54321; // Server port
    String downloadDirectory = "./download"; // Directory to save downloaded files

    public FileClient() {
        try {
            Socket socket = new Socket(serverAddress, serverPort);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Request the file list
            System.out.println("File list:");
            String fileName;
            while (!(fileName = in.readLine()).equals("END_OF_LIST")) {
                System.out.println(fileName);
            }

            // Request a specific file
            System.out.print("Enter the name of the file you want to download: ");
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            String selectedFileName = userInput.readLine();
            out.println(selectedFileName);

            // Get response from server if the requested file exist
            String response = in.readLine();
            if (response.startsWith("File not found")) {
                System.out.println("File not found on the server.");
            } else {
                // Receive the file content
                long start = System.currentTimeMillis();
                InputStream fileInputStream = socket.getInputStream();
                File downloadedFile = new File(downloadDirectory, selectedFileName);
                FileOutputStream fileOutputStream = new FileOutputStream(downloadedFile);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }
                System.out.println("time taken in MS--" + (System.currentTimeMillis() - start));
                fileOutputStream.close();
                System.out.println("File downloaded successfully.");
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new FileClient();
    }
}
