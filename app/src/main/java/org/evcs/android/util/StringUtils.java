package org.evcs.android.util;

public final class StringUtils {

    private StringUtils() {}

    /**
     * Sets the first character of the {@link CharSequence} to upper case
     *
     * @param s CharSequence to set uppercase
     * @return CharSequence with first char as upper case
     */
    public static <T extends CharSequence> CharSequence capitalize(T s) {
        if (s == null || s.length() == 0) {
            return s;
        } else {
            return Character.toUpperCase(s.charAt(0))
                + s.subSequence(1, s.length()).toString();
        }
    }

    public static String onlyNumbers(CharSequence key) {
         return key.toString().replaceAll("[^0-9]", "");
    }
}
