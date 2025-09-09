package com.epayco.android;

import androidx.annotation.NonNull;
import com.loopj.android.http.*;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import com.epayco.android.models.Authentication;
import com.epayco.android.models.Card;
import com.epayco.android.models.Cash;
import com.epayco.android.models.Charge;
import com.epayco.android.models.ChargeSub;
import com.epayco.android.models.Client;
import com.epayco.android.models.ClientList;
import com.epayco.android.models.Daviplata;
import com.epayco.android.models.DaviplataConfirm;
import com.epayco.android.models.Plan;
import com.epayco.android.models.PlanUpdate;
import com.epayco.android.models.Pse;
import com.epayco.android.models.Safetypay;
import com.epayco.android.models.Subscription;
import com.epayco.android.util.DateUtils;
import com.epayco.android.util.EpaycoCallback;
import com.epayco.android.util.Util;

import static com.epayco.android.util.EpaycoNetworkUtils.hashMapFromCLient;
import static com.epayco.android.util.EpaycoNetworkUtils.hashMapFromCLientCardDefault;
import static com.epayco.android.util.EpaycoNetworkUtils.hashMapFromCLientCardNew;
import static com.epayco.android.util.EpaycoNetworkUtils.hashMapFromCLientDelete;
import static com.epayco.android.util.EpaycoNetworkUtils.hashMapFromCLients;
import static com.epayco.android.util.EpaycoNetworkUtils.hashMapFromCard;
import static com.epayco.android.util.EpaycoNetworkUtils.hashMapFromCash;
import static com.epayco.android.util.EpaycoNetworkUtils.hashMapFromCharge;
import static com.epayco.android.util.EpaycoNetworkUtils.hashMapFromDaviplataConfirm;
import static com.epayco.android.util.EpaycoNetworkUtils.hashMapFromEmpty;
import static com.epayco.android.util.EpaycoNetworkUtils.hashMapFromPlan;
import static com.epayco.android.util.EpaycoNetworkUtils.hashMapFromPlanUpdate;
import static com.epayco.android.util.EpaycoNetworkUtils.hashMapFromPse;
import static com.epayco.android.util.EpaycoNetworkUtils.hashMapFromSafetypay;
import static com.epayco.android.util.EpaycoNetworkUtils.hashMapFromDaviplata;
import static com.epayco.android.util.EpaycoNetworkUtils.hashMapFromSub;
import static com.epayco.android.util.EpaycoNetworkUtils.hashMapFromSubCancel;
import static com.epayco.android.util.EpaycoNetworkUtils.hashMapFromSubCharge;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class Epayco {

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static final String BASE_URL = System.getenv("BASE_URL_SDK") != null ? System.getenv("BASE_URL_SDK") : "https://eks-subscription-api-lumen-service.epayco.io";
    public static final String BASE_URL_SECURE = System.getenv("SECURE_URL_SDK") != null ? System.getenv("SECURE_URL_SDK") : "https://eks-rest-pagos-service.epayco.io";
    public static final String ENTORNO = System.getenv("ENTORNO_SDK") != null ? System.getenv("ENTORNO_SDK") : "/restpagos";
    public static final String BASE_URL_APIFY = System.getenv("BASE_URL_APIFY") != null ? System.getenv("BASE_URL_APIFY") : "https://eks-apify-service.epayco.io";

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
                    Log.e("epayco", "Error procesando respuesta: " + e.getMessage(), e);
                    callback.onError(e);
                }
            }
            @Override
            public void onError(Exception e) {
                Log.e("epayco", "Error en la solicitud, Token no encontrado: " + e.getMessage(), e);
                callback.onError(e);
            }
        };

        new Authentication().AuthService(apiKey, privateKey, new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                try {
                    token_bearer = data.getString("bearer_token");
                    String Base = base(false);
                    String jsonBody = hashMapFromCard(card);
                    if (token_bearer != null) {
                        post(Base + "/v1/tokens", jsonBody, token_bearer, new EpaycoCallback() {
                            @Override
                            public void onSuccess(JSONObject response) throws JSONException {
                                Log.d("epayco", "Respuesta de petición: " + response.toString());
                                callback.onSuccess(response);
                            }
                            @Override
                            public void onError(Exception e) {
                                Log.e("epayco", "Error en la solicitud: " + e.getMessage(), e);
                                callback.onError(e);
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.e("epayco", "Error en la solicitud: " + e.getMessage(), e);
                    callback.onError(e);
                }
            }
            @Override
            public void onError(Exception e) {
            Log.e("epayco", "Error en crear Token: " + (e != null ? e.getMessage() : "Error desconocido"), e);
            Token.onError(e);
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
                    Log.e("epayco", "Error procesando respuesta: " + e.getMessage(), e);
                    callback.onError(e);
                }
            }
            @Override
            public void onError(Exception e) {
                Log.e("epayco", "Error en la solicitud, cliente no creado: " + (e != null ? e.getMessage() : "Error desconocido"), e);
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
                        post(Base + "/payment/v1/customer/create", jsonBody, token_bearer, new EpaycoCallback() {
                            @Override
                            public void onSuccess(JSONObject response) throws JSONException {
                                callback.onSuccess(response);
                            }
                            @Override
                            public void onError(Exception e) {
                                Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
                                callback.onError(e);
                            }
                        });
                    } catch (Exception e) {
                        Log.e("epayco", "Excepción al crear cliente: " + e.getMessage(), e);
                        callback.onError(e);
                    }
                }
            }
            @Override
            public void onError(Exception e) {
                Log.e("epayco", "Error en autenticación: " + (e != null ? e.getMessage() : "Error desconocido"), e);
                Token.onError(e);
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
                    Log.e("epayco", "Error procesando respuesta: " + e.getMessage(), e);
                    callback.onError(e);
                }
            }
            @Override
            public void onError(Exception e) {
                Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
                callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey, privateKey, new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
            token_bearer = data.getString("bearer_token");
            String Base = base(false);
            if (token_bearer != null) {
                try {
                get(Base + "/payment/v1/customer/" + apiKey + "/" + uid, null, token_bearer, new EpaycoCallback() {
                    @Override
                    public void onSuccess(JSONObject response) throws JSONException {
                    callback.onSuccess(response);
                    }

                    @Override
                    public void onError(Exception e) {
                    Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
                    callback.onError(e);
                    }
                });
                } catch (Exception e) {
                Log.e("epayco", "Excepción al obtener cliente: " + (e != null ? e.getMessage() : "Error desconocido"), e);
                callback.onError(e);
                }
            }
            }

            @Override
            public void onError(Exception error) {
            Log.e("epayco", "Error en autenticación al obtener cliente: " + (error != null ? error.getMessage() : "Error desconocido"), error);
            Token.onError(error);
            }
        });
    }

    /**
     * Return list customer
     * @param callback    response request api
     */
    public void getCustomerList(@NonNull final ClientList clients, @NonNull final EpaycoCallback callback) {
        final EpaycoCallback Token = new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
            try {
                callback.onSuccess(data);
            } catch (JSONException e) {
                Log.e("epayco", "Error procesando respuesta: " + e.getMessage(), e);
                callback.onError(e);
            }
            }
            @Override
            public void onError(Exception e) {
            Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
            callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey, privateKey, new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
            token_bearer = data.getString("bearer_token");
            String Base = base(false);
            String jsonBody = hashMapFromCLients(clients);
            if (token_bearer != null) {
                try {
                get(Base + "/payment/v1/customers/" + apiKey, jsonBody, token_bearer, new EpaycoCallback() {
                    @Override
                    public void onSuccess(JSONObject response) throws JSONException {
                    callback.onSuccess(response);
                    }

                    @Override
                    public void onError(Exception e) {
                    Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
                    callback.onError(e);
                    }
                });
                } catch (Exception e) {
                Log.e("epayco", "Excepción al obtener lista de clientes: " + (e != null ? e.getMessage() : "Error desconocido"), e);
                callback.onError(e);
                }
            }
            }

            @Override
            public void onError(Exception error) {
            Log.e("epayco", "Error en autenticación al obtener lista de clientes: " + (error != null ? error.getMessage() : "Error desconocido"), error);
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
                Log.e("epayco", "Error procesando respuesta: " + e.getMessage(), e);
                callback.onError(e);
            }
            }
            @Override
            public void onError(Exception e) {
            Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
            callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey, privateKey, new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
            token_bearer = data.getString("bearer_token");
            String Base = base(false);
            String jsonBody = hashMapFromCLientDelete(client);
            if (token_bearer != null) {
                try {
                post(Base + "/v1/remove/token", jsonBody, token_bearer, new EpaycoCallback() {
                    @Override
                    public void onSuccess(JSONObject response) throws JSONException {
                    callback.onSuccess(response);
                    }
                    @Override
                    public void onError(Exception e) {
                    Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
                    callback.onError(e);
                    }
                });
                } catch (Exception e) {
                Log.e("epayco", "Excepción al eliminar token: " + e.getMessage(), e);
                callback.onError(e);
                }
            }
            }
            @Override
            public void onError(Exception error) {
            Log.e("epayco", "Error en autenticación al eliminar token: " + (error != null ? error.getMessage() : "Error desconocido"), error);
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
                Log.e("epayco", "Error procesando respuesta: " + e.getMessage(), e);
                callback.onError(e);
            }
            }
            @Override
            public void onError(Exception e) {
            Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
            callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey, privateKey, new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
            token_bearer = data.getString("bearer_token");
            String Base = base(false);
            String jsonBody = hashMapFromCLientCardDefault(client);
            if (token_bearer != null) {
                try {
                post(Base + "/payment/v1/customer/reasign/card/default", jsonBody, token_bearer, new EpaycoCallback() {
                    @Override
                    public void onSuccess(JSONObject response) throws JSONException {
                    
                    callback.onSuccess(response);
                    }

                    @Override
                    public void onError(Exception e) {
                    Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
                    callback.onError(e);
                    }
                });
                } catch (Exception e) {
                Log.e("epayco", "Excepción al reasignar tarjeta por defecto: " + e.getMessage(), e);
                callback.onError(e);
                }
            }
            }

            @Override
            public void onError(Exception error) {
            Log.e("epayco", "Error en autenticación al reasignar tarjeta por defecto: " + (error != null ? error.getMessage() : "Error desconocido"), error);
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
                    Log.d("epayco", "Respuesta exitosa: " + data.toString());
                    callback.onSuccess(data);
                } catch (JSONException e) {
                    Log.e("epayco", "Error procesando respuesta: " + e.getMessage(), e);
                    callback.onError(e);
                }
            }
            @Override
            public void onError(Exception e) {
                Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
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
                        post(Base + "/v1/customer/add/token", jsonBody, token_bearer, new EpaycoCallback() {
                            @Override
                            public void onSuccess(JSONObject response) throws JSONException {
                               
                                callback.onSuccess(response);
                            }
                            @Override
                            public void onError(Exception e) {
                                Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
                                callback.onError(e);
                            }
                        });
                    } catch (Exception e) {
                        Log.e("epayco", "Excepción al agregar nuevo token: " + e.getMessage(), e);
                        callback.onError(e);
                    }
                }
            }
            @Override
            public void onError(Exception error) {
                Log.e("epayco", "Error en autenticación al agregar nuevo token: " + (error != null ? error.getMessage() : "Error desconocido"), error);
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
        new Authentication().AuthService(apiKey, privateKey, new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
            token_bearer = data.getString("bearer_token");
            String Base = base(false);
            String jsonBody = hashMapFromPlan(plan);
            if (token_bearer != null) {
                try {
                post(Base + "/recurring/v1/plan/create", jsonBody, token_bearer, new EpaycoCallback() {
                    @Override
                    public void onSuccess(JSONObject response) throws JSONException {
                    callback.onSuccess(response);
                    }

                    @Override
                    public void onError(Exception e) {
                    Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
                    callback.onError(e);
                    }
                });
                } catch (Exception e) {
                Log.e("epayco", "Excepción al crear plan: " + e.getMessage(), e);
                callback.onError(e);
                }
            }
            }

            @Override
            public void onError(Exception error) {
            Log.e("epayco", "Error en autenticación al crear plan: " + (error != null ? error.getMessage() : "Error desconocido"), error);
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
                Log.e("epayco", "Error procesando respuesta: " + e.getMessage(), e);
                callback.onError(e);
            }
            }
            @Override
            public void onError(Exception e) {
            Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
            callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey, privateKey, new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
            token_bearer = data.getString("bearer_token");
            String Base = base(false);
            if (token_bearer != null) {
                try {
                get(Base + "/recurring/v1/plan/" + apiKey + "/" + uid, null, token_bearer, new EpaycoCallback() {
                    @Override
                    public void onSuccess(JSONObject response) throws JSONException {
                    callback.onSuccess(response);
                    }

                    @Override
                    public void onError(Exception e) {
                    Log.e("epayco", "Error al obtener plan: " + (e != null ? e.getMessage() : "Error desconocido"), e);
                    callback.onError(e);
                    }
                });
                } catch (Exception e) {
                Log.e("epayco", "Excepción al obtener plan: " + (e != null ? e.getMessage() : "Error desconocido"), e);
                callback.onError(e);
                }
            }
            }

            @Override
            public void onError(Exception error) {
            Log.e("epayco", "Error en autenticación al obtener plan: " + (error != null ? error.getMessage() : "Error desconocido"), error);
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
                Log.e("epayco", "Error procesando respuesta: " + e.getMessage(), e);
                callback.onError(e);
            }
            }
            @Override
            public void onError(Exception e) {
            Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
            callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey, privateKey, new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
            token_bearer = data.getString("bearer_token");
            String Base = base(false);
            String jsonBody = hashMapFromEmpty();
            if (token_bearer != null) {
                try {
                post(Base + "/recurring/v1/plan/remove/" + apiKey + "/" + uid, jsonBody, token_bearer, new EpaycoCallback() {
                    @Override
                    public void onSuccess(JSONObject response) throws JSONException {
                    Log.d("epayco", "Respuesta exitosa: " + response.toString());
                    callback.onSuccess(response);
                    }
                    @Override
                    public void onError(Exception e) {
                    Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
                    callback.onError(e);
                    }
                });
                } catch (Exception e) {
                Log.e("epayco", "Excepción al eliminar plan: " + e.getMessage(), e);
                callback.onError(e);
                }
            }
            }
            @Override
            public void onError(Exception error) {
            Log.e("epayco", "Error en autenticación al eliminar plan: " + (error != null ? error.getMessage() : "Error desconocido"), error);
            Token.onError(error);
            }
        });
    }

    /**
     * Return list plan
     *
     * @param callback response request api
     */
   public void getPlanList(@NonNull final EpaycoCallback callback) {
        final EpaycoCallback Token = new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
            try {
                callback.onSuccess(data);
            } catch (JSONException e) {
                Log.e("epayco", "Error procesando respuesta: " + e.getMessage(), e);
                callback.onError(e);
            }
            }
            @Override
            public void onError(Exception e) {
            Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
            callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey, privateKey, new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
            token_bearer = data.getString("bearer_token");
            String Base = base(false);
            if (token_bearer != null) {
                try {
                get(Base + "/recurring/v1/plans?public_key=", null, token_bearer, new EpaycoCallback() {
                    @Override
                    public void onSuccess(JSONObject response) throws JSONException {
                    callback.onSuccess(response);
                    }
                    @Override
                    public void onError(Exception e) {
                    Log.e("epayco", "Error al obtener lista de planes: " + (e != null ? e.getMessage() : "Error desconocido"), e);
                    callback.onError(e);
                    }
                });
                } catch (Exception e) {
                Log.e("epayco", "Excepción al obtener lista de planes: " + (e != null ? e.getMessage() : "Error desconocido"), e);
                callback.onError(e);
                }
            }
            }
            @Override
            public void onError(Exception error) {
            Log.e("epayco", "Error en autenticación al obtener lista de planes: " + (error != null ? error.getMessage() : "Error desconocido"), error);
            Token.onError(error);
            }
        });
    }

    /**
     * Update plan
     * @param plan        data model plan
     * @param callback    response request api
     */
    public void updatePlan(@NonNull final String uid, @NonNull final PlanUpdate plan, @NonNull final EpaycoCallback callback) {
        final EpaycoCallback Token = new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                try {
                    callback.onSuccess(data);
                } catch (JSONException e) {
                    Log.e("epayco", "Error procesando respuesta: " + e.getMessage(), e);
                    callback.onError(e);
                }
            }
            @Override
            public void onError(Exception e) {
                Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
                callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey, privateKey, new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                token_bearer = data.getString("bearer_token");
                String Base = base(false);
                String jsonBody = hashMapFromPlanUpdate(plan);
                if (token_bearer != null) {
                    try {
                        post(Base + "/recurring/v1/plan/edit/" + uid, jsonBody, token_bearer, new EpaycoCallback() {
                            @Override
                            public void onSuccess(JSONObject response) throws JSONException {
                                Log.d("epayco", "Respuesta de actualización de plan: " + response.toString());
                                callback.onSuccess(response);
                            }
                            @Override
                            public void onError(Exception e) {
                                Log.e("epayco", "Error en la actualización de plan: " + (e != null ? e.getMessage() : "Error desconocido"), e);
                                callback.onError(e);
                            }
                        });
                    } catch (Exception e) {
                        Log.e("epayco", "Excepción al actualizar plan: " + e.getMessage(), e);
                        callback.onError(e);
                    }
                }
            }
            @Override
            public void onError(Exception error) {
                Log.e("epayco", "Error en autenticación al actualizar plan: " + (error != null ? error.getMessage() : "Error desconocido"), error);
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
                Log.e("epayco", "Error procesando respuesta: " + e.getMessage(), e);
                callback.onError(e);
            }
            }
            @Override
            public void onError(Exception e) {
            Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
            callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey, privateKey, new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
            token_bearer = data.getString("bearer_token");
            String Base = base(false);
            String jsonBody = hashMapFromSub(sub);
            if (token_bearer != null) {
                try {
                post(Base + "/recurring/v1/subscription/create", jsonBody, token_bearer, new EpaycoCallback() {
                    @Override
                    public void onSuccess(JSONObject response) throws JSONException {
                   
                    callback.onSuccess(response);
                    }
                    @Override
                    public void onError(Exception e) {
                    Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
                    callback.onError(e);
                    }
                });
                } catch (Exception e) {
                Log.e("epayco", "Excepción al crear suscripción: " + e.getMessage(), e);
                callback.onError(e);
                }
            }
            }
            @Override
            public void onError(Exception error) {
            Log.e("epayco", "Error en autenticación al crear suscripción: " + (error != null ? error.getMessage() : "Error desconocido"), error);
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
                Log.e("epayco", "Error procesando respuesta: " + e.getMessage(), e);
                callback.onError(e);
            }
            }
            @Override
            public void onError(Exception e) {
            Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
            callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey, privateKey, new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
            token_bearer = data.getString("bearer_token");
            String Base = base(false);
            if (token_bearer != null) {
                try {
                get(Base + "/recurring/v1/subscription/" + uid + "/" + apiKey, null, token_bearer, new EpaycoCallback() {
                    @Override
                    public void onSuccess(JSONObject response) throws JSONException {
                   
                    callback.onSuccess(response);
                    }

                    @Override
                    public void onError(Exception e) {
                    Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
                    callback.onError(e);
                    }
                });
                } catch (Exception e) {
                Log.e("epayco", "Excepción al obtener suscripción: " + (e != null ? e.getMessage() : "Error desconocido"), e);
                callback.onError(e);
                }
            }
            }

            @Override
            public void onError(Exception error) {
            Log.e("epayco", "Error en autenticación al obtener suscripción: " + (error != null ? error.getMessage() : "Error desconocido"), error);
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
                Log.e("epayco", "Error procesando respuesta: " + e.getMessage(), e);
                callback.onError(e);
            }
            }
            @Override
            public void onError(Exception e) {
            Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
            callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey, privateKey, new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
            token_bearer = data.getString("bearer_token");
            String Base = base(false);
            if (token_bearer != null) {
                try {
                get(Base + "/recurring/v1/subscriptions/" + apiKey, null, token_bearer, new EpaycoCallback() {
                    @Override
                    public void onSuccess(JSONObject response) throws JSONException {
                   
                    callback.onSuccess(response);
                    }

                    @Override
                    public void onError(Exception e) {
                    Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
                    callback.onError(e);
                    }
                });
                } catch (Exception e) {
                Log.e("epayco", "Excepción al obtener lista de suscripciones: " + (e != null ? e.getMessage() : "Error desconocido"), e);
                callback.onError(e);
                }
            }
            }

            @Override
            public void onError(Exception error) {
            Log.e("epayco", "Error en autenticación al obtener lista de suscripciones: " + (error != null ? error.getMessage() : "Error desconocido"), error);
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
                Log.e("epayco", "Error procesando respuesta: " + e.getMessage(), e);
                callback.onError(e);
            }
            }
            @Override
            public void onError(Exception e) {
            Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
            callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey, privateKey, new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
            token_bearer = data.getString("bearer_token");
            String Base = base(false);
            String jsonBody = hashMapFromSubCharge(sub);
            if (token_bearer != null) {
                try {
                post(Base + "/payment/v1/charge/subscription/create", jsonBody, token_bearer, new EpaycoCallback() {
                    @Override
                    public void onSuccess(JSONObject response) throws JSONException {
                   
                    callback.onSuccess(response);
                    }
                    @Override
                    public void onError(Exception e) {
                    Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
                    callback.onError(e);
                    }
                });
                } catch (Exception e) {
                Log.e("epayco", "Excepción al hacer el cargo a la suscripción: " + e.getMessage(), e);
                callback.onError(e);
                }
            }
            }
            @Override
            public void onError(Exception error) {
            Log.e("epayco", "Error en autenticación al hacer cargo a suscripción: " + (error != null ? error.getMessage() : "Error desconocido"), error);
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
                Log.e("epayco", "Error procesando respuesta: " + e.getMessage(), e);
                callback.onError(e);
            }
            }
            @Override
            public void onError(Exception e) {
            Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
            callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey, privateKey, new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
            token_bearer = data.getString("bearer_token");
            String Base = base(false);
            String jsonBody = hashMapFromSubCancel(uid);
            if (token_bearer != null) {
                try {
                post(Base + "/recurring/v1/subscription/cancel", jsonBody, token_bearer, new EpaycoCallback() {
                    @Override
                    public void onSuccess(JSONObject response) throws JSONException {
                    
                    callback.onSuccess(response);
                    }
                    @Override
                    public void onError(Exception e) {
                    Log.e("epayco", "Error en la cancelación de suscripción: " + (e != null ? e.getMessage() : "Error desconocido"), e);
                    callback.onError(e);
                    }
                });
                } catch (Exception e) {
                Log.e("epayco", "Excepción al cancelar suscripción: " + e.getMessage(), e);
                callback.onError(e);
                }
            }
            }
            @Override
            public void onError(Exception error) {
            Log.e("epayco", "Error en autenticación al cancelar suscripción: " + (error != null ? error.getMessage() : "Error desconocido"), error);
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
                    Log.e("epayco", "Error procesando respuesta: " + e.getMessage(), e);
                    callback.onError(e);
                }
            }
            @Override
            public void onError(Exception e) {
                Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
                callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey, privateKey, new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
            token_bearer = data.getString("bearer_token");
            String Base = base(true);
            String jsonBody = hashMapFromPse(pse, buildOptions());
            if (token_bearer != null) {
                try {
                post(Base + "/pagos/debitos.json", jsonBody, token_bearer, new EpaycoCallback() {
                    @Override
                    public void onSuccess(JSONObject response) throws JSONException {
                    callback.onSuccess(response);
                    }

                    @Override
                    public void onError(Exception e) {
                    Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
                    callback.onError(e);
                    }
                });
                } catch (Exception e) {
                Log.e("epayco", "Excepción al crear transacción PSE: " + e.getMessage(), e);
                callback.onError(e);
                }
            }
            }

            @Override
            public void onError(Exception error) {
            Log.e("epayco", "Error en autenticación al crear transacción PSE: " + (error != null ? error.getMessage() : "Error desconocido"), error);
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
                Log.e("epayco", "Error procesando respuesta: " + e.getMessage(), e);
                callback.onError(e);
            }
            }
            @Override
            public void onError(Exception e) {
            Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
            callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                token_bearer = data.getString("bearer_token");
                String Base = base(true);
                if (token_bearer != null) {
                    try {
                        get(Base + "/pse/transactioninfomation.json?transactionID=" + uid + "&public_key=" + apiKey, null, token_bearer, new EpaycoCallback() {
                            @Override
                            public void onSuccess(JSONObject response) throws JSONException {
                              
                                callback.onSuccess(response);
                            }
                            @Override
                            public void onError(Exception e) {
                                Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
                                callback.onError(e);
                            }
                        });
                    } catch (Exception e) {
                        Log.e("epayco", "Excepción al obtener transacción PSE: " + e.getMessage(), e);
                        callback.onError(e);
                    }
                }
            }
            @Override
            public void onError(Exception error) {
                Log.e("epayco", "Error en autenticación al obtener transacción PSE: " + (error != null ? error.getMessage() : "Error desconocido"), error);
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
            public void onSuccess(JSONObject data) {
                try {
                    callback.onSuccess(data);

                } catch (JSONException e) {
                    Log.e("epayco", "Error al formatear JSON", e);
                }
            }
            @Override
            public void onError(Exception error) {
                Log.e("epayco", "Error al obtener bancos", error);
            }
        };
        new Authentication().AuthService(apiKey, privateKey, new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                token_bearer = data.getString("bearer_token");
                String Base = base(true);
                boolean strTest = test ? Boolean.TRUE : Boolean.FALSE;
                String testStr = apiKey + "&test=" + strTest;
                if (token_bearer != null) {
                    try {
                        get(Base + "/pse/bancos.json?public_key=" + testStr, null, token_bearer, new EpaycoCallback() {

                            @Override
                            public void onSuccess(JSONObject response) throws JSONException {

                                callback.onSuccess(response);
                            }
                            @Override
                            public void onError(Exception e) {
                                Log.e("epayco", "Error en la solicitud de bancos: " + (e != null ? e.getMessage() : "Error desconocido"), e);
                                callback.onError(e);
                            }
                        });
                    } catch (Exception e) {
                        Log.e("epayco", "Excepción al obtener bancos: " + e.getMessage(), e);
                        callback.onError(e);
                    }
                }
            }
            @Override
            public void onError(Exception error) {
                Log.e("epayco", "Error en autenticación al obtener bancos: " + (error != null ? error.getMessage() : "Error desconocido"), error);
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
                Log.e("epayco", "Error procesando respuesta: " + e.getMessage(), e);
                callback.onError(e);
            }
            }
            @Override
            public void onError(Exception e) {
            Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
            callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey, privateKey, new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
            token_bearer = data.getString("bearer_token");
            String Base = base(true);
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
                Log.e("epayco", "Método de pago en efectivo no soportado: " + cash.getType());
                return;
            }
            if (token_bearer != null) {
                try {
                post(Base + url, jsonBody, token_bearer, new EpaycoCallback() {
                    @Override
                    public void onSuccess(JSONObject response) throws JSONException {
                   
                    callback.onSuccess(response);
                    }
                    @Override
                    public void onError(Exception e) {
                    Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
                    callback.onError(e);
                    }
                });
                } catch (Exception e) {
                Log.e("epayco", "Excepción al crear transacción cash: " + e.getMessage(), e);
                callback.onError(e);
                }
            }
            }
            @Override
            public void onError(Exception error) {
            Log.e("epayco", "Error en autenticación al crear transacción cash: " + (error != null ? error.getMessage() : "Error desconocido"), error);
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
                Log.e("epayco", "Error procesando respuesta: " + e.getMessage(), e);
                callback.onError(e);
            }
            }
            @Override
            public void onError(Exception e) {
            Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
            callback.onError(e);
            }
        };
        new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                token_bearer = data.getString("bearer_token");
                String Base = base(false);
                if (token_bearer != null) {
                    try {
                        get(Base + "/transaction/response.json?ref_payco=" + uid + "&public_key=" + apiKey, null, token_bearer, new EpaycoCallback() {
                            @Override
                            public void onSuccess(JSONObject response) throws JSONException {
                                Log.d("epayco", "Respuesta exitosa: " + response.toString());
                                callback.onSuccess(response);
                            }
                            @Override
                            public void onError(Exception e) {
                                Log.e("epayco", "Error en la respuesta: " + (e != null ? e.getMessage() : "Error desconocido"), e);
                                callback.onError(e);
                            }
                        });
                    } catch (Exception e) {
                        Log.e("epayco", "Excepción al obtener referencia de pago: " + e.getMessage(), e);
                        callback.onError(e);
                    }
                }
            }
            @Override
            public void onError(Exception error) {
                Log.e("epayco", "Error en autenticación al obtener referencia de pago: " + (error != null ? error.getMessage() : "Error desconocido"), error);
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
                Log.e("epayco", "Error procesando respuesta: " + e.getMessage(), e);
                callback.onError(e);
            }
            }
            @Override
            public void onError(Exception e) {
            Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
            callback.onError(e);
            }
        };
        try {
            new Authentication().AuthService(apiKey, privateKey, new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                token_bearer = data.getString("bearer_token");
                String Base = base(false);
                String jsonBody = hashMapFromCharge(charge);
                if (token_bearer != null) {
                try {
                    post(Base + "/payment/v1/charge/create", jsonBody, token_bearer, new EpaycoCallback() {
                    @Override
                    public void onSuccess(JSONObject response) throws JSONException {
                       
                        if (response.has("status") && response.getBoolean("status")) {
                        callback.onSuccess(data);
                        } else {
                        Log.e("epayco", "Respuesta con error: " + response.toString());
                        }
                        callback.onSuccess(response);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
                        callback.onError(e);
                    }
                    });
                } catch (Exception e) {
                    Log.e("epayco", "Excepción al crear cargo: " + e.getMessage(), e);
                    callback.onError(e);
                }
                }
            }

            @Override
            public void onError(Exception error) {
                Log.e("epayco", "Error en autenticación al crear pago: " + (error != null ? error.getMessage() : "Error desconocido"), error);
                Token.onError(error);
            }
            });
        } catch (Exception ex) {
            Log.e("epayco", "Excepción general en createCharge: " + ex.getMessage(), ex);
            callback.onError(ex);
        }
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
                    Log.e("epayco", "Error procesando respuesta: " + e.getMessage(), e);
                    callback.onError(e);
                }
            }
            @Override
            public void onError(Exception e) {
                try {
                    System.err.println("Error en la solicitud: " + e.getMessage());
                    if (e instanceof IOException) {
                        Log.e("epayco", "Error de red o servidor: " + e.getMessage(), e);
                        callback.onError(new Exception("Error de red o servidor: " + e.getMessage(), e));
                    } else if (e instanceof JSONException) {
                        Log.e("epayco", "Error procesando la respuesta del servidor: " + e.getMessage(), e);
                        callback.onError(new Exception("Error procesando la respuesta del servidor: " + e.getMessage(), e));
                    } else {
                        Log.e("epayco", "Error desconocido: " + e.getMessage(), e);
                        callback.onError(e);
                    }
                } catch (Exception ex) {
                    Log.e("epayco", "Excepción en onError: " + ex.getMessage(), ex);
                    callback.onError(ex);
                }
            }
        };
        new Authentication().AuthServiceApify(apiKey, privateKey, new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                try { 
                    callback.onSuccess(data);
                    token_bearer = data.getString("token");
                    String BaseApi = baseApify(true);
                    String jsonBody = hashMapFromDaviplata(daviplata);
                    if (token_bearer != null) {
                        try {
                            post(BaseApi + "/payment/process/daviplata", jsonBody, token_bearer, callback);
                        } catch (Exception e) {
                            Log.e("epayco", "Error al hacer POST Daviplata: " + e.getMessage(), e);
                            callback.onError(e);
                        }
                    }
                } catch (Exception ex) {
                    Log.e("epayco", "Excepción en onSuccess Daviplata: " + ex.getMessage(), ex);
                    callback.onError(ex);
                }
            }
            @Override
            public void onError(Exception error) {
                try {
                    Log.e("epayco", "Error en autenticación Daviplata: " + (error != null ? error.getMessage() : "Error desconocido"), error);
                    Token.onError(error);
                } catch (Exception ex) {
                    Log.e("epayco", "Excepción en onError autenticación Daviplata: " + ex.getMessage(), ex);
                    Token.onError(ex);
                }
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
                Log.e("epayco", "Error procesando respuesta: " + e.getMessage(), e);
                callback.onError(e);
            } catch (Exception ex) {
                Log.e("epayco", "Excepción inesperada en onSuccess: " + ex.getMessage(), ex);
                callback.onError(ex);
            }
            }

            @Override
            public void onError(Exception e) {
            try {
                Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
                if (e instanceof IOException) {
                callback.onError(new Exception("Error de red o servidor: " + e.getMessage(), e));
                } else if (e instanceof JSONException) {
                callback.onError(new Exception("Error procesando la respuesta del servidor: " + e.getMessage(), e));
                } else {
                callback.onError(e);
                }
            } catch (Exception ex) {
                Log.e("epayco", "Excepción inesperada en onError: " + ex.getMessage(), ex);
                callback.onError(ex);
            }
            }
        };

        new Authentication().AuthServiceApify(apiKey, privateKey, new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
            try {
                token_bearer = data.getString("bearer_token");
                String BaseApi = baseApify(true);
                String jsonBody = hashMapFromDaviplataConfirm(confirm);
                if (token_bearer != null) {
                post(BaseApi + "/payment/confirm/daviplata", jsonBody, token_bearer, new EpaycoCallback() {
                    @Override
                    public void onSuccess(JSONObject response) throws JSONException {
                    try {
                        if (response.has("status") && response.getBoolean("status")) {
                          callback.onSuccess(data);
                        } else {
                        Log.e("epayco", "Error en confirmación Daviplata: " + response.toString());
                        }
                        callback.onSuccess(response);
                    } catch (Exception ex) {
                        Log.e("epayco", "Excepción en onSuccess confirmación Daviplata: " + ex.getMessage(), ex);
                        callback.onError(ex);
                    }
                    }

                    @Override
                    public void onError(Exception e) {
                    Log.e("epayco", "Error en la solicitud de confirmación Daviplata: " + (e != null ? e.getMessage() : "Error desconocido"), e);
                    callback.onError(e);
                    }
                });
                }
            } catch (Exception e) {
                Log.e("epayco", "Excepción al procesar confirmación Daviplata: " + e.getMessage(), e);
                callback.onError(e);
            }
            }

            @Override
            public void onError(Exception error) {
            try {
                Log.e("epayco", "Error en autenticación confirmación Daviplata: " + (error != null ? error.getMessage() : "Error desconocido"), error);
                Token.onError(error);
            } catch (Exception ex) {
                Log.e("epayco", "Excepción en onError autenticación confirmación Daviplata: " + ex.getMessage(), ex);
                Token.onError(ex);
            }
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
                Log.e("epayco", "Error procesando respuesta: " + e.getMessage(), e);
                callback.onError(e);
            }
            }
            @Override
            public void onError(Exception e) {
            Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
            callback.onError(e);
            }
        };
        new Authentication().AuthServiceApify(apiKey, privateKey, new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
            token_bearer = data.getString("token");
            String Base = base(false);
            String jsonBody = hashMapFromSafetypay(safetypay);
            if (token_bearer != null) {
                try {
                post(BASE_URL_APIFY + "/payment/process/safetypay", jsonBody, token_bearer, new EpaycoCallback() {
                    @Override
                    public void onSuccess(JSONObject response) throws JSONException {
                   
                    callback.onSuccess(response);
                    }
                    @Override
                    public void onError(Exception e) {
                    Log.e("epayco", "Error en la solicitud: " + (e != null ? e.getMessage() : "Error desconocido"), e);
                    callback.onError(e);
                    }
                });
                } catch (Exception e) {
                Log.e("epayco", "Excepción al procesar Safetypay: " + e.getMessage(), e);
                callback.onError(e);
                }
            }
            }
            @Override
            public void onError(Exception error) {
            Log.e("epayco", "Error en autenticación Safetypay: " + (error != null ? error.getMessage() : "Error desconocido"), error);
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
    public void get(final String urlString,final String jsonBody ,final String token,final EpaycoCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {

                    StringBuilder urlWithParams = new StringBuilder(urlString);
                    //connection.setDoOutput(true);
                    // Escribir el cuerpo en la solicitud
                    if (jsonBody != null && !jsonBody.isEmpty()) {
                        urlWithParams.append("?");
                        JSONObject jsonObject = new JSONObject(jsonBody);
                        urlWithParams.append(URLEncoder.encode("page", "UTF-8"))
                                .append("=")
                                .append(URLEncoder.encode(jsonObject.get("page").toString(), "UTF-8"))
                                .append("&");
                        urlWithParams.append(URLEncoder.encode("perPage", "UTF-8"))
                                .append("=")
                                .append(URLEncoder.encode(jsonObject.get("perPage").toString(), "UTF-8"))
                                .append("&");
                        //Eliminar el último "&"
                        urlWithParams.setLength(urlWithParams.length() - 1);
                    }
                    // Configurar la URL de destino
                    //URL url = new URL(urlString);
                    URL url = new URL(urlWithParams.toString());

                    // Abrir la conexión HTTP
                    connection = (HttpURLConnection) url.openConnection();

                    // Configurar la conexión
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Authorization", "Bearer " + token);
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
                            if (response.has("status") && !response.getBoolean("status")) {
                                Log.e("epaycoError", "Error validando datos: " + response.toString());
                            } else if (response.has("status") && response.get("status").equals("error")) {
                                Log.e("epaycoError", "Error validando datos: " + response.toString());
                            } else {
                                Log.d("epaycoResponse", response.toString());
                            }
                            callback.onSuccess(response);
                        }
                    } else {
                        System.err.println("Error: " + responseCode + " " + connection.getResponseMessage());
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "utf-8"));
                        StringBuilder errorResponse = new StringBuilder();
                        String errorLine;
                        while ((errorLine = br.readLine()) != null) {
                            errorResponse.append(errorLine.trim());
                        }
                        System.err.println("Detalles del error: " + errorResponse.toString());
                        // Llamar al método onError del Callback en caso de error
                        callback.onError(new IOException(errorResponse.toString()));
                    }
                } catch (Exception e) {
                    // Llamar al método onError del Callback en caso de excepción
                    String errorMsg = (e.getMessage() != null) ? e.getMessage() : "Error desconocido al realizar la petición GET";
                    Log.e("epaycoError", errorMsg, e);
                    callback.onError(new Exception("Error en la petición GET: " + errorMsg, e));
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
                            if (response.has("status") && !response.getBoolean("status")) {
                                Log.e("epaycoError", "Error validando datos: " + response.toString());
                            } else if (response.has("status") && response.get("status").equals("error")) {
                                Log.e("epaycoError", "Error validando datos: " + response.toString());
                            } else {
                                Log.d("epaycoResponse", response.toString());
                            }
                            callback.onSuccess(response);
                        }catch (Exception e) {
                            // Llamar al método onError del Callback en caso de excepción
                            Log.e("epaycoError",e.getMessage());
                            callback.onError(e);
                        }
                    } else {
                        System.err.println("Error: " + responseCode + " " + connection.getResponseMessage());
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "utf-8"));
                        StringBuilder errorResponse = new StringBuilder();
                        String errorLine;
                        while ((errorLine = br.readLine()) != null) {
                            errorResponse.append(errorLine.trim());
                        }
                        System.err.println("Detalles del error: " + errorResponse.toString());
                        // Llamar al método onError del Callback en caso de error
                        callback.onError(new IOException(errorResponse.toString()));
                    }
                } catch (Exception e) {
                    // Llamar al método onError del Callback en caso de excepción
                    Log.e("epaycoError",e.getMessage());
                    callback.onError(e);
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }

            }
        }).start();
    }
    public String base (boolean isApi) {
        if (isApi) {
            return BASE_URL_SECURE + ENTORNO;
        } else {
            return BASE_URL;
        }
    }

    public String baseApify (boolean isApi) {
        if (isApi) {
            return BASE_URL_APIFY;
        } else {
            return "No se encontro url";
        }
    }
}