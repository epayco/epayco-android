package co.epayco.android.util;

import androidx.annotation.Nullable;

public class EpaycoTextUtils {

    /**
     * Eval string is empty
     * @param value    String eval
     * @return boolean
     */
    public static boolean isBlank(String value) {
        return value == null || value.trim().length() == 0;
    }

    /**
     * Remove characters string
     * @param cardNumberWithSpaces    Number credit card
     * @return boolean
     */
    @Nullable
    static String removeSpacesAndHyphens(@Nullable String cardNumberWithSpaces) {
        if (isBlank(cardNumberWithSpaces)) {
            return null;
        }
        return cardNumberWithSpaces.replaceAll("\\s|-", "");
    }

    /**
     * Build prefixes
     * @param number      Number credit card
     * @param prefixes    Prefixes credit card
     * @return boolean
     */
    public static boolean hasAnyPrefix(String number, String... prefixes) {
        if (number == null) {
            return false;
        }

        for (String prefix : prefixes) {
            if (number.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Valid numbers positive
     * @param value    Number or string
     * @return boolean
     */
    public static boolean isWholePositiveNumber(String value) {
        if (value == null) {
            return false;
        }

        for (int i = 0; i < value.length(); i++) {
            if (!Character.isDigit(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
