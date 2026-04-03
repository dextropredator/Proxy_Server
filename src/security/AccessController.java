package security;

import java.util.HashSet;
import java.util.Set;

public class AccessController {

    private static final Set<String> BLOCKED_DOMAINS = new HashSet<>();

    static {
        BLOCKED_DOMAINS.add("facebook.com");
        BLOCKED_DOMAINS.add("instagram.com");
        BLOCKED_DOMAINS.add("tiktok.com");
    }

    public static boolean isBlocked(String host) {
        if (host == null || host.isEmpty()) {
            return false;
        }
        
        String lowerHost = host.toLowerCase();

        for (String blockedDomain : BLOCKED_DOMAINS) {
            if (lowerHost.equals(blockedDomain) || lowerHost.endsWith("." + blockedDomain)) {
                return true;
            }
        }

        return false;
    }

    public static void blockDomain(String domain) {
        if (domain != null && !domain.isEmpty()) {
            BLOCKED_DOMAINS.add(domain.toLowerCase());
        }
    }

    public static void unblockDomain(String domain) {
        if (domain != null) {
            BLOCKED_DOMAINS.remove(domain.toLowerCase());
        }
    }
}