package service;

import security.AccessController;
import network.ServerConnector;
import network.StreamForwarder;
import utils.Logger;
import utils.Constants;

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

            Logger.info("Processing: " + requestLine);

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
            Logger.error("Processor Error", e);
        }
    }

    private void handleHttp(String requestLine, String url, InputStream clientIn, OutputStream clientOut, Socket clientSocket) throws Exception {
        String host = url.replace("http://", "").split("/")[0].trim();

     
        if (host.contains(":")) {
            host = host.split(":")[0];
        }
     

        if (AccessController.isBlocked(host)) {
            Logger.warn("BLOCKED HTTP access to: " + host);
            sendForbidden(clientOut);
            clientSocket.close();
            return;
        }

        Socket serverSocket = ServerConnector.connect(host, 80);
        BufferedWriter serverOut = new BufferedWriter(new OutputStreamWriter(serverSocket.getOutputStream()));
        
        serverOut.write(requestLine + "\r\n");

        String line;
        while ((line = readLine(clientIn)) != null && !line.isEmpty()) {
            if (line.toLowerCase().startsWith("proxy-connection:") || line.toLowerCase().startsWith("connection:")) {
                continue;
            }
            serverOut.write(line + "\r\n");
        }
        
        serverOut.write("Connection: close\r\n");
        serverOut.write("\r\n");
        serverOut.flush();

        InputStream serverIn = serverSocket.getInputStream();
        byte[] buffer = new byte[Constants.BUFFER_SIZE];
        int bytesRead;
        
        while ((bytesRead = serverIn.read(buffer)) != -1) {
            clientOut.write(buffer, 0, bytesRead);
            clientOut.flush(); 
        }

        serverSocket.close();
        clientSocket.close();
    }

    private void handleHttps(String requestLine, String hostPort, InputStream clientIn, OutputStream clientOut, Socket clientSocket) throws Exception {
        String[] split = hostPort.split(":");
        String host = split[0].trim();
        int port = (split.length > 1) ? Integer.parseInt(split[1].trim()) : 443;

        if (AccessController.isBlocked(host)) {
            Logger.warn("BLOCKED HTTPS access to: " + host);
            sendForbidden(clientOut);
            clientSocket.close(); 
            return;
        }

        String line;
        while ((line = readLine(clientIn)) != null && !line.isEmpty()) {
        }

        Socket serverSocket = ServerConnector.connect(host, port);
        
        clientOut.write(Constants.HTTP_200_CONNECTION_ESTABLISHED.getBytes());
        clientOut.flush();

        Thread clientToServer = new Thread(new StreamForwarder(clientIn, serverSocket.getOutputStream()));
        Thread serverToClient = new Thread(new StreamForwarder(serverSocket.getInputStream(), clientOut));

        clientToServer.start();
        serverToClient.start();
    }

    private void sendForbidden(OutputStream out) throws IOException {
        out.write(Constants.HTTP_403_FORBIDDEN.getBytes());
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