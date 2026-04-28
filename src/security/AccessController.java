package security;
import java.util.HashSet;
import java.util.Set;
public class AccessController {
    private static final Set<String> BLOCKED_DOMAINS = new HashSet<>();
    static {
        
        BLOCKED_DOMAINS.add("facebook.com");
        BLOCKED_DOMAINS.add("instagram.com");
        BLOCKED_DOMAINS.add("github.com");
        BLOCKED_DOMAINS.add("reddit.com");
    }
    public static boolean isBlocked(String host) {
        if (host == null || host.isEmpty()) {
            return false;
        }
        host = host.toLowerCase().trim();
        for (String blockedDomain : BLOCKED_DOMAINS) {
            if (host.equals(blockedDomain) || host.endsWith("." + blockedDomain)) {
                return true;
            }
        }
        return false;
    }
}