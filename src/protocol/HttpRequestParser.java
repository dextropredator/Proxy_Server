package protocol;

public class HttpRequestParser {

    public static String extractHost(String requestLine) {
        String url = requestLine.split(" ")[1];
        return url.split("/")[2];
    }
}