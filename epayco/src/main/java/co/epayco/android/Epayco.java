package co.epayco.android;

import com.loopj.android.http.*;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import co.epayco.android.models.Authentication;
import co.epayco.android.models.Card;
import co.epayco.android.models.Cash;
import co.epayco.android.models.Charge;
import co.epayco.android.models.ChargeSub;
import co.epayco.android.models.Client;
import co.epayco.android.models.Daviplata;
import co.epayco.android.models.DaviplataConfirm;
import co.epayco.android.models.Plan;
import co.epayco.android.models.Pse;
import co.epayco.android.models.Safetypay;
import co.epayco.android.models.Subscription;
import co.epayco.android.util.DateUtils;
import co.epayco.android.util.EpaycoCallback;
import co.epayco.android.util.Util;

import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromCLient;
import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromCLientCardDefault;
import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromCLientCardNew;
import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromCLientDelete;
import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromCard;
import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromCash;
import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromCharge;
import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromDaviplataConfirm;
import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromEmpty;
import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromPlan;
import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromPse;
import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromSafetypay;
import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromDaviplata;
import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromSub;
import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromSubCancel;
import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromSubCharge;

import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class Epayco {

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static final String BASE_URL = "https://api.secure.payco.co";
    private static final String BASE_URL_SECURE = "https://secure.payco.co/restpagos";
    public static final String BASE_URL_APIFY = "https://apify.epayco.co";

    private static final int MAX_TIME_OUT= 190*10000;

    private String apiKey;
    private String privateKey;
    private String lang;
    private Boolean test;
    String token_bearer;

    public Epayco(Authentication auth) {
        //Tiempos de respuesta modificados
        client.setConnectTimeout(MAX_TIME_OUT);
        client.setResponseTimeout(MAX_TIME_OUT);

        this.apiKey = auth.getApiKey();
        this.privateKey = auth.getPrivateKey();
        this.lang = auth.getLang();
        this.test = auth.getTest();

    }

    private JSONObject buildOptions() {
        JSONObject options = new JSONObject();

        try {
            options.put("apiKey", apiKey);
            options.put("privateKey", privateKey);
            options.put("lang", lang);
            options.put("test", test);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return options;
    }

    /****************************
     * Access token definitions *
     ***************************/

    /**
     * Create credit card token
     * @param card     data credit card
     * @param callback response request api
     */
    public void createToken(@NonNull final Card card, @NonNull final EpaycoCallback callback) {
        final EpaycoCallback Token = new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                try {
                    callback.onSuccess(data);
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
            @Override
            public void onError(Exception e) {
                System.err.println("Error en la solicitud: " + e.getMessage());
                callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                token_bearer = data.getString("bearer_token");
                String Base = base(false);
                String jsonBody = hashMapFromCard(card);
                if(token_bearer != null){
                    try {
                        post(Base + "/v1/tokens", jsonBody,token_bearer,callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
            @Override
            public void onError(Exception error) {
                Log.d("createToken","=>"+error);
                Token.onError(error);
            }
        });
    }


    /***************************
     * Validations credit card *
     **************************/

    /**
     * Valid all parameters credit card
     * @param card    Card model
     * @return boolean
     */
    public Boolean validCard(@NonNull Card card) {
        if (card.getCvc() == null) {
            return Util.validateNumber(card.getNumber()) && validateExpiryDate(card.getMonth(), card.getYear());
        } else {
            return Util.validateNumber(card.getNumber()) && validateExpiryDate(card.getMonth(), card.getYear()) && validateCVC(card.getCvc());
        }
    }

    /**
     * Valid number credit card
     * @param number    Number card model
     * @return boolean
     */
    public Boolean validNumberCard(String number) {
        return Util.isValidCardNumber(number);
    }

    /**
     * Validate month expiration credit card
     * @param month    Month card model
     * @return boolean
     */
    public Boolean validExpiryMonth(String month) {
        return Util.validateExpMonth(month);
    }

    /**
     * Validate year expration credit card
     * @param year    Year card model
     * @return
     */
    public Boolean validExpiryYear(String year) {
        return Util.validateExpYear(year);
    }

    /**
     * Validate CVC
     * @param cvc    CVC card model
     * @return boolean
     */
    public Boolean validateCVC(String cvc) {
        return Util.validateCVC(cvc);
    }

    /**
     * Validate month and year expiration
     * @param month    Month expiration card model
     * @param year     Year expiration card model
     * @return boolean
     */
    public boolean validateExpiryDate(String month, String year) {
        if (!Util.validateExpMonth(month)) {
            return false;
        }
        if (!Util.validateExpYear(year)) {
            return false;
        }
        int expMonth = Integer.parseInt(month);
        int expYear = Integer.parseInt(year);
        return !DateUtils.hasMonthPassed(expYear, expMonth);
    }


    /*******************************
     * Access customer definitions *
     ******************************/

    /**
     * Create customer
     * @param client
     * @param callback
     */
    public void createCustomer(@NonNull final Client client, @NonNull final EpaycoCallback callback) {
        final EpaycoCallback Token = new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                try {
                    callback.onSuccess(data);
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
            @Override
            public void onError(Exception e) {
                System.err.println("Error en la solicitud: " + e.getMessage());
                callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                token_bearer = data.getString("bearer_token");
                String Base = base(false);
                String jsonBody = hashMapFromCLient(client);
                if(token_bearer != null){
                    try {
                        post(Base + "/payment/v1/customer/create", jsonBody,token_bearer,callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
            @Override
            public void onError(Exception error) {
                Log.d("createToken","=>"+error);
                Token.onError(error);
            }
        });
    }

    /**
     * Retrieve customer from id
     * @param uid      id customer
     * @param callback response request api
     */
    public void getCustomer(@NonNull final String uid, @NonNull final EpaycoCallback callback) {
        final EpaycoCallback Token = new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                try {
                    callback.onSuccess(data);
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
            @Override
            public void onError(Exception e) {
                System.err.println("Error en la solicitud: " + e.getMessage());
                callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                token_bearer = data.getString("bearer_token");
                String Base = base(false);
                if(token_bearer != null){
                    try {
                        get(Base + "/payment/v1/customer/" + apiKey + "/" + uid,token_bearer,callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
            @Override
            public void onError(Exception error) {
                Log.d("createToken","=>"+error);
                Token.onError(error);
            }
        });
    }

    /**
     * Return list customer
     * @param callback    response request api
     */
    public void getCustomerList(@NonNull final EpaycoCallback callback) {
        final EpaycoCallback Token = new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                try {
                    callback.onSuccess(data);
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
            @Override
            public void onError(Exception e) {
                System.err.println("Error en la solicitud: " + e.getMessage());
                callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                token_bearer = data.getString("bearer_token");
                String Base = base(false);
                if(token_bearer != null){
                    try {
                        get(Base + "/payment/v1/customers/" + apiKey,token_bearer,callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
            @Override
            public void onError(Exception error) {
                Log.d("createToken","=>"+error);
                Token.onError(error);
            }
        });
    }

    /**
     * delete customer token
     * @param client
     * @param callback
     */
    public void deleteTokenCustomer(@NonNull final Client client, @NonNull final EpaycoCallback callback) {
        final EpaycoCallback Token = new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                try {
                    callback.onSuccess(data);
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
            @Override
            public void onError(Exception e) {
                System.err.println("Error en la solicitud: " + e.getMessage());
                callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                token_bearer = data.getString("bearer_token");
                String Base = base(false);
                String jsonBody = hashMapFromCLientDelete(client);
                if(token_bearer != null){
                    try {
                        post(Base + "/v1/remove/token",jsonBody,token_bearer,callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
            @Override
            public void onError(Exception error) {
                Log.d("createToken","=>"+error);
                Token.onError(error);
            }
        });
    }

    /**
     * add default token card exited
     * @param client
     * @param callback
     */
    public void addTokenDefault(@NonNull final Client client, @NonNull final EpaycoCallback callback) {
        final EpaycoCallback Token = new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                try {
                    callback.onSuccess(data);
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
            @Override
            public void onError(Exception e) {
                System.err.println("Error en la solicitud: " + e.getMessage());
                callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                token_bearer = data.getString("bearer_token");
                String Base = base(false);
                String jsonBody = hashMapFromCLientCardDefault(client);
                if(token_bearer != null){
                    try {
                        post(Base + "/payment/v1/customer/reasign/card/default",jsonBody,token_bearer,callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
            @Override
            public void onError(Exception error) {
                Log.d("createToken","=>"+error);
                Token.onError(error);
            }
        });
    }


    /**
     * add new token card
     * @param client
     * @param callback
     */
    public void addNewToken(@NonNull final Client client, @NonNull final EpaycoCallback callback) {
        final EpaycoCallback Token = new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                try {
                    callback.onSuccess(data);
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
            @Override
            public void onError(Exception e) {
                System.err.println("Error en la solicitud: " + e.getMessage());
                callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                token_bearer = data.getString("bearer_token");
                String Base = base(false);
                String jsonBody = hashMapFromCLientCardNew(client);
                if(token_bearer != null){
                    try {
                        post(Base + "/v1/customer/add/token",jsonBody,token_bearer,callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
            @Override
            public void onError(Exception error) {
                Log.d("createToken","=>"+error);
                Token.onError(error);
            }
        });
    }

    /***************************
     * Access plan definitions *
     ***************************/

    /**
     * Create plan
     * @param plan        data model plan
     * @param callback    response request api
     */
    public void createPlan(@NonNull final Plan plan, @NonNull final EpaycoCallback callback) {
        final EpaycoCallback Token = new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                try {
                    callback.onSuccess(data);
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
            @Override
            public void onError(Exception e) {
                System.err.println("Error en la solicitud: " + e.getMessage());
                callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                token_bearer = data.getString("bearer_token");
                String Base = base(false);
                String jsonBody = hashMapFromPlan(plan);
                if(token_bearer != null){
                    try {
                        post(Base + "/recurring/v1/plan/create",jsonBody,token_bearer,callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
            @Override
            public void onError(Exception error) {
                Log.d("createToken","=>"+error);
                Token.onError(error);
            }
        });
    }

    /**
     * Retrieve plan from id
     * @param uid         id plan
     * @param callback    response request api
     */
    public void getPlan(@NonNull final String uid, @NonNull final EpaycoCallback callback) {
        final EpaycoCallback Token = new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                try {
                    callback.onSuccess(data);
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
            @Override
            public void onError(Exception e) {
                System.err.println("Error en la solicitud: " + e.getMessage());
                callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                token_bearer = data.getString("bearer_token");
                String Base = base(false);
                if(token_bearer != null){
                    try {
                        get(Base + "/recurring/v1/plan/" + apiKey + "/" + uid,token_bearer,callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
            @Override
            public void onError(Exception error) {
                Log.d("createToken","=>"+error);
                Token.onError(error);
            }
        });
    }

    public void deletePlan(@NonNull final String uid, @NonNull final EpaycoCallback callback) {
        final EpaycoCallback Token = new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                try {
                    callback.onSuccess(data);
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
            @Override
            public void onError(Exception e) {
                System.err.println("Error en la solicitud: " + e.getMessage());
                callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                token_bearer = data.getString("bearer_token");
                String Base = base(false);
                String jsonBody = hashMapFromEmpty();
                if(token_bearer != null){
                    try {
                        post(Base + "/recurring/v1/plan/remove/" + apiKey + "/" + uid,jsonBody,token_bearer,callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
            @Override
            public void onError(Exception error) {
                Log.d("createToken","=>"+error);
                Token.onError(error);
            }
        });
    }

    /**
     * Return list plan
     * @param callback    response request api
     */
    public void getPlanList(@NonNull final String uid, @NonNull final EpaycoCallback callback) {
        final EpaycoCallback Token = new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                try {
                    callback.onSuccess(data);
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
            @Override
            public void onError(Exception e) {
                System.err.println("Error en la solicitud: " + e.getMessage());
                callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                token_bearer = data.getString("bearer_token");
                String Base = base(false);
                if(token_bearer != null){
                    try {
                        get(Base + "/recurring/v1/plans/",token_bearer,callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
            @Override
            public void onError(Exception error) {
                Log.d("createToken","=>"+error);
                Token.onError(error);
            }
        });
    }

    /************************************
     * Access subscriptions definitions *
     ************************************/

    /**
     *
     * Create subscription
     * @param sub         data model subscription
     * @param callback    response request api
     */
    public void createSubscription(@NonNull final Subscription sub, @NonNull final EpaycoCallback callback) {
        final EpaycoCallback Token = new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                try {
                    callback.onSuccess(data);
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
            @Override
            public void onError(Exception e) {
                System.err.println("Error en la solicitud: " + e.getMessage());
                callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                token_bearer = data.getString("bearer_token");
                String Base = base(false);
                String jsonBody = hashMapFromSub(sub);
                if(token_bearer != null){
                    try {
                        post(Base + "/recurring/v1/subscription/create",jsonBody,token_bearer,callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
            @Override
            public void onError(Exception error) {
                Log.d("createToken","=>"+error);
                Token.onError(error);
            }
        });
    }

    /**
     * Retrieve subscription from id
     * @param uid         id subscription
     * @param callback    response request api
     */
    public void getSubscription(@NonNull final String uid, @NonNull final EpaycoCallback callback) {
        final EpaycoCallback Token = new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                try {
                    callback.onSuccess(data);
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
            @Override
            public void onError(Exception e) {
                System.err.println("Error en la solicitud: " + e.getMessage());
                callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                token_bearer = data.getString("bearer_token");
                String Base = base(false);
                if(token_bearer != null){
                    try {
                        get(Base + "/recurring/v1/subscription/" + uid + "/" + apiKey,token_bearer,callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
            @Override
            public void onError(Exception error) {
                Log.d("createToken","=>"+error);
                Token.onError(error);
            }
        });
    }

    /**
     * Get list subscription
     * @param callback    response request api
     */
    public void getSubscriptionList( @NonNull final EpaycoCallback callback) {
        final EpaycoCallback Token = new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                try {
                    callback.onSuccess(data);
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
            @Override
            public void onError(Exception e) {
                System.err.println("Error en la solicitud: " + e.getMessage());
                callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                token_bearer = data.getString("bearer_token");
                String Base = base(false);
                if(token_bearer != null){
                    try {
                        get(Base + "/recurring/v1/subscriptions/" + apiKey,token_bearer,callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
            @Override
            public void onError(Exception error) {
                Log.d("createToken","=>"+error);
                Token.onError(error);
            }
        });
    }

    /**
     * Charge subscription ans subscribe plan
     * @param sub         data model charge subscription
     * @param callback    response request api
     */
    public void chargeSubscription(@NonNull final ChargeSub sub, @NonNull final EpaycoCallback callback) {
        final EpaycoCallback Token = new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                try {
                    callback.onSuccess(data);
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
            @Override
            public void onError(Exception e) {
                System.err.println("Error en la solicitud: " + e.getMessage());
                callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                token_bearer = data.getString("bearer_token");
                String Base = base(false);
                String jsonBody = hashMapFromSubCharge(sub);
                if(token_bearer != null){
                    try {
                        post(Base + "/payment/v1/charge/subscription/create",jsonBody,token_bearer,callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
            @Override
            public void onError(Exception error) {
                Log.d("createToken","=>"+error);
                Token.onError(error);
            }
        });
    }


    /**
     * Cancel subscription from id
     * @param uid         id subscription
     * @param callback    response request api
     */
    public void cancelSubscription(@NonNull final String uid, @NonNull final EpaycoCallback callback) {
        final EpaycoCallback Token = new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                try {
                    callback.onSuccess(data);
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
            @Override
            public void onError(Exception e) {
                System.err.println("Error en la solicitud: " + e.getMessage());
                callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                token_bearer = data.getString("bearer_token");
                String Base = base(false);
                String jsonBody = hashMapFromSubCancel(uid);
                if(token_bearer != null){
                    try {
                        post(Base + "/recurring/v1/subscription/cancel",jsonBody,token_bearer,callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
            @Override
            public void onError(Exception error) {
                Log.d("createToken","=>"+error);
                Token.onError(error);
            }
        });
    }

    /***************************
     * Access bank definitions *
     ***************************/

    /**
     * Create pse transaction
     * @param pse         data model pse
     * @param callback    response request api
     */
    public void createPseTransaction(@NonNull final Pse pse, @NonNull final EpaycoCallback callback) {
        final EpaycoCallback Token = new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                try {
                    callback.onSuccess(data);
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
            @Override
            public void onError(Exception e) {
                System.err.println("Error en la solicitud: " + e.getMessage());
                callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                token_bearer = data.getString("bearer_token");
                String Base = base(false);
                String jsonBody = hashMapFromPse(pse, buildOptions());
                if(token_bearer != null){
                    try {
                        post(BASE_URL_SECURE + "/pagos/debitos.json", jsonBody,token_bearer,callback);
                        //post("https://webhook.site/6fe14390-858a-4e38-972d-d8e49f6be366", jsonBody,token_bearer,callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
            @Override
            public void onError(Exception error) {
                Log.d("createToken","=>"+error);
                Token.onError(error);
            }
        });
    }

    /**
     * Retrieve pse transaction from transactionID
     * @param uid         transaction id
     * @param callback    response request api
     */
    public void getPseTransaction(@NonNull final String uid, @NonNull final EpaycoCallback callback) {
        final EpaycoCallback Token = new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                try {
                    callback.onSuccess(data);
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
            @Override
            public void onError(Exception e) {
                System.err.println("Error en la solicitud: " + e.getMessage());
                callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                token_bearer = data.getString("bearer_token");
                String Base = base(false);
                if(token_bearer != null){
                    try {
                        get(Base + "/pse/transactioninfomation.json?transactionID=" + uid + "&public_key=" + apiKey,token_bearer,callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
            @Override
            public void onError(Exception error) {
                Log.d("createToken","=>"+error);
                Token.onError(error);
            }
        });
    }

    /**
     * Get Banks List
     */
    public void getBanks( @NonNull final EpaycoCallback callback) {
        final EpaycoCallback Token = new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                try {
                    callback.onSuccess(data);
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
            @Override
            public void onError(Exception e) {
                System.err.println("Error en la solicitud: " + e.getMessage());
                callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                token_bearer = data.getString("bearer_token");
                String Base = base(false);
                String strTest = "";
                if(test){
                    strTest= "1";
                }else{
                    strTest= "2";
                }
                String testStr = apiKey + "&test=" + strTest;
                if(token_bearer != null){
                    try {
                        get(Base + "/pse/bancos.json?public_key=" + testStr,token_bearer,callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
            @Override
            public void onError(Exception error) {
                Log.d("createToken","=>"+error);
                Token.onError(error);
            }
        });
    }

    /***************************
     * Access cash definitions *
     ***************************/

    /**
     * Create cash transaction
     * @param cash        data cash model
     * @param callback    response request api
     */
    public void createCashTransaction(@NonNull final Cash cash, @NonNull final EpaycoCallback callback) {
        if(cash.getType().toLowerCase(Locale.ROOT).equals("baloto")){
            Exception err = new Exception("Método de pago en efectivo no válido, unicamnete soportados: efecty, gana, redservi, puntored, sured");
            callback.onError(err);
            return;
        }
        final EpaycoCallback Token = new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                try {
                    callback.onSuccess(data);
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
            @Override
            public void onError(Exception e) {
                System.err.println("Error en la solicitud: " + e.getMessage());
                callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                token_bearer = data.getString("bearer_token");
                String Base = base(false);
                String jsonBody = hashMapFromCash(cash, buildOptions());
                String url = null;
                switch (cash.getType()) {
                    case "efecty":
                        url = "/v2/efectivo/efecty";
                        break;
                    case "gana":
                        url = "/v2/efectivo/gana";
                        break;
                    case "redservi":
                        url = "/v2/efectivo/redservi";
                        break;
                    case "puntored":
                        url = "/v2/efectivo/puntored";
                        break;
                    case "sured":
                        url = "/v2/efectivo/sured";
                        break;
                    default:
                        System.out.println("error payment");
                }
                if(token_bearer != null){
                    try {
                        post(BASE_URL_SECURE + url, jsonBody,token_bearer,callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
            @Override
            public void onError(Exception error) {
                Log.d("createToken","=>"+error);
                Token.onError(error);
            }
        });
    }

    public void getReferencePayment(@NonNull final String uid, @NonNull final EpaycoCallback callback) {
        final EpaycoCallback Token = new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                try {
                    callback.onSuccess(data);
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
            @Override
            public void onError(Exception e) {
                System.err.println("Error en la solicitud: " + e.getMessage());
                callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                token_bearer = data.getString("bearer_token");
                String Base = base(false);
                if(token_bearer != null){
                    try {
                        get(Base + "/transaction/response.json?ref_payco=" + uid + "&public_key=" + apiKey,token_bearer,callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
            @Override
            public void onError(Exception error) {
                Log.d("createToken","=>"+error);
                Token.onError(error);
            }
        });
    }



    /*****************************
     * Access charge definitions *
     *****************************/

    /**
     * Create charge token card
     * @param charge      charge model
     * @param callback    response request api
     */
    public void createCharge(@NonNull final Charge charge, @NonNull final EpaycoCallback callback) {
        final EpaycoCallback Token = new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                try {
                    callback.onSuccess(data);
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
            @Override
            public void onError(Exception e) {
                System.err.println("Error en la solicitud: " + e.getMessage());
                callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                token_bearer = data.getString("bearer_token");
                String Base = base(false);
                String jsonBody = hashMapFromCharge(charge);
                if(token_bearer != null){
                    try {
                        post(Base + "/payment/v1/charge/create", jsonBody,token_bearer,callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
            @Override
            public void onError(Exception error) {
                Log.d("createToken","=>"+error);
                Token.onError(error);
            }
        });
    }

    /*****************************
     * Access davioplata definitions *
     *****************************/
    /**
     * Create daviplata transaction
     * @param daviplata   Daviplata model
     * @param callback    response request api
     */
    public void createDaviplata(@NonNull final Daviplata daviplata, @NonNull final EpaycoCallback callback) {
        final EpaycoCallback Token = new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                try {
                    callback.onSuccess(data);
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
            @Override
            public void onError(Exception e) {
                System.err.println("Error en la solicitud: " + e.getMessage());
                callback.onError(e);
            }
        };
        new Authentication().AuthServiceApify(apiKey,privateKey,new EpaycoCallback(){
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                token_bearer = data.getString("token");
                String Base = base(false);
                String jsonBody = hashMapFromDaviplata(daviplata);
                if(token_bearer != null){
                    try {
                        post(BASE_URL_APIFY + "/payment/process/daviplata", jsonBody,token_bearer,callback);
                        //post( "https://webhook.site/6fe14390-858a-4e38-972d-d8e49f6be366", jsonBody,token_bearer,callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
            @Override
            public void onError(Exception error) {
                Log.d("createToken","=>"+error);
                Token.onError(error);
            }
        });
    }

    /**
     * Confirm daviplata transaction
     * @param confirm DaviplatfConfirma model
     * @param callback    response request api
     */
    public void confirmDaviplata(@NonNull final DaviplataConfirm confirm, @NonNull final EpaycoCallback callback) {
        final EpaycoCallback Token = new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                try {
                    callback.onSuccess(data);
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
            @Override
            public void onError(Exception e) {
                System.err.println("Error en la solicitud: " + e.getMessage());
                callback.onError(e);
            }
        };
        new Authentication().AuthServiceApify(apiKey,privateKey,new EpaycoCallback(){
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                token_bearer = data.getString("bearer_token");
                String Base = base(false);
                String jsonBody = hashMapFromDaviplataConfirm(confirm);
                if(token_bearer != null){
                    try {
                        post(BASE_URL_APIFY + "/payment/confirm/daviplata", jsonBody,token_bearer,callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
            @Override
            public void onError(Exception error) {
                Log.d("createToken","=>"+error);
                Token.onError(error);
            }
        });
    }

    /*****************************
     * Access safetypay definitions *
     *****************************/
    /**
     * Create safetypay transaction
     * @param safetypay   Safetypay model
     * @param callback    response request api
     */
    public void createSafetypay(@NonNull final Safetypay safetypay, @NonNull final EpaycoCallback callback) {
        final EpaycoCallback Token = new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                try {
                    callback.onSuccess(data);
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
            @Override
            public void onError(Exception e) {
                System.err.println("Error en la solicitud: " + e.getMessage());
                callback.onError(e);
            }
        };
        new Authentication().AuthServiceApify(apiKey,privateKey,new EpaycoCallback(){
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                token_bearer = data.getString("token");
                String Base = base(false);
                String jsonBody = hashMapFromSafetypay(safetypay);
                if(token_bearer != null){
                    try {
                        post(BASE_URL_APIFY + "/payment/process/safetypay", jsonBody,token_bearer,callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
            @Override
            public void onError(Exception error) {
                Log.d("createToken","=>"+error);
                Token.onError(error);
            }
        });
    }


    /***************
     * Definitions *
     ***************/

    /**
     * Petition api type get
     * @param urlString  url petition api
     * @param token  data user token
     * @param callback response request api
     */
    public void get(final String urlString,final String token,final EpaycoCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    // Configurar la URL de destino
                    URL url = new URL(urlString);

                    // Abrir la conexión HTTP
                    connection = (HttpURLConnection) url.openConnection();

                    // Configurar la conexión
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Authorization", "Bearer "+token);
                    connection.setRequestProperty("type", "sdk-jwt ");
                    // Obtener la respuesta
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // Leer la respuesta
                        try (BufferedReader br = new BufferedReader(
                                new InputStreamReader(connection.getInputStream()))) {
                            StringBuilder data = new StringBuilder();
                            String responseLine;
                            while ((responseLine = br.readLine()) != null) {
                                data.append(responseLine.trim());
                            }
                            JSONObject response = new JSONObject(new String(data));
                            // Llamar al método onSuccess del Callback
                            callback.onSuccess(response);
                        }
                    } else {
                        // Llamar al método onError del Callback en caso de error
                        callback.onError(new Exception("HTTP Error: " + responseCode));
                    }
                } catch (Exception e) {
                    // Llamar al método onError del Callback en caso de excepción
                    callback.onError(e);
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    /**
     * Petition api type post
     * @param urlString  url petition api
     * @param jsonBody   data send petition
     * @param token      data user token
     * @param callback response request api
     */
    public static void post(final String urlString,final String jsonBody,final String token,final EpaycoCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection connection = null;
                try {
                    // Configurar la URL de destino
                    URL url = new URL(urlString);

                    // Abrir la conexión HTTP
                    connection = (HttpURLConnection) url.openConnection();

                    // Configurar la conexión
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Authorization", "Bearer "+token);
                    connection.setRequestProperty("type", "sdk-jwt ");
                    connection.setDoOutput(true);

                    // Obtener el flujo de salida y escribir el cuerpo JSON
                    try (OutputStream os = connection.getOutputStream()) {
                        byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                        os.write(input, 0, input.length);
                    }

                    // Obtener la respuesta
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // Leer la respuesta
                        try (BufferedReader br = new BufferedReader(
                                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                            StringBuilder data = new StringBuilder();
                            String responseLine;
                            while ((responseLine = br.readLine()) != null) {
                                data.append(responseLine.trim());
                            }
                            JSONObject response = new JSONObject(new String(data));
                            // Llamar al método onSuccess del Callback
                            callback.onSuccess(response);
                        }
                    } else {
                        // Llamar al método onError del Callback en caso de error
                        callback.onError(new IOException("HTTP Error: " + responseCode));
                    }
                } catch (Exception e) {
                    // Llamar al método onError del Callback en caso de excepción
                    callback.onError(e);
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }

            }
        }).start();
    }
    public String base(boolean isApi) {
        if (isApi) {
            return BASE_URL_SECURE;
        } else {
            return BASE_URL;
        }
    }
}