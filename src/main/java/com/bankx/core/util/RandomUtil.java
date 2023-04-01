package com.bankx.core.util;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil {

    private static final char[] base62Chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final Random random = new Random();
    private static final int DEFAULT_CHAR_LENGTH = 8;

    private static long generatorLastNum = 0;

    public static String generateNumber(int length) {

        if(length > 18) {
            length = 18;
        }

        int randomMin = 0;
        int randomMax = Double.valueOf(Math.pow(10,length/2 + 1)).intValue();

        if(length <= 2) {

            return String.valueOf(ThreadLocalRandom.current().nextInt(randomMin, randomMax));
        }

        randomMin =  Double.valueOf(Math.pow(10,length/2)).intValue();

        long generatorLimit =  Double.valueOf(Math.pow(10,Math.floor((length-1)/2))).longValue();

        long id = System.currentTimeMillis() % generatorLimit;
        if ( id <= generatorLastNum ) {
            id = (generatorLastNum + 1) % generatorLimit;
        }
        long time = (generatorLastNum = id);

        int randomInt = ThreadLocalRandom.current().nextInt(randomMin, randomMax);

        String random = String.valueOf(randomInt) + time;

        while(random.length() < length) {
            random += ThreadLocalRandom.current().nextInt(0,10);
        }

        return random;
    }

    public static String generateReferenceNumber() {

        var sb = new StringBuilder(DEFAULT_CHAR_LENGTH);

        for (int i = 0; i< DEFAULT_CHAR_LENGTH; i++) {
            sb.append(base62Chars[random.nextInt(36)]);
        }

        return sb.toString();
    }


}
