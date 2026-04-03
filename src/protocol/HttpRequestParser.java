package protocol;

import model.HttpRequest;
import java.io.IOException;
import java.io.InputStream;

public class HttpRequestParser {

    public static HttpRequest parse(InputStream in) throws IOException {
        HttpRequest request = new HttpRequest();

        String requestLine = readLine(in);
        if (requestLine == null || requestLine.isEmpty()) {
            return null;
        }

        String[] parts = requestLine.split(" ");
        if (parts.length >= 3) {
            request.setMethod(parts[0]);
            request.setUri(parts[1]);
            request.setVersion(parts[2]);
        } else {
            return null;
        }

        String line;
        while ((line = readLine(in)) != null && !line.isEmpty()) {
            String[] headerParts = line.split(":", 2);
            if (headerParts.length == 2) {
                request.addHeader(headerParts[0].trim(), headerParts[1].trim());
            }
        }

        return request;
    }

    public static String extractHost(String uri) {
        if (uri == null) {
            return null;
        }
        
        if (uri.startsWith("http://") || uri.startsWith("https://")) {
            String withoutProtocol = uri.replaceFirst("https?://", "");
            return withoutProtocol.split("/")[0].split(":")[0];
        }
        
        return uri.split(":")[0];
    }

    private static String readLine(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        int c;
        while ((c = in.read()) != -1) {
            if (c == '\r') {
                continue;
            }
            if (c == '\n') {
                break;
            }
            sb.append((char) c);
        }
        if (sb.length() == 0 && c == -1) {
            return null;
        }
        return sb.toString();
    }
}