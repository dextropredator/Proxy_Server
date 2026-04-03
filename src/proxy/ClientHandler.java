package proxy;

import service.RequestProcessor;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private RequestProcessor processor;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        this.processor = new RequestProcessor();
    }

    @Override
    public void run() {
        processor.process(clientSocket);
    }
}