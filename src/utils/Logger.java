package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void info(String message) {
        log("INFO", message);
    }

    public static void warn(String message) {
        log("WARN", message);
    }

    public static void error(String message) {
        log("ERROR", message);
    }

    public static void error(String message, Throwable t) {
        log("ERROR", message + " - Exception: " + t.getMessage());
        t.printStackTrace();
    }

    private static void log(String level, String message) {
        String time = dateFormat.format(new Date());
        String logMessage = String.format("[%s] [%s] %s", time, level, message);
        
        if (level.equals("ERROR")) {
            System.err.println(logMessage);
        } else {
            System.out.println(logMessage);
        }
    }
}