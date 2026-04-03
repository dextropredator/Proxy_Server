package security;

import java.util.HashSet;
import java.util.Set;

public class AccessController {
    
    // We use a HashSet for super fast lookups
    private static final Set<String> BLOCKED_DOMAINS = new HashSet<>();

    static {
        // Add your domains here!
        BLOCKED_DOMAINS.add("facebook.com");
        BLOCKED_DOMAINS.add("instagram.com");
        BLOCKED_DOMAINS.add("github.com");
    }

    public static boolean isBlocked(String host) {
        if (host == null || host.isEmpty()) {
            return false;
        }
        
        // Convert to lowercase just in case the browser sends "FaceBook.com"
        host = host.toLowerCase().trim();

        for (String blockedDomain : BLOCKED_DOMAINS) {
            // This checks for EXACT matches (facebook.com) OR subdomains (www.facebook.com)
            if (host.equals(blockedDomain) || host.endsWith("." + blockedDomain)) {
                return true;
            }
        }
        
        return false;
    }
}