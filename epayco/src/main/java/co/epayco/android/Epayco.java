package co.epayco.android;

import android.support.annotation.NonNull;

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
import cz.msebera.android.httpclient.Header;

import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromCLient;
import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromCard;
import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromCash;
import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromDaviplataConfirm;
import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromPlan;
import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromSafetypay;
import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromSub;
import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromSubCharge;
import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromPse;
import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromCharge;
import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromCLientDelete;
import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromCLientCardDefault;
import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromCLientCardNew;
import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromDaviplata;

public class Epayco {

    private static AsyncHttpClient client = new AsyncHttpClient();

    private static final String BASE_URL = "https://api.secure.payco.co";
    private static final String BASE_URL_SECURE = "https://secure.payco.co";
    private static final String BASE_URL_APIFY = "https://apify.epayco.co";

    private static final int MAX_TIME_OUT= 190*10000;

    private String apiKey;
    private String privateKey;
    private String lang;
    private Boolean test;
    String token_bearer;
    String token_bearer2;

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
    // public void createToken(@NonNull Card card, @NonNull EpaycoCallback callback) {
    //     String Base = base(false);
    //     try {
    //         post(Base + "/v1/tokens", hashMapFromCard(card), apiKey, callback);
    //     } catch (Exception e) {
    //         callback.onError(e);
    //     }
    // }
     public void createToken(@NonNull final Card card, @NonNull final EpaycoCallback callback) {

        Epayco epayco = new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){

            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                String projectnumber1 = data.getString("bearer_token");
                token_bearer2 = projectnumber1;
                token_bearer = "Bearer " + projectnumber1;
               // Log.d("createToken","=>"+token_bearer);
                String Base = base(false);
                if(token_bearer2 != null){
                    try {
                        post(Base + "/v1/tokens", hashMapFromCard(card), token_bearer, callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }

            @Override
            public void onError(Exception error) {
                Log.d("bearer_token","=>"+error);
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

        Epayco epayco = new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){

            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                String projectnumber1 = data.getString("bearer_token");
                token_bearer2 = projectnumber1;
                token_bearer = "Bearer " + projectnumber1;
               // Log.d("createCutomer","=>"+token_bearer);
                String Base = base(false);
                if(token_bearer2 != null){
                    try {
                        post(Base + "/payment/v1/customer/create", hashMapFromCLient(client), token_bearer, callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }

            @Override
            public void onError(Exception error) {
                Log.d("bearer_token","=>"+error);
            }
        });

    }

    /**
     * Retrieve customer from id
     * @param uid      id customer
     * @param callback response request api
     */

    public void getCustomer(final String uid, @NonNull final EpaycoCallback callback) {
       
        Epayco epayco = new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){

            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                String projectnumber1 = data.getString("bearer_token");
                token_bearer2 = projectnumber1;
                token_bearer = "Bearer " + projectnumber1;
                // Log.d("getCustomer","=>"+token_bearer);
                String Base = base(false);
                if(token_bearer2 != null){
                    try {
                        get(Base + "/payment/v1/customer/" + apiKey + "/" + uid,token_bearer, callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }

            @Override
            public void onError(Exception error) {
                Log.d("bearer_token","=>"+error);
            }
        });

    }

    /**
      * Return list customer
      * @param callback    response request api
     */
    public void getCustomerList(@NonNull final EpaycoCallback callback) {
          Epayco epayco = new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){

            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                String projectnumber1 = data.getString("bearer_token");
                token_bearer2 = projectnumber1;
                token_bearer = "Bearer " + projectnumber1;
               //  Log.d("getCustomerList","=>"+token_bearer);
                String Base = base(false);
               if(token_bearer2 != null){
                    try {
                        get(Base + "/payment/v1/customers/" + apiKey + "/",token_bearer, callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
           }      
           
           @Override
            public void onError(Exception error) {
                Log.d("bearer_token","=>"+error);
            }
           
           
          });
    }


  /**
     * delete customer token
     * @param client    
     * @param callback
     */
    public void deleteTokenCustomer(@NonNull final Client client, @NonNull final EpaycoCallback callback) {
        Epayco epayco = new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){

            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                String projectnumber1 = data.getString("bearer_token");
                token_bearer2 = projectnumber1;
                token_bearer = "Bearer " + projectnumber1;
             //   Log.d("deleteTokenCustomer", "=>" + token_bearer);
                String Base = base(false);
                if (token_bearer2 != null) {
                    try {
                        post(Base + "/v1/remove/token", hashMapFromCLientDelete(client), token_bearer, callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
            @Override
            public void onError(Exception error) {
                Log.d("deleteTokenCustomer","=>"+error);
            }
        });

    }

  /**
     * add default token card exited
     * @param client    
     * @param callback
     */
    public void addTokenDefault(@NonNull final Client client, @NonNull final EpaycoCallback callback) {
        Epayco epayco = new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){

            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                String projectnumber1 = data.getString("bearer_token");
                token_bearer2 = projectnumber1;
                token_bearer = "Bearer " + projectnumber1;
              //  Log.d("addTokenDefault", "=>" + token_bearer);
                String Base = base(false);
                if (token_bearer2 != null) {
                    try {
                        post(Base + "/payment/v1/customer/reasign/card/default", hashMapFromCLientCardDefault(client), token_bearer, callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }

            @Override
            public void onError(Exception error) {
                Log.d("bearer_token","=>"+error);
            }
        });
    }
      /**
     * add new token card
     * @param client    
     * @param callback
     */
     public void addNewToken(@NonNull final Client client, @NonNull final EpaycoCallback callback) {
        Epayco epayco = new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                String projectnumber1 = data.getString("bearer_token");
                token_bearer2 = projectnumber1;
                token_bearer = "Bearer " + projectnumber1;
              //   Log.d("addNewToken","=>"+token_bearer);
                String Base = base(false);
                if(token_bearer2 != null){
                    try {
                        post(Base + "/v1/customer/add/token", hashMapFromCLientCardNew(client), token_bearer, callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }

            }
            @Override
            public void onError(Exception error) {
                Log.d("bearer_token","=>"+error);
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
        Epayco epayco = new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){

            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                String projectnumber1 = data.getString("bearer_token");
                token_bearer2 = projectnumber1;
                token_bearer = "Bearer " + projectnumber1;
            //    Log.d("createPlan","=>"+token_bearer);
        String Base = base(false);
                if(token_bearer2 != null){
                    try {
                        post(Base + "/recurring/v1/plan/create", hashMapFromPlan(plan), token_bearer, callback);
                        } catch (Exception e) {
                         callback.onError(e);
                         }
                     }
        }

        @Override
        public void onError(Exception error) {
            Log.d("bearer_token","=>"+error);
        }
    });
    }

    /**
     * Retrieve plan from id
     * @param uid         id plan
     * @param callback    response request api
     */
    public void getPlan(final String uid, @NonNull final EpaycoCallback callback) {
         Epayco epayco = new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){
               @Override
            public void onSuccess(JSONObject data) throws JSONException {
                  String projectnumber1 = data.getString("bearer_token");
                token_bearer2 = projectnumber1;
                token_bearer = "Bearer " + projectnumber1;
              //   Log.d("getPlan","=>"+token_bearer);
                String Base = base(false);
                   if(token_bearer2 != null){
                    try {
                        get(Base + "/recurring/v1/plan/" + apiKey + "/" + uid,token_bearer, callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }

            }
             @Override
            public void onError(Exception error) {
                Log.d("bearer_token","=>"+error);
            }

          });
    }

    public void deletePlan(final String uid, @NonNull final EpaycoCallback callback) {
        Epayco epayco = new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                String projectnumber1 = data.getString("bearer_token");
                token_bearer2 = projectnumber1;
                token_bearer = "Bearer " + projectnumber1;
                //   Log.d("getPlan","=>"+token_bearer);
                String Base = base(false);
                if(token_bearer2 != null){
                    try {
                        post(Base + "/recurring/v1/plan/remove/" + apiKey + "/" + uid, null, token_bearer, callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }

            }
            @Override
            public void onError(Exception error) {
                Log.d("bearer_token","=>"+error);
            }

        });
    }

    /**
     * Return list plan
     * @param callback    response request api
     */
    public void getPlanList(@NonNull final EpaycoCallback callback) {
        Epayco epayco = new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){
               @Override
            public void onSuccess(JSONObject data) throws JSONException {
                  String projectnumber1 = data.getString("bearer_token");
                token_bearer2 = projectnumber1;
                token_bearer = "Bearer " + projectnumber1;
             //    Log.d("getPlanList","=>"+token_bearer);
        String Base = base(false);
         if(token_bearer2 != null){
        try {
            get(Base + "/recurring/v1/plans/" + apiKey + "/",token_bearer, callback);
            } catch (Exception e) {
            callback.onError(e);
            }
         }

            }  @Override
            public void onError(Exception error) {
                Log.d("bearer_token","=>"+error);
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
    public void createSubscription(final Subscription sub, @NonNull final EpaycoCallback callback) {
        Epayco epayco = new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){

            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                String projectnumber1 = data.getString("bearer_token");
                token_bearer2 = projectnumber1;
                token_bearer = "Bearer " + projectnumber1;
             //   Log.d("createSubscription","=>"+token_bearer);
             String Base = base(false);
                if(token_bearer2 != null) {
                    try {
                        post(Base + "/recurring/v1/subscription/create", hashMapFromSub(sub), token_bearer, callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
    }

    @Override
    public void onError(Exception error) {
        Log.d("bearer_token","=>"+error);
    }
});
    }

    /**
     * Retrieve subscription from id
     * @param uid         id subscription
     * @param callback    response request api
     */
    public void getSubscription(final String uid, @NonNull final EpaycoCallback callback) {
         Epayco epayco = new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){
               @Override
            public void onSuccess(JSONObject data) throws JSONException {
                  String projectnumber1 = data.getString("bearer_token");
                token_bearer2 = projectnumber1;
                token_bearer = "Bearer " + projectnumber1;
             //    Log.d("getSubscription","=>"+token_bearer);
                 String Base = base(false);
               if(token_bearer2 != null){
            try {
             get(Base + "/recurring/v1/subscription/" + uid + "/" + apiKey,token_bearer, callback);
          } catch (Exception e) {
                callback.onError(e);
            }
         }
            }
             @Override
            public void onError(Exception error) {
                Log.d("bearer_token","=>"+error);
            }

           });
    }

    /**
     * Get list subscription
     * @param callback    response request api
     */
    public void getSubscriptionList(@NonNull final EpaycoCallback callback) {
         Epayco epayco = new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){
               @Override
            public void onSuccess(JSONObject data) throws JSONException {
                  String projectnumber1 = data.getString("bearer_token");
                token_bearer2 = projectnumber1;
                token_bearer = "Bearer " + projectnumber1;
             //    Log.d("getSubscriptionList","=>"+token_bearer);
        String Base = base(false);
                if(token_bearer2 != null){
             try {
                  get(Base + "/recurring/v1/subscriptions/" + apiKey + "/",token_bearer, callback);
                } catch (Exception e) {
                    callback.onError(e);
                 }
         }
    
           }     @Override
            public void onError(Exception error) {
                Log.d("bearer_token","=>"+error);
            }

           });
    }

    /**
     * Charge subscription ans subscribe plan
     * @param sub         data model charge subscription
     * @param callback    response request api
     */
      public void chargeSubscription(final ChargeSub sub, @NonNull final EpaycoCallback callback) {
        Epayco epayco = new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                String projectnumber1 = data.getString("bearer_token");
                token_bearer2 = projectnumber1;
                token_bearer = "Bearer " + projectnumber1;
           //     Log.d("chargeSubscription","=>"+token_bearer);
        String Base = base(false);
                if(token_bearer2 != null) {
                    try {
                        post(Base + "/payment/v1/charge/subscription/create", hashMapFromSubCharge(sub), token_bearer, callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
    }
    @Override
    public void onError(Exception error) {
        Log.d("bearer_token","=>"+error);
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
    public void createPseTransaction(final Pse pse, @NonNull final EpaycoCallback callback) {
        Epayco epayco = new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                String projectnumber1 = data.getString("bearer_token");
                token_bearer2 = projectnumber1;
                token_bearer = "Bearer " + projectnumber1;
           //     Log.d("createPseTransaction","=>"+token_bearer);
        String Base = base(true);
                if(token_bearer2 != null) {
        try {
            post(Base + "/restpagos/pagos/debitos.json", hashMapFromPse(pse, buildOptions()), token_bearer, callback);
        } catch (Exception e) {
            callback.onError(e);
        }
    }
}
    @Override
    public void onError(Exception error) {
        Log.d("bearer_token","=>"+error);
    }
});
    }

    /**
     * Retrieve pse transaction from transactionID
     * @param uid         transaction id
     * @param callback    response request api
     */
    public void getPseTransaction(final String uid, @NonNull final EpaycoCallback callback) {
           Epayco epayco = new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){
               @Override
            public void onSuccess(JSONObject data) throws JSONException {
                  String projectnumber1 = data.getString("bearer_token");
                token_bearer2 = projectnumber1;
                token_bearer = "Bearer " + projectnumber1;
            //     Log.d("getPseTransaction","=>"+token_bearer);
        String Base = base(true);
              if(token_bearer2 != null){
                try {
                    get(Base + "/restpagos/pse/transactioninfomation.json?transactionID=" + uid + "&public_key=" + apiKey, token_bearer,callback);
                    } catch (Exception e) {
                 callback.onError(e);
                 }
                 }
     

        }     @Override
            public void onError(Exception error) {
                Log.d("bearer_token","=>"+error);
            }

           });
    }

    /**
     * Get Banks List
     */
    public void getBanks(@NonNull final EpaycoCallback callback) {
        Epayco epayco = new Authentication().AuthService(apiKey, privateKey, new EpaycoCallback(){
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                String projectnumber1 = data.getString("bearer_token");
                token_bearer2 = projectnumber1;
                token_bearer = "Bearer " + projectnumber1;
                String Base = base(true);
                        
                if(token_bearer2 != null) {
                    try {
                        get(Base + "/restpagos/pse/bancos.json?public_key=" + apiKey, token_bearer, callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }

            @Override
            public void onError(Exception error) {
                Log.d("bearer_token","=>"+error);
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
        public void createCashTransaction(final Cash cash, @NonNull final EpaycoCallback callback) {
        Epayco epayco = new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                String projectnumber1 = data.getString("bearer_token");
                token_bearer2 = projectnumber1;
                token_bearer = "Bearer " + projectnumber1;
              //  Log.d("createCashTransaction", "=>" + token_bearer);
                String Base = base(true), url = null;

                switch (cash.getType()) {
                    case "efecty":
                        url = "/restpagos/v2/efectivo/efecty";
                        break;
                    case "baloto":
                        url = "/restpagos/v2/efectivo/baloto";
                        break;
                    case "gana":
                        url = "/restpagos/v2/efectivo/gana";
                        break;
                    case "redservi":
                        url = "/restpagos/v2/efectivo/redservi";
                        break;
                    case "puntored":
                        url = "/restpagos/v2/efectivo/puntored";
                        break;
                    case "sured":
                        url = "/restpagos/v2/efectivo/sured";
                        break;
                    default:
                        System.out.println("error payment");
                }
                if(token_bearer2 != null){
                try {
                    post(Base + url, hashMapFromCash(cash, buildOptions()), token_bearer, callback);
                } catch (Exception e) {
                    callback.onError(e);
                }
            }
            }
            @Override
            public void onError(Exception error) {
                Log.d("bearer_token","=>"+error);
            }
        });
    }

    public void getReferencePayment(final String uid, @NonNull final EpaycoCallback callback) {
          Epayco epayco = new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){
               @Override
            public void onSuccess(JSONObject data) throws JSONException {
                  String projectnumber1 = data.getString("bearer_token");
                token_bearer2 = projectnumber1;
                token_bearer = "Bearer " + projectnumber1;
            //     Log.d("getPseTransaction","=>"+token_bearer);
        String Base = base(true);
               if(token_bearer2 != null){
                     try {
                         get(Base + "/restpagos/transaction/response.json?ref_payco=" + uid + "&public_key=" + apiKey,token_bearer, callback);
                        } catch (Exception e) {
                         callback.onError(e);
                        }
                 }
  

         }     @Override
            public void onError(Exception error) {
                Log.d("bearer_token","=>"+error);
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
    public void createCharge(final Charge charge, @NonNull final EpaycoCallback callback) {
        Epayco epayco = new Authentication().AuthService(apiKey,privateKey,new EpaycoCallback(){

            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                String projectnumber1 = data.getString("bearer_token");
                token_bearer2 = projectnumber1;
                token_bearer = "Bearer " + projectnumber1;
           //     Log.d("createCharge","=>"+token_bearer);
                String Base = base(false);
                if(token_bearer2 != null){

        try {
            post(Base + "/payment/v1/charge/create", hashMapFromCharge(charge), token_bearer, callback);
        } catch (Exception e) {
            callback.onError(e);
        }

    }
}

    @Override
    public void onError(Exception error) {
        Log.d("bearer_token","=>"+error);
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
    public void createDaviplata(final Daviplata daviplata, @NonNull final EpaycoCallback callback) {
        Epayco epayco = new Authentication().AuthServiceApify(apiKey,privateKey,new EpaycoCallback(){

            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                String token = data.getString("token");
                token_bearer = "Bearer " + token;
                if(token != null){
                    try {
                        post(BASE_URL_APIFY + "/payment/process/daviplata", hashMapFromDaviplata(daviplata), token_bearer, callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }

            @Override
            public void onError(Exception error) {
                Log.d("bearer_token","=>"+error);
            }
        });
    }

    /**
     * Confirm daviplata transaction
     * @param confirm DaviplatfConfirma model
     * @param callback    response request api
     */
    public void confirmDaviplata(final DaviplataConfirm confirm, @NonNull final EpaycoCallback callback) {
        Epayco epayco = new Authentication().AuthServiceApify(apiKey,privateKey,new EpaycoCallback(){

            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                String token = data.getString("token");
                token_bearer = "Bearer " + token;
                if(token != null){

                    try {
                        post(BASE_URL_APIFY + "/payment/confirm/daviplata", hashMapFromDaviplataConfirm(confirm), token_bearer, callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }

                }
            }

            @Override
            public void onError(Exception error) {
                Log.d("bearer_token","=>"+error);
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
    public void createSafetypay(final Safetypay safetypay, @NonNull final EpaycoCallback callback) {
        Epayco epayco = new Authentication().AuthServiceApify(apiKey,privateKey,new EpaycoCallback(){

            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                String token = data.getString("token");
                token_bearer = "Bearer " + token;
                if(token != null){

                    try {
                        post(BASE_URL_APIFY + "/payment/process/safetypay", hashMapFromSafetypay(safetypay), token_bearer, callback);
                    } catch (Exception e) {
                        callback.onError(e);
                    }

                }
            }

            @Override
            public void onError(Exception error) {
                Log.d("bearer_token","=>"+error);
            }
        });
    }


    /***************
     * Definitions *
     ***************/

    /**
     * Petition api type get
     * @param url      url petition api
     * @param options  data user options
     * @param callback response request api
     */
    public static void get(String url,String options, @NonNull final EpaycoCallback callback) {
        client.addHeader("Authorization",options);
        client.addHeader("type", "sdk-jwt");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject obj = new JSONObject(new String(responseBody));
                    callback.onSuccess(obj);
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
    

    /**
     * Petition api type post
     * @param url      url petition api
     * @param data     data send petition
     * @param options  data user options
     * @param callback response request api
     */
    public static void post(String url, @NonNull RequestParams data, String options, @NonNull final EpaycoCallback callback) {
        //client.setBasicAuth(options, "");
        client.addHeader("Authorization",options);
        client.addHeader("type", "sdk-jwt");
        client.post(url, data, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject obj = new JSONObject(new String(responseBody));
                    //JSONObject data = new JSONObject(obj.getString("data"));
                    callback.onSuccess(obj);
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

    public String base(boolean isApi) {
        if (isApi) {
            return BASE_URL_SECURE;
        } else {
            return BASE_URL;
        }
    }
}