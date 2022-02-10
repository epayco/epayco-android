// package co.epayco.android.models;
// import com.loopj.android.http.RequestParams;
// import com.loopj.android.http.*;
// import cz.msebera.android.httpclient.Header;
// import org.json.JSONException;
// import org.json.JSONObject;
// import co.epayco.android.util.EpaycoCallback;

package co.epayco.android.models;
import android.support.annotation.NonNull;
import co.epayco.android.Epayco;
import co.epayco.android.util.EpaycoCallback;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

public class Authentication {
    private static AsyncHttpClient cliente = new AsyncHttpClient();
    String apiKey;
    String privateKey;
    String lang;
    String auth;
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

    public String getAuth() {
        return auth;
    }
    public void setAuth(String auth) {
        this.auth = auth;
    }


    public Epayco AuthService (String apiKey, String privateKey, @NonNull EpaycoCallback callback) {
        try {
            post("https://api.secure.epayco.io/v1/auth/login", GetBearerToken(apiKey,privateKey), apiKey,false, callback);
        } catch (Exception e) {
            callback.onError(e);
        }
        return null;
    }

    public Epayco AuthServiceApify (String apiKey, String privateKey, @NonNull EpaycoCallback callback) {
        try {
            String basic = apiKey+":"+privateKey;
            byte[] encoded = Base64.encode(basic.getBytes(), Base64.DEFAULT);
            String tokenizer = new String(encoded);
            String token = "Basic " + tokenizer;
            System.out.println("voy a pedir el token a apify");
            post("https://apify.epayco.io/login", GetBearerToken(apiKey,privateKey), token, true, callback);
        } catch (Exception e) {
            callback.onError(e);
        }
        return null;
    }

   // public static RequestParams GetBearerToken(String apiKey, String privateKey) {
    private RequestParams GetBearerToken(String apiKey, String privateKey) {
        {
            RequestParams cardParams = new RequestParams();
            cardParams.put("public_key", apiKey);
            cardParams.put("private_key", privateKey);
            return cardParams;
        }
    }


    /**
     * Petition api type post
     * @param url      url petition api
     * @param data     data send petition
     * @param options  data user options
     * @param callback response request api
     */
    public static void post(String url, @NonNull RequestParams data, String options, Boolean isApify, @NonNull final EpaycoCallback callback) {
        
        if(isApify){
            System.out.println("entro al if isApify \n");
            System.out.println(options);
            cliente.addHeader("Authorization", options);
        }else{
            cliente.setBasicAuth(options, "");
        }

        cliente.addHeader("type", "sdk");
        cliente.post(url, data, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {

                try {
                    String jsonString = new String(responseBody);
                    System.out.println("jsonString \n");
                    System.out.println(jsonString);
                    JSONObject jsonObject = new JSONObject(jsonString);
                    System.out.println("jsonObject \n");
                    System.out.println(jsonObject);
                    JSONObject obj = new JSONObject(new String(responseBody));
                    System.out.println("obj \n");
                    System.out.println(obj);

                    callback.onSuccess(obj);
                } catch (JSONException e) {
                    System.out.println("catch => \n");
                    System.out.println(e);
                    callback.onError(e);
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println(("estoy en el onFailure \n"));
                System.out.println(" \n");
                System.out.println(headers);
                System.out.println(" \n");
                System.out.println(new String(responseBody));
                System.out.println(" \n");
                callback.onError((Exception) error);
            }

        });
    }
}
