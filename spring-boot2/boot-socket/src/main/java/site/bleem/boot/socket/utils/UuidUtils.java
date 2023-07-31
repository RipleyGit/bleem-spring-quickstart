package site.bleem.boot.socket.utils;

import java.util.UUID;

public class UuidUtils {
    
    public static String generateUuid() {
        return UUID.randomUUID().toString();
    }
}