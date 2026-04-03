package app;

import proxy.ProxyListener;
import utils.Constants;
import utils.Logger;

public class ProxyServer {
    public static void main(String args[]) {
        try {
            Logger.info("Initializing Proxy Server...");
          
            new ProxyListener(Constants.DEFAULT_PORT).start();

        } catch (Exception e) {
            Logger.error("A critical error occurred while starting the proxy server.", e);
        }
    }
}