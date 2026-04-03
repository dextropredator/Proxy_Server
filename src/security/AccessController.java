package security;

public class AccessController {

    public static boolean isBlocked(String host) {
        return host.contains("facebook.com");
    }
}