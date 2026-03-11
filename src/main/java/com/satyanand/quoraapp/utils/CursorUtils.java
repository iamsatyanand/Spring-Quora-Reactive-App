package com.satyanand.quoraapp.utils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

public class CursorUtils {

    public static boolean isValidCursor(String cursor){
        if(cursor == null || cursor.isEmpty()){
            return false;
        }

        try{
            LocalDateTime.parse(cursor);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static String encode(LocalDateTime cursor){
        return Base64.getEncoder().encodeToString(cursor.toString().getBytes(StandardCharsets.UTF_8));
    }

    public static LocalDateTime decode(String cursor){
        return LocalDateTime.parse(
                new String(Base64.getDecoder().decode(cursor))
        );
    }

    public static LocalDateTime parseCursor(String cursor){
        if(!isValidCursor(cursor)){
            throw new IllegalArgumentException("Invalid cursor");
        }
        return LocalDateTime.parse(cursor);
    }
}
