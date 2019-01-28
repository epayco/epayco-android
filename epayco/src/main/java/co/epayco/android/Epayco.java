package co.epayco.android;

import android.support.annotation.NonNull;

import com.loopj.android.http.*;

import org.json.JSONException;
import org.json.JSONObject;

import co.epayco.android.models.Authentication;
import co.epayco.android.models.Card;
import co.epayco.android.models.Cash;
import co.epayco.android.models.Charge;
import co.epayco.android.models.ChargeSub;
import co.epayco.android.models.Client;
import co.epayco.android.models.Plan;
import co.epayco.android.models.Pse;
import co.epayco.android.models.Subscription;
import co.epayco.android.util.DateUtils;
import co.epayco.android.util.EpaycoCallback;
import co.epayco.android.util.Util;
import cz.msebera.android.httpclient.Header;

import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromCLient;
import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromCard;
import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromCash;
import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromPlan;
import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromSub;
import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromSubCharge;
import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromPse;
import static co.epayco.android.util.EpaycoNetworkUtils.hashMapFromCharge;

public class Epayco {

    private static AsyncHttpClient client = new AsyncHttpClient();

    private static final String BASE_URL = "https://api.secure.payco.co";
    private static final String BASE_URL_SECURE = "https://secure.payco.co";

    private static final int MAX_TIME_OUT= 190*1000;

    private String apiKey;
    private String privateKey;
    private String lang;
    private Boolean test;

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
    public void createToken(@NonNull Card card, @NonNull EpaycoCallback callback) {
        String Base = base(false);
        try {
            post(Base + "/v1/tokens", hashMapFromCard(card), apiKey, callback);
        } catch (Exception e) {
            callback.onError(e);
        }
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
    public void createCustomer(@NonNull Client client, @NonNull EpaycoCallback callback) {
        String Base = base(false);
        try {
            post(Base + "/payment/v1/customer/create", hashMapFromCLient(client), apiKey, callback);
        } catch (Exception e) {
            callback.onError(e);
        }

    }

    /**
     * Retrieve customer from id
     * @param uid      id customer
     * @param callback response request api
     */
    public void getCustomer(String uid, @NonNull EpaycoCallback callback) {
        String Base = base(false);
        try {
            get(Base + "/payment/v1/customer/" + apiKey + "/" + uid, callback);
        } catch (Exception e) {
            callback.onError(e);
        }

    }

    /**
      * Return list customer
      * @param callback    response request api
     */
    public void getCustomerList(@NonNull EpaycoCallback callback) {
        String Base = base(false);
        try {
            get(Base + "/payment/v1/customers/" + apiKey + "/", callback);
        } catch (Exception e) {
            callback.onError(e);
        }
    }

    /***************************
     * Access plan definitions *
     ***************************/

    /**
     * Create plan
     * @param plan        data model plan
     * @param callback    response request api
     */
    public void createPlan(@NonNull Plan plan, @NonNull EpaycoCallback callback) {
        String Base = base(false);
        try {
            post(Base + "/recurring/v1/plan/create", hashMapFromPlan(plan), apiKey, callback);
        } catch (Exception e) {
            callback.onError(e);
        }
    }

    /**
     * Retrieve plan from id
     * @param uid         id plan
     * @param callback    response request api
     */
    public void getPlan(String uid, @NonNull EpaycoCallback callback) {
        String Base = base(false);
        try {
            get(Base + "/recurring/v1/plan/" + apiKey + "/" + uid, callback);
        } catch (Exception e) {
            callback.onError(e);
        }
    }

    /**
     * Return list plan
     * @param callback    response request api
     */
    public void getPlanList(@NonNull EpaycoCallback callback) {
        String Base = base(false);
        try {
            get(Base + "/recurring/v1/plans/" + apiKey + "/", callback);
        } catch (Exception e) {
            callback.onError(e);
        }
    }

    /************************************
     * Access subscriptions definitions *
     ************************************/

    /**
     * Create subscription
     * @param sub         data model subscription
     * @param callback    response request api
     */
    public void createSubscription(Subscription sub, @NonNull EpaycoCallback callback) {
        String Base = base(false);
        try {
            post(Base + "/recurring/v1/subscription/create", hashMapFromSub(sub), apiKey, callback);
        } catch (Exception e) {
            callback.onError(e);
        }
    }

    /**
     * Retrieve subscription from id
     * @param uid         id subscription
     * @param callback    response request api
     */
    public void getSubscription(String uid, @NonNull EpaycoCallback callback) {
        String Base = base(false);
        try {
            get(Base + "/recurring/v1/subscription/" + uid + "/" + apiKey, callback);
        } catch (Exception e) {
            callback.onError(e);
        }
    }

    /**
     * Get list subscription
     * @param callback    response request api
     */
    public void getSubscriptionList(@NonNull EpaycoCallback callback) {
        String Base = base(false);
        try {
            get(Base + "/recurring/v1/subscriptions/" + apiKey + "/", callback);
        } catch (Exception e) {
            callback.onError(e);
        }
    }

    /**
     * Charge subscription ans subscribe plan
     * @param sub         data model charge subscription
     * @param callback    response request api
     */
    public void chargeSubscription(ChargeSub sub, @NonNull EpaycoCallback callback) {
        String Base = base(false);
        try {
            post(Base + "/payment/v1/charge/subscription/create", hashMapFromSubCharge(sub), apiKey, callback);
        } catch (Exception e) {
            callback.onError(e);
        }
    }

    /***************************
     * Access bank definitions *
     ***************************/

    /**
     * Create pse transaction
     * @param pse         data model pse
     * @param callback    response request api
     */
    public void createPseTransaction(Pse pse, @NonNull EpaycoCallback callback) {
        String Base = base(true);
        try {
            post(Base + "/restpagos/pagos/debitos.json", hashMapFromPse(pse, buildOptions()), apiKey, callback);
        } catch (Exception e) {
            callback.onError(e);
        }
    }

    /**
     * Retrieve pse transaction from transactionID
     * @param uid         transaction id
     * @param callback    response request api
     */
    public void getPseTransaction(String uid, @NonNull EpaycoCallback callback) {
        String Base = base(true);
        try {
            get(Base + "/restpagos/pse/transactioninfomation.json?transactionID=" + uid + "&public_key=" + apiKey, callback);
        } catch (Exception e) {
            callback.onError(e);
        }
    }

    /***************************
     * Access cash definitions *
     ***************************/

    /**
     * Create cash transaction
     * @param cash        data cash model
     * @param callback    response request api
     */
    public void createCashTransaction(Cash cash, @NonNull EpaycoCallback callback) {
        String Base = base(true), url = null;

        switch (cash.getType()) {
            case "efecty":
                    url = "/restpagos/pagos/efecties.json";
                break;
            case "baloto":
                url = "/restpagos/pagos/balotos.json";
                break;
            case "gana":
                url = "/restpagos/pagos/ganas.json";
                break;
            default:
                System.out.println("error payment");
        }

        try {
            post(Base + url, hashMapFromCash(cash, buildOptions()), apiKey, callback);
        } catch (Exception e) {
            callback.onError(e);
        }
    }

    public void getReferencePayment(String uid, @NonNull EpaycoCallback callback) {
        String Base = base(true);
        try {
            get(Base + "/restpagos/transaction/response.json?ref_payco=" + uid + "&public_key=" + apiKey, callback);
        } catch (Exception e) {
            callback.onError(e);
        }
    }

    /*****************************
     * Access charge definitions *
     *****************************/

    /**
     * Create charge token card
     * @param charge      charge model
     * @param callback    response request api
     */
    public void createCharge(Charge charge, @NonNull EpaycoCallback callback) {
        String Base = base(false);
        try {
            post(Base + "/payment/v1/charge/create", hashMapFromCharge(charge), apiKey, callback);
        } catch (Exception e) {
            callback.onError(e);
        }
    }

    /***************
     * Definitions *
     ***************/

    /**
     * Petition api type get
     * @param url      url petition api
     * @param callback response request api
     */
    public static void get(String url, @NonNull final EpaycoCallback callback) {
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

    public String base(boolean isApi) {
        if (isApi) {
            return BASE_URL_SECURE;
        } else {
            return BASE_URL;
        }
    }
}