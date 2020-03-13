package co.epayco.android.models;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.*;
import cz.msebera.android.httpclient.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class Authentication {

    String apiKey;
    String privateKey;
    String lang;
    String token_bearer;
    Boolean test;

    public Authentication() {}

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Boolean getTest() {
        return test;
    }

    public void setTest(Boolean test) {
        this.test = test;
    }

    public void AuthService (String apiKey, String privateKey) {
        try {
          return  post("https://api.secure.payco.co/v1/auth/login", GetBearerToken(apiKey,privateKey), apiKey, callback);
        } catch (Exception e) {
            callback.onError(e);
        }

    }

public static RequestParams GetBearerToken(String apiKey, String privateKey) {
        {
        RequestParams cardParams = new RequestParams();
        cardParams.put("public_key", apiKey);
        cardParams.put("private_key", privateKey);
        return cardParams;
        }


            /**
     * Petition api type post
     * @param url      url petition api
     * @param data     data send petition
     * @param options  data user options
     * @param callback response request api
     */
    public static void post(String url, @NonNull RequestParams data, String options, @NonNull final EpaycoCallback callback) {
        client.setBasicAuth(options, "");
        client.addHeader("type", "sdk");
        client.post(url, data, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject obj = new JSONObject(new String(responseBody));
                    JSONObject data = new JSONObject(obj.getString("data"));
                    callback.onSuccess(data);
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                callback.onError((Exception) error);
            }
        });
    }

}
