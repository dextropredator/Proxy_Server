package protocol;

import model.HttpResponse;
import java.io.IOException;
import java.io.InputStream;

public class HttpResponseParser {

    public static HttpResponse parse(InputStream in) throws IOException {
        HttpResponse response = new HttpResponse();

        String statusLine = readLine(in);
        if (statusLine == null || statusLine.isEmpty()) {
            return null;
        }

        String[] parts = statusLine.split(" ", 3);
        if (parts.length >= 2) {
            response.setVersion(parts[0]);
            try {
                response.setStatusCode(Integer.parseInt(parts[1]));
            } catch (NumberFormatException e) {
                response.setStatusCode(500);
            }
            if (parts.length == 3) {
                response.setStatusMessage(parts[2]);
            } else {
                response.setStatusMessage("");
            }
        } else {
            return null;
        }

        String line;
        while ((line = readLine(in)) != null && !line.isEmpty()) {
            String[] headerParts = line.split(":", 2);
            if (headerParts.length == 2) {
                response.addHeader(headerParts[0].trim(), headerParts[1].trim());
            }
        }

        return response;
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