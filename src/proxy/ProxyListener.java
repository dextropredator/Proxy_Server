package proxy;
import java.net.ServerSocket;
import java.net.Socket;
public class ProxyListener {
    private int port;
    public ProxyListener (int port){
        this.port = port;
    }
    public void start() throws Exception {
        ServerSocket Listener = new ServerSocket(port);
        System.out.println("Proxy Running on port :"+port);
        while(true){
            Socket client =  Listener.accept();
            new Thread(new ClientHandler(client)).start();
        }
    }
    
}
