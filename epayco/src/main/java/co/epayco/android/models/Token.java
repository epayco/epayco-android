package co.epayco.android.models;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;

public class Token {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({TYPE_CARD})
    public @interface TokenType {}
    public static final String TYPE_CARD = "card";

    private final String mId;
    private final String mType;
    private final Date mCreated;
    private final boolean mLivemode;
    private final boolean mUsed;
    private final Card mCard;

    public Token(
            String id,
            boolean livemode,
            Date created,
            Boolean used,
            Card card,
            @TokenType String type) {
        mId = id;
        mType = type;
        mCreated = created;
        mLivemode = livemode;
        mCard = card;
        mUsed = used;
    }

    public Date getCreated() {
        return mCreated;
    }

    public String getId() {
        return mId;
    }

    public boolean getLivemode() {
        return mLivemode;
    }

    public boolean getUsed() {
        return mUsed;
    }

    @TokenType
    public String getType() {
        return mType;
    }

    public Card getCard() {
        return mCard;
    }
}