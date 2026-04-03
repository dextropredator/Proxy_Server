package utils;

public class Constants {
    
    public static final int DEFAULT_PORT = 8080;
    
    public static final int BUFFER_SIZE = 4096;
    
    public static final int CONNECT_TIMEOUT_MS = 10000;
    public static final int READ_TIMEOUT_MS = 30000;
    
    public static final int MAX_CACHE_SIZE = 100;
    
    public static final String CRLF = "\r\n";
    
    public static final String HTTP_200_CONNECTION_ESTABLISHED = "HTTP/1.1 200 Connection Established\r\n\r\n";
    
    public static final String HTTP_403_FORBIDDEN = "HTTP/1.1 403 Forbidden\r\nContent-Type: text/html\r\n\r\n<h1>403 Forbidden</h1><p>Website Blocked by Proxy.</p>";
}