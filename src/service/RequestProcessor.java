package service;

import security.AccessController;
import java.io.*;
import java.net.Socket;

public class RequestProcessor {

    public void process(Socket clientSocket) {
        try {
            InputStream clientInRaw = clientSocket.getInputStream();
            OutputStream clientOutRaw = clientSocket.getOutputStream();

            String requestLine = readLine(clientInRaw);
            if (requestLine == null || requestLine.isEmpty()) {
                clientSocket.close();
                return;
            }

            System.out.println("Processing: " + requestLine);

            String[] parts = requestLine.split(" ");
            if (parts.length < 2) {
                clientSocket.close();
                return;
            }

            String method = parts[0];
            String urlOrHost = parts[1];

            if (method.equals("CONNECT")) {
                handleHttps(requestLine, urlOrHost, clientInRaw, clientOutRaw, clientSocket);
            } else {
                handleHttp(requestLine, urlOrHost, clientInRaw, clientOutRaw, clientSocket);
            }

        } catch (Exception e) {
            System.err.println("Processor Error: " + e.getMessage());
        }
    }

    private void handleHttp(String requestLine, String url, InputStream clientIn, OutputStream clientOut, Socket clientSocket) throws Exception {
        String host = url.replace("http://", "").split("/")[0];

        if (AccessController.isBlocked(host)) {
            sendForbidden(clientOut);
            clientSocket.close();
            return;
        }

        Socket serverSocket = new Socket(host, 80);
        BufferedWriter serverOut = new BufferedWriter(new OutputStreamWriter(serverSocket.getOutputStream()));
        
        serverOut.write(requestLine + "\r\n");

        String line;
        while (!(line = readLine(clientIn)).isEmpty()) {
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
    }

    private void handleHttps(String requestLine, String hostPort, InputStream clientIn, OutputStream clientOut, Socket clientSocket) throws Exception {
        String[] split = hostPort.split(":");
        String host = split[0];
        int port = (split.length > 1) ? Integer.parseInt(split[1]) : 443;

        if (AccessController.isBlocked(host)) {
            clientSocket.close(); 
            return;
        }

        String line;
        while (!(line = readLine(clientIn)).isEmpty()) {
        }

        Socket serverSocket = new Socket(host, port);
        
        String connectionEstablished = "HTTP/1.1 200 Connection Established\r\n\r\n";
        clientOut.write(connectionEstablished.getBytes());
        clientOut.flush();

        Thread clientToServer = new Thread(new network.StreamForwarder(clientIn, serverSocket.getOutputStream()));
        Thread serverToClient = new Thread(new network.StreamForwarder(serverSocket.getInputStream(), clientOut));

        clientToServer.start();
        serverToClient.start();
    }

    private void sendForbidden(OutputStream out) throws IOException {
        String response = "HTTP/1.1 403 Forbidden\r\nContent-Type: text/html\r\n\r\n" +
                          "<h1>403 Forbidden</h1><p>Website Blocked by Proxy Access Controller.</p>";
        out.write(response.getBytes());
        out.flush();
    }

    private String readLine(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        int c;
        while ((c = in.read()) != -1) {
            if (c == '\r') continue;
            if (c == '\n') break;
            sb.append((char) c);
        }
        if (sb.length() == 0 && c == -1) return null;
        return sb.toString();
    }
}