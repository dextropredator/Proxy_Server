 package network;

// import java.io.IOException;
// import java.net.InetSocketAddress;
// import java.net.Socket;

// public class ServerConnector {

//     private static final int CONNECT_TIMEOUT_MS = 10000; 
//     private static final int READ_TIMEOUT_MS = 30000; 
//     public Socket connect(String host, int port) throws IOException {
//     // This creates the socket AND connects it in one single step.
//     // Do NOT call socket.connect() after this line.
//     return new Socket(host, port); 
// }   

// //     public static Socket connect(String host, int port) throws IOException {
// //         //Socket socket = new Socket();
// //         // It MUST point to 8080 now because that is your Domain Server

// //         //socket.connect(new InetSocketAddress(host, port), CONNECT_TIMEOUT_MS);
// //         Socket socket = new Socket("127.0.0.1", 8080); // This ALREADY connects the socket
// // socket.connect(new InetSocketAddress("127.0.0.1", 8080)); // ERROR: You can't connect twice!
        
// //         socket.setSoTimeout(READ_TIMEOUT_MS);
        
// //         return socket;
// //     }
// }package network;



import java.io.IOException;
import java.net.Socket;

public class ServerConnector {
    private static final int READ_TIMEOUT_MS = 30000; 

    // ADDED 'static' BACK HERE!
    public static Socket connect(String host, int port) throws IOException {
        Socket socket;
        
        // SMART ROUTING: Intercept your custom domains
        if (host != null && host.contains("test.com")) {
            // Reroute to your local Domain Server on 8080
            socket = new Socket("127.0.0.1", 8080);
        } else {
            // Let normal internet traffic pass through normally
            socket = new Socket(host, port);
        }
        
        socket.setSoTimeout(READ_TIMEOUT_MS);
        return socket;
    }
}