package network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ServerConnector {

    private static final int CONNECT_TIMEOUT_MS = 10000; 
    private static final int READ_TIMEOUT_MS = 30000;    

    public static Socket connect(String host, int port) throws IOException {
        Socket socket = new Socket();
        
        socket.connect(new InetSocketAddress(host, port), CONNECT_TIMEOUT_MS);
        
        socket.setSoTimeout(READ_TIMEOUT_MS);
        
        return socket;
    }
}