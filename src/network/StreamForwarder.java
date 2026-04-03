package network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamForwarder implements Runnable {

    private InputStream inputStream;
    private OutputStream outputStream;

    public StreamForwarder(InputStream inputStream, OutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        try {
            byte[] buffer = new byte[4096];
            int bytesRead;
            
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                outputStream.flush();
            }
            
        } catch (IOException e) {
            
        } finally {
            try {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            } catch (IOException e) {
                
            }
        }
    }
}