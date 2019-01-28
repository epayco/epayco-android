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
import co.epayco.android.models.Plan;
import co.epayco.android.models.Pse;
import co.epayco.android.models.Subscription;

public class EpaycoNetworkUtils {

    public static RequestParams hashMapFromCard(Card card) {

        RequestParams cardParams = new RequestParams();

        cardParams.put("card[number]", card.getNumber());
        cardParams.put("card[cvc]", card.getCvc());
        cardParams.put("card[exp_month]", card.getMonth());
        cardParams.put("card[exp_year]", card.getYear());

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

        return subscriptionParams;
    }


    public static RequestParams hashMapFromSubCharge(ChargeSub subscription) {

        RequestParams subscriptionParams = new RequestParams();

        subscriptionParams.put("id_plan", subscription.getIdPlan());
        subscriptionParams.put("customer", subscription.getCustomer());
        subscriptionParams.put("token_card", subscription.getTokenCard());
        subscriptionParams.put("doc_type", subscription.getDocType());
        subscriptionParams.put("doc_number", subscription.getDocNumber());

        return subscriptionParams;
    }

    public static RequestParams hashMapFromCharge(Charge charge) {

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

        //Optional
        chargeParams.put("url_response", charge.getUrlResponse());
        chargeParams.put("url_confirmation", charge.getUrlConfirmation());
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

        //Optional
        pseParams.put("metodoconfirmacion", encrypt("GET", secretKey));
        pseParams.put("direccion", encrypt(pse.getAddress(), secretKey));
        pseParams.put("extra1", encrypt(pse.getExtra1(), secretKey));
        pseParams.put("extra2", encrypt(pse.getExtra2(), secretKey));
        pseParams.put("extra3", encrypt(pse.getExtra3(), secretKey));
        pseParams.put("ciudad", encrypt(pse.getCity(), secretKey));
        pseParams.put("depto", encrypt(pse.getDepto(), secretKey));
        pseParams.put("splitpayment", encrypt(pse.getSplitpayment(),secretKey));
        pseParams.put("split_app_id", encrypt(pse.getSplit_app_id(),secretKey));
        pseParams.put("split_merchant_id", encrypt(pse.getSplit_merchant_id(),secretKey));
        pseParams.put("split_type", encrypt(pse.getSplit_type(),secretKey));
        pseParams.put("split_primary_receiver", encrypt(pse.getSplit_primary_receiver(),secretKey));
        pseParams.put("split_primary_receiver_fee", encrypt(pse.getSplit_primary_receiver_fee(),secretKey));

        //System
        pseParams.put("enpruebas", encrypt(new String(test).toUpperCase(), secretKey));
        pseParams.put("i", generateI());
        pseParams.put("lenguaje", "android");

        return pseParams;
    }

    public static RequestParams hashMapFromCash(Cash cash, JSONObject options) {

        RequestParams cashParams = new RequestParams();
        String apiKey = null, secretKey = null, test = null;

        try {
            apiKey = options.getString("apiKey");
            secretKey = options.getString("privateKey");
            test = options.getString("test");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Schema
        cashParams.put("public_key", apiKey);
        cashParams.put("fechaexpiracion", encrypt(cash.getEndDate(), secretKey));

        //Required
        cashParams.put("tipo_doc", encrypt(cash.getDocType(), secretKey));
        cashParams.put("documento", encrypt(cash.getDocNumber(), secretKey));
        cashParams.put("nombres", encrypt(cash.getName(), secretKey));
        cashParams.put("apellidos", encrypt(cash.getLastName(), secretKey));
        cashParams.put("email", encrypt(cash.getEmail(), secretKey));
        cashParams.put("factura", encrypt(cash.getInvoice(), secretKey));
        cashParams.put("descripcion", encrypt(cash.getDescription(), secretKey));
        cashParams.put("valor", encrypt(cash.getValue(), secretKey));
        cashParams.put("iva", encrypt(cash.getTax(), secretKey));
        cashParams.put("baseiva", encrypt(cash.getTaxBase(), secretKey));
        cashParams.put("celular", encrypt(cash.getPhone(), secretKey));
        cashParams.put("moneda", encrypt(cash.getCurrency(), secretKey));

        //Optional
        cashParams.put("url_respuesta", encrypt(cash.getUrlResponse(), secretKey));
        cashParams.put("url_confirmacion", encrypt(cash.getUrlConfirmation(), secretKey));
        cashParams.put("extra1", encrypt(cash.getExtra1(), secretKey));
        cashParams.put("extra2", encrypt(cash.getExtra2(), secretKey));
        cashParams.put("extra3", encrypt(cash.getExtra3(), secretKey));
        cashParams.put("pais", encrypt(cash.getCountry(), secretKey));
        cashParams.put("ciudad", encrypt(cash.getCity(), secretKey));
        cashParams.put("depto", encrypt(cash.getDepto(), secretKey));
        cashParams.put("direccion", encrypt(cash.getAddress(), secretKey));
        cashParams.put("splitpayment", encrypt(cash.getSplitpayment(),secretKey));
        cashParams.put("split_app_id", encrypt(cash.getSplit_app_id(),secretKey));
        cashParams.put("split_merchant_id", encrypt(cash.getSplit_merchant_id(),secretKey));
        cashParams.put("split_type", encrypt(cash.getSplit_type(),secretKey));
        cashParams.put("split_primary_receiver", encrypt(cash.getSplit_primary_receiver(),secretKey));
        cashParams.put("split_primary_receiver_fee", encrypt(cash.getSplit_primary_receiver_fee(),secretKey));

        //System
        cashParams.put("enpruebas", encrypt(new String(test).toUpperCase(), secretKey));
        cashParams.put("i", generateI());
        cashParams.put("lenguaje", "android");

        return cashParams;
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


