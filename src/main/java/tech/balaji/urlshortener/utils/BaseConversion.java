package tech.balaji.urlshortener.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BaseConversion {

    private static final String ALLOWED_STRING = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final char[] ALLOWED_CHARACTER_ARRAY = ALLOWED_STRING.toCharArray();
    private static final int BASE_LENGTH = ALLOWED_CHARACTER_ARRAY.length;

    public static String encode(long input) {
        StringBuilder encodedString = new StringBuilder();

        if (input == 0) {
            return String.valueOf(ALLOWED_CHARACTER_ARRAY[0]);
        }

        while (input > 0) {
            encodedString.append(ALLOWED_CHARACTER_ARRAY[(int) (input % BASE_LENGTH)]);
            input = input / BASE_LENGTH;
        }

        return encodedString.reverse().toString();
    }

}
