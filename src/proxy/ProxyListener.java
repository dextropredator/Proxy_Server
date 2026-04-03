package proxy;

import utils.Logger;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ProxyListener {

    private int port;

    public ProxyListener(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Logger.info("Proxy Server is running and listening on port: " + port);

            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                
                ClientHandler handler = new ClientHandler(clientSocket);
                
               
                new Thread(handler).start();
            }
        } catch (IOException e) {
            Logger.error("Failed to start server on port " + port, e);
        }
    }
}