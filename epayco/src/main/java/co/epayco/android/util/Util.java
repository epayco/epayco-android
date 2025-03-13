package co.epayco.android.util;

import static co.epayco.android.models.Card.AMERICAN_EXPRESS;
import static co.epayco.android.models.Card.DINERS_CLUB;
import static co.epayco.android.models.Card.DISCOVER;
import static co.epayco.android.models.Card.JCB;
import static co.epayco.android.models.Card.MASTERCARD;
import static co.epayco.android.models.Card.PREFIXES_AMERICAN_EXPRESS;
import static co.epayco.android.models.Card.PREFIXES_DINERS_CLUB;
import static co.epayco.android.models.Card.PREFIXES_DISCOVER;
import static co.epayco.android.models.Card.PREFIXES_JCB;
import static co.epayco.android.models.Card.PREFIXES_MASTERCARD;
import static co.epayco.android.models.Card.PREFIXES_VISA;
import static co.epayco.android.models.Card.UNKNOWN;
import static co.epayco.android.models.Card.VISA;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class Util {

    public static final int LENGTH_COMMON_CARD = 16;
    public static final int LENGTH_AMERICAN_EXPRESS = 15;
    public static final int LENGTH_DINERS_CLUB = 14;

    public static Boolean validateNumber(String number) {
        return isValidCardNumber(number);
    }

    public static boolean isValidCardNumber(@Nullable String cardNumber) {
        String normalizedNumber = EpaycoTextUtils.removeSpacesAndHyphens(cardNumber);
        return isValidLuhnNumber(normalizedNumber) && isValidCardLength(normalizedNumber);
    }

    public static boolean validateExpMonth(String expMonth) {
        int month = Integer.parseInt(expMonth);
        return expMonth != null && month >= 1 && month <= 12;
    }

    public static boolean validateExpYear(String expYear) {
        int year = Integer.parseInt(expYear);
        return expYear != null && !DateUtils.hasYearPassed(year);
    }

    public static boolean validateCVC(String cvc) {
        if (EpaycoTextUtils.isBlank(cvc)) {
            return false;
        }
        String cvcValue = cvc.trim();
        return EpaycoTextUtils.isWholePositiveNumber(cvcValue);
    }

    public static boolean isValidCardLength(String cardNumber) {
        if (cardNumber == null) {
            return false;
        }
        return isValidCardLength(cardNumber, getPossibleCardType(cardNumber, false));
    }

    public static boolean isValidCardLength(
            @Nullable String cardNumber,
            @NonNull String cardBrand) {
        if(cardNumber == null || UNKNOWN.equals(cardBrand)) {
            return false;
        }

        int length = cardNumber.length();
        switch (cardBrand) {
            case AMERICAN_EXPRESS:
                return length == LENGTH_AMERICAN_EXPRESS;
            case DINERS_CLUB:
                return length == LENGTH_DINERS_CLUB;
            default:
                return length == LENGTH_COMMON_CARD;
        }
    }

    private static String getPossibleCardType(@Nullable String cardNumber, boolean shouldNormalize) {
        if (EpaycoTextUtils.isBlank(cardNumber)) {
            return UNKNOWN;
        }

        String spacelessCardNumber = cardNumber;
        if (shouldNormalize) {
            spacelessCardNumber = EpaycoTextUtils.removeSpacesAndHyphens(cardNumber);
        }

        if (EpaycoTextUtils.hasAnyPrefix(spacelessCardNumber, PREFIXES_AMERICAN_EXPRESS)) {
            return AMERICAN_EXPRESS;
        } else if (EpaycoTextUtils.hasAnyPrefix(spacelessCardNumber, PREFIXES_DISCOVER)) {
            return DISCOVER;
        } else if (EpaycoTextUtils.hasAnyPrefix(spacelessCardNumber, PREFIXES_JCB)) {
            return JCB;
        } else if (EpaycoTextUtils.hasAnyPrefix(spacelessCardNumber, PREFIXES_DINERS_CLUB)) {
            return DINERS_CLUB;
        } else if (EpaycoTextUtils.hasAnyPrefix(spacelessCardNumber, PREFIXES_VISA)) {
            return VISA;
        } else if (EpaycoTextUtils.hasAnyPrefix(spacelessCardNumber, PREFIXES_MASTERCARD)) {
            return MASTERCARD;
        } else {
            return UNKNOWN;
        }
    }

    private static boolean isValidLuhnNumber(@Nullable String cardNumber) {
        if (cardNumber == null) {
            return false;
        }

        boolean isOdd = true;
        int sum = 0;

        for (int index = cardNumber.length() - 1; index >= 0; index--) {
            char c = cardNumber.charAt(index);
            if (!Character.isDigit(c)) {
                return false;
            }

            int digitInteger = Character.getNumericValue(c);
            isOdd = !isOdd;

            if (isOdd) {
                digitInteger *= 2;
            }

            if (digitInteger > 9) {
                digitInteger -= 9;
            }

            sum += digitInteger;
        }

        return sum % 10 == 0;
    }
}
