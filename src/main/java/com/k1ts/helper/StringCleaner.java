package com.k1ts.helper;

public class StringCleaner {

    public static String clean(String data) {
        if (data == null) {
            return "";
        }

        return data.strip();
    }
}
