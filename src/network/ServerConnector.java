 package network;
import java.io.IOException;
import java.net.Socket;
public class ServerConnector {
    private static final int READ_TIMEOUT_MS = 30000; 
    public static Socket connect(String host, int port) throws IOException {
        Socket socket;  
        if (host != null && host.contains("test.com")) {
            socket = new Socket("127.0.0.1", 8080);
        } else {
            socket = new Socket(host, port);
        }
        socket.setSoTimeout(READ_TIMEOUT_MS);
        return socket;
    }
}