package app;
import proxy.ProxyListener;
public class ProxyServer{
    public static void main(String args[]){
        try{
            new ProxyListener(8080).start();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}