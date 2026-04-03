package proxy;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader clientIn = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            OutputStream clientOut = clientSocket.getOutputStream();

            String requestLine = clientIn.readLine();
            if (requestLine == null)
                return;

            System.out.println("Request: " + requestLine);
            if (requestLine.startsWith("CONNECT")) {
                handleHttps(clientIn, clientOut);
                return;
            }

            String[] parts = requestLine.split(" ");
            String url = parts[1];

            String host = url.split("/")[2];

            Socket serverSocket = new Socket(host, 80);

            BufferedWriter serverOut = new BufferedWriter(
                    new OutputStreamWriter(serverSocket.getOutputStream()));

            String line;
            while (!(line = clientIn.readLine()).isEmpty()) {
                serverOut.write(line + "\r\n");
            }

            serverOut.write("\r\n");
            serverOut.flush();

            InputStream serverIn = serverSocket.getInputStream();

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = serverIn.read(buffer)) != -1) {
                clientOut.write(buffer, 0, bytesRead);
            }

            clientOut.flush();

            serverSocket.close();
            clientSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleHttps(BufferedReader clientIn, OutputStream clientOut) {
        try {
            System.out.println("Handling HTTPS CONNECT");

            String connectLine = ""; // we already read requestLine before

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}