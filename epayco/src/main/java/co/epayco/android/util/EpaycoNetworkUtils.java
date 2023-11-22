package co.epayco.android.util;

import android.util.Base64;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

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

public class EpaycoNetworkUtils {

    public static RequestParams hashMapFromCard(Card card) {

        RequestParams cardParams = new RequestParams();

        cardParams.put("card[number]", card.getNumber());
        cardParams.put("card[cvc]", card.getCvc());
        cardParams.put("card[exp_month]", card.getMonth());
        cardParams.put("card[exp_year]", card.getYear());
        cardParams.put("hasCvv", card.getHasCvv());

        return cardParams;
    }

    public static RequestParams hashMapFromCLient(Client client) {

        RequestParams clientParams = new RequestParams();

        clientParams.put("token_card", client.getTokenId());
        clientParams.put("name", client.getName());
        clientParams.put("email", client.getEmail());
        clientParams.put("phone", client.getPhone());
        clientParams.put("default", client.getDefaultCard());


        return clientParams;
    }


    public static RequestParams hashMapFromCLientDelete(Client client) {

        RequestParams clientParams = new RequestParams();

        clientParams.put("franchise", client.getFranchise());
        clientParams.put("mask", client.getMask());
        clientParams.put("customer_id", client.getCustomer_id());

        return clientParams;
    }

    public static RequestParams hashMapFromCLientCardDefault(Client client) {

        RequestParams clientParams = new RequestParams();

        clientParams.put("customer_id", client.getCustomer_id());
        clientParams.put("token", client.getTokenId());
        clientParams.put("franchise", client.getFranchise());
        clientParams.put("mask", client.getMask());


        return clientParams;
    }

    public static RequestParams hashMapFromCLientCardNew(Client client) {

        RequestParams clientParams = new RequestParams();
        clientParams.put("token_card", client.getTokenId());
        clientParams.put("customer_id", client.getCustomer_id());

        return clientParams;
    }

    public static RequestParams hashMapFromPlan(Plan plan) {

        RequestParams planParams = new RequestParams();

        planParams.put("id_plan", plan.getIdPlan());
        planParams.put("name", plan.getName());
        planParams.put("description", plan.getDescription());
        planParams.put("amount", plan.getAmount());
        planParams.put("currency", plan.getCurrency());
        planParams.put("interval", plan.getInterval());
        planParams.put("interval_count", plan.getIntervalCount());
        planParams.put("trial_days", plan.getTrialDays());

        return planParams;
    }

    public static RequestParams hashMapFromSub(Subscription subscription) {

        RequestParams subscriptionParams = new RequestParams();

        subscriptionParams.put("id_plan", subscription.getIdPlan());
        subscriptionParams.put("customer", subscription.getCustomer());
        subscriptionParams.put("token_card", subscription.getTokenCard());
        subscriptionParams.put("url_confirmation", subscription.getUrlConfirmation());
        subscriptionParams.put("doc_type", subscription.getDoc_type());
        subscriptionParams.put("doc_number", subscription.getDoc_number());

        return subscriptionParams;
    }


    public static RequestParams hashMapFromSubCharge(ChargeSub subscription) {

        JSONObject extras_epayco = new JSONObject();
        try {
            extras_epayco.put("extra5","P47");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        RequestParams subscriptionParams = new RequestParams();

        subscriptionParams.put("id_plan", subscription.getIdPlan());
        subscriptionParams.put("customer", subscription.getCustomer());
        subscriptionParams.put("token_card", subscription.getTokenCard());
        subscriptionParams.put("doc_type", subscription.getDocType());
        subscriptionParams.put("doc_number", subscription.getDocNumber());
        subscriptionParams.put("ip", subscription.getIp());
        subscriptionParams.put("extras_epayco", extras_epayco);


        return subscriptionParams;
    }

    public static RequestParams hashMapFromSubCancel(String id) {

        RequestParams subscriptionParams = new RequestParams();

        subscriptionParams.put("id", id);

        return subscriptionParams;
    }

    public static RequestParams hashMapFromCharge(Charge charge) {

        JSONObject extras_epayco = new JSONObject();
        try {
            extras_epayco.put("extra5","P47");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        RequestParams chargeParams = new RequestParams();

        //Schema
        chargeParams.put("token_card", charge.getTokenCard());
        chargeParams.put("customer_id", charge.getCustomerId());

        //Required
        chargeParams.put("doc_type", charge.getDocType());
        chargeParams.put("doc_number", charge.getDocNumber());
        chargeParams.put("name", charge.getName());
        chargeParams.put("last_name", charge.getLastName());
        chargeParams.put("email", charge.getEmail());
        chargeParams.put("bill", charge.getInvoice());
        chargeParams.put("description", charge.getDescription());
        chargeParams.put("value", charge.getValue());
        chargeParams.put("tax", charge.getTax());
        chargeParams.put("tax_base", charge.getTaxBase());
        chargeParams.put("currency", charge.getCurrency());
        chargeParams.put("dues", charge.getDues());
        chargeParams.put("ip", charge.getIp());
        chargeParams.put("extras_epayco", extras_epayco);
        //Optional
        chargeParams.put("ico", charge.getIco());
        chargeParams.put("use_default_card_customer", charge.getUse_default_card_customer());
        chargeParams.put("url_response", charge.getUrlResponse());
        chargeParams.put("url_confirmation", charge.getUrlConfirmation());
        chargeParams.put("method_confirmation", charge.getMethodConfirmation());
        chargeParams.put("extra1", charge.getExtra1());
        chargeParams.put("extra2", charge.getExtra2());
        chargeParams.put("extra3", charge.getExtra3());
        chargeParams.put("city", charge.getCity());
        chargeParams.put("departament", charge.getDepartament());
        chargeParams.put("country", charge.getCountry());
        chargeParams.put("address", charge.getAddress());
        chargeParams.put("splitpayment", charge.getSplitpayment());
        chargeParams.put("split_app_id", charge.getSplit_app_id());
        chargeParams.put("split_merchant_id", charge.getSplit_merchant_id());
        chargeParams.put("split_type", charge.getSplit_type());
        chargeParams.put("split_primary_receiver", charge.getSplit_primary_receiver());
        chargeParams.put("split_primary_receiver_fee", charge.getSplit_primary_receiver_fee());

        return chargeParams;
    }

    public static RequestParams hashMapFromPse(Pse pse, JSONObject options) {

        RequestParams pseParams = new RequestParams();
        String apiKey = null, secretKey = null, test = null;
        try {
            apiKey = options.getString("apiKey");
            secretKey = options.getString("privateKey");
            test = options.getString("test");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject extras_epayco = new JSONObject();
        try {
            extras_epayco.put("extra5",encrypt("P47", secretKey));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        //Schema
        pseParams.put("public_key", apiKey);
        pseParams.put("banco", encrypt(pse.getBank(), secretKey));
        pseParams.put("tipo_persona", encrypt(pse.getTypePerson(), secretKey));

        //Required
        pseParams.put("factura", encrypt(pse.getInvoice(), secretKey));
        pseParams.put("descripcion", encrypt(pse.getDescription(), secretKey));
        pseParams.put("valor", encrypt(pse.getValue(), secretKey));
        pseParams.put("iva", encrypt(pse.getTax(), secretKey));
        pseParams.put("baseiva", encrypt(pse.getTaxBase(), secretKey));
        pseParams.put("moneda", encrypt(pse.getCurrency(), secretKey));
        pseParams.put("tipo_persona", encrypt(pse.getTypePerson(), secretKey));
        pseParams.put("tipo_doc", encrypt(pse.getDocType(), secretKey));
        pseParams.put("documento", encrypt(pse.getDocNumber(), secretKey));
        pseParams.put("nombres", encrypt(pse.getName(), secretKey));
        pseParams.put("apellidos", encrypt(pse.getLastName(), secretKey));
        pseParams.put("email", encrypt(pse.getEmail(), secretKey));
        pseParams.put("pais", encrypt(pse.getCountry(), secretKey));
        pseParams.put("celular", encrypt(pse.getPhone(), secretKey));
        pseParams.put("pais", encrypt(pse.getCountry(), secretKey));
        pseParams.put("url_respuesta", encrypt(pse.getUrlResponse(), secretKey));
        pseParams.put("url_confirmacion", encrypt(pse.getUrlConfirmation(), secretKey));
        pseParams.put("ip", encrypt(pse.getIp(), secretKey));
        pseParams.put("extras_epayco", extras_epayco);
        //Optional
        pseParams.put("ico", encrypt(pse.getIco(), secretKey));
        pseParams.put("metodoconfirmacion", encrypt(pse.getMethodConfirmation(), secretKey));
        pseParams.put("direccion", encrypt(pse.getAddress(), secretKey));
        pseParams.put("extra1", encrypt(pse.getExtra1(), secretKey));
        pseParams.put("extra2", encrypt(pse.getExtra2(), secretKey));
        pseParams.put("extra3", encrypt(pse.getExtra3(), secretKey));
        pseParams.put("ciudad", encrypt(pse.getCity(), secretKey));
        pseParams.put("depto", encrypt(pse.getDepto(), secretKey));
        if (pse.getSplitpayment()!=null ){
            pseParams.put("splitpayment", encrypt(pse.getSplitpayment(),secretKey));
            pseParams.put("split_app_id", encrypt(pse.getSplit_app_id(),secretKey));
            pseParams.put("split_merchant_id", encrypt(pse.getSplit_merchant_id(),secretKey));
            pseParams.put("split_type", encrypt(pse.getSplit_type(),secretKey));
            pseParams.put("split_primary_receiver", encrypt(pse.getSplit_primary_receiver(),secretKey));
            pseParams.put("split_primary_receiver_fee", encrypt(pse.getSplit_primary_receiver_fee(),secretKey));
        }
        //System
        pseParams.put("enpruebas", encrypt(new String(test).toUpperCase(), secretKey));
        pseParams.put("i", generateI());
        pseParams.put("lenguaje", "android");

        return pseParams;
    }

    public static RequestParams hashMapFromCash(Cash cash, JSONObject options) {

        RequestParams cashParams = new RequestParams();
        String apiKey = null, secretKey = null, test = null;
        JSONObject extras_epayco = new JSONObject();
        try {
            extras_epayco.put("extra5","P47");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        try {
            apiKey = options.getString("apiKey");
            secretKey = options.getString("privateKey");
            test = options.getString("test");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Schema
        cashParams.put("public_key", apiKey);
        cashParams.put("fechaexpiracion", cash.getEndDate());

        //Required
        cashParams.put("tipo_doc", cash.getDocType());
        cashParams.put("documento", cash.getDocNumber());
        cashParams.put("nombres", cash.getName());
        cashParams.put("apellidos", cash.getLastName());
        cashParams.put("email", cash.getEmail());
        cashParams.put("factura", cash.getInvoice());
        cashParams.put("descripcion", cash.getDescription());
        cashParams.put("valor", cash.getValue());
        cashParams.put("iva", cash.getTax());
        cashParams.put("baseiva", cash.getTaxBase());
        cashParams.put("celular", cash.getPhone());
        cashParams.put("moneda", cash.getCurrency());
        cashParams.put("ip", cash.getIp());
        cashParams.put("extras_epayco", extras_epayco);

        //Optional
        cashParams.put("ico", cash.getIco());
        cashParams.put("url_respuesta", cash.getUrlResponse());
        cashParams.put("url_confirmacion", cash.getUrlConfirmation());
        cashParams.put("metodoconfirmacion", cash.getMethodConfirmation());
        cashParams.put("extra1", cash.getExtra1());
        cashParams.put("extra2", cash.getExtra2());
        cashParams.put("extra3", cash.getExtra3());
        cashParams.put("pais", cash.getCountry());
        cashParams.put("ciudad", cash.getCity());
        cashParams.put("depto", cash.getDepto());
        cashParams.put("direccion", cash.getAddress());
        cashParams.put("splitpayment", cash.getSplitpayment());
        cashParams.put("split_app_id", cash.getSplit_app_id());
        cashParams.put("split_merchant_id", cash.getSplit_merchant_id());
        cashParams.put("split_type", cash.getSplit_type());
        cashParams.put("split_primary_receiver", cash.getSplit_primary_receiver());
        cashParams.put("split_primary_receiver_fee",cash.getSplit_primary_receiver_fee());

        //System
        cashParams.put("enpruebas", new String(test).toUpperCase());
        cashParams.put("i", generateI());
        cashParams.put("lenguaje", "android");

        return cashParams;
    }

    public static RequestParams hashMapFromDaviplata(Daviplata daviplata) {

        RequestParams daviplataParams = new RequestParams();
        JSONObject extras_epayco = new JSONObject();
        try {
            extras_epayco.put("extra5","P47");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        //Required
        daviplataParams.put("docType", daviplata.getDocType());
        daviplataParams.put("document", daviplata.getDocument());
        daviplataParams.put("name", daviplata.getName());
        daviplataParams.put("indCountry", daviplata.getIndCountry());
        daviplataParams.put("city", daviplata.getCity());
        daviplataParams.put("address", daviplata.getAddress());
        daviplataParams.put("value", daviplata.getValue());
        daviplataParams.put("extras_epayco", extras_epayco);

        //Optional
        daviplataParams.put("lastName", daviplata.getLastName());
        daviplataParams.put("email", daviplata.getEmail());
        daviplataParams.put("phone", daviplata.getPhone());
        daviplataParams.put("country", daviplata.getCountry());
        daviplataParams.put("ip", daviplata.getIp());
        daviplataParams.put("currency", daviplata.getCurrency());
        daviplataParams.put("invoice", daviplata.getInvoice());
        daviplataParams.put("description", daviplata.getDescription());
        daviplataParams.put("tax", daviplata.getTax());
        daviplataParams.put("taxBase", daviplata.getTaxBase());
        daviplataParams.put("ico", daviplata.getIco());
        daviplataParams.put("testMode", daviplata.getTestMode());
        daviplataParams.put("urlResponse", daviplata.getUrlResponse());
        daviplataParams.put("urlConfirmation", daviplata.getUrlConfirmation());
        daviplataParams.put("methodConfirmation", daviplata.getMethodConfirmation());
        return daviplataParams;
    }

    public static RequestParams hashMapFromDaviplataConfirm(DaviplataConfirm daviplata) {

        RequestParams daviplataParams = new RequestParams();

        //Required
        daviplataParams.put("refPayco", daviplata.getRefPayco());
        daviplataParams.put("idSessionToken", daviplata.getIdSessionToken());
        daviplataParams.put("otp", daviplata.getOtp());
        return daviplataParams;
    }

    public static RequestParams hashMapFromSafetypay(Safetypay safetypay) {

        RequestParams safetypayParams = new RequestParams();

        //Required
        safetypayParams.put("cash", safetypay.getCash());
        safetypayParams.put("expirationDate", safetypay.getEndDate());
        safetypayParams.put("docType", safetypay.getDocType());
        safetypayParams.put("document", safetypay.getDocument());
        safetypayParams.put("name", safetypay.getName());
        safetypayParams.put("indCountry", safetypay.getIndCountry());
        safetypayParams.put("city", safetypay.getCity());
        safetypayParams.put("address", safetypay.getAddress());
        safetypayParams.put("value", safetypay.getValue());

        //Optional
        safetypayParams.put("lastName", safetypay.getLastName());
        safetypayParams.put("email", safetypay.getEmail());
        safetypayParams.put("phone", safetypay.getPhone());
        safetypayParams.put("country", safetypay.getCountry());
        safetypayParams.put("ip", safetypay.getIp());
        safetypayParams.put("currency", safetypay.getCurrency());
        safetypayParams.put("invoice", safetypay.getInvoice());
        safetypayParams.put("description", safetypay.getDescription());
        safetypayParams.put("tax", safetypay.getTax());
        safetypayParams.put("taxBase", safetypay.getTaxBase());
        safetypayParams.put("ico", safetypay.getIco());
        safetypayParams.put("testMode", safetypay.getTestMode());
        safetypayParams.put("urlResponse", safetypay.getUrlResponse());
        safetypayParams.put("urlResponsePointer", safetypay.getUrlResponsePointer());
        safetypayParams.put("urlConfirmation", safetypay.getUrlConfirmation());
        safetypayParams.put("methodConfirmation", safetypay.getMethodConfirmation());
        safetypayParams.put("typeIntegration", safetypay.getTypeIntegration());
        return safetypayParams;
    }

    public static RequestParams hashMapFromEmpty() {
        RequestParams emptyParams = new RequestParams();
        return emptyParams;
    }

    public static String encrypt(String value, String key) {
        String cryptText = null, keyString = ((value == null) ? "N/A" : value);
        EpaycoCrypt Crypt = new EpaycoCrypt(key);

        try {
            cryptText = Crypt.bytesToHex(Crypt.encrypt(keyString));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cryptText;
    }

    public static String generateI() {
        String iv = "0000000000000000";
        byte[] encoded = Base64.encode(iv.getBytes(), Base64.DEFAULT);
        String iv64 = new String(encoded);
        iv64.replace("\n","");
        return  iv64;
    }
}


