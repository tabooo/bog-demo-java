package com.bog.demo.util;

public class ShortURLGenerator {

    private static final String CHAR_MAP = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int BASE = CHAR_MAP.length();

    public static String idToShortURL(int id) {

        StringBuilder sb = new StringBuilder();

        while (id > 0) {
            sb.append(CHAR_MAP.charAt(id % BASE));
            id = id / BASE;
        }
        return sb.reverse().toString();
    }


    private static int shortURLtoID(String shortUrl) {
        int no = 0;
        for (char c : shortUrl.toCharArray()) {
            no = no * BASE + CHAR_MAP.indexOf(c);
        }
        return no;
    }

    //tests - main method
    public static void main(String args[]) {

        String shortUrl = idToShortURL(1234);
        System.out.println("shortUrl : " + shortUrl);
        System.out.println("ID : " + shortURLtoID(shortUrl));

    }
}