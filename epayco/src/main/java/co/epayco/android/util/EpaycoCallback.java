package co.epayco.android.util;


import org.json.JSONException;
import org.json.JSONObject;

public interface EpaycoCallback {

    void onSuccess(JSONObject data) throws JSONException;

    void onError(Exception error);
}
