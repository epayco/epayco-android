package co.epayco.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import co.epayco.android.Epayco;
import co.epayco.android.models.Authentication;
import co.epayco.android.models.Card;
import co.epayco.android.models.Cash;
import co.epayco.android.models.Charge;
import co.epayco.android.models.ChargeSub;
import co.epayco.android.models.Client;
import co.epayco.android.models.Plan;
import co.epayco.android.models.Pse;
import co.epayco.android.models.Subscription;
import co.epayco.android.util.EpaycoCallback;

public class example extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        Authentication auth = new Authentication();

        auth.setApiKey("491d6a0b6e992cf924edd8d3d088aff1");
        auth.setPrivateKey("268c8e0162990cf2ce97fa7ade2eff5a");
        auth.setLang("ES");
        auth.setTest(true);

        Epayco epayco = new Epayco(auth);

        Card card = new Card();
        card.setNumber("4575623182290326");
        card.setMonth("12");
        card.setYear("2018");
        card.setCvc("123");

//        Boolean number = epayco.validNumberCard("4575623182290326");
//
//        Boolean month = epayco.validExpiryMonth("12");
//        Boolean year = epayco.validExpiryYear("2018");
//
//        Boolean expDate = epayco.validateExpiryDate("12", "2018");
//
//        Boolean cvc = epayco.validateCVC("123");
//
//        Boolean cardValid = epayco.validCard(card);
//
//        System.out.println("validate: " + cardValid);

//        epayco.createToken(card, new EpaycoCallback() {
//            @Override
//            public void onSuccess(JSONObject object) throws JSONException {
//                System.out.println("dataEpayco: " + object.getString("id"));
//            }
//
//            @Override
//            public void onError(Exception error) {
//                System.out.println("dataEpayco: " + error);
//            }
//        });
//
//        Client client = new Client();
//        client.setTokenId("Az9wdX4Wj3JRmr9NC");
//        client.setName("Cliente epayco");
//        client.setEmail("cliente@epayco.co");
//        client.setPhone("305274321");
//        client.setDefaultCard(true);
//
//        epayco.createCustomer(client, new EpaycoCallback() {
//            @Override
//            public void onSuccess(JSONObject data) throws JSONException {
//                System.out.println("dataEpayco: " + data);
//            }
//
//            @Override
//            public void onError(Exception error) {
//                System.out.println("dataEpayco: " + error);
//            }
//        });
//
//        epayco.getCustomer("oDFmhNLasGwAhSDWw", new EpaycoCallback() {
//            @Override
//            public void onSuccess(JSONObject data) throws JSONException {
//                System.out.println("dataEpayco: " + data);
//            }
//
//            @Override
//            public void onError(Exception error) {
//                System.out.println("dataEpayco: " + error.getMessage());
//            }
//        });
//
//        epayco.getCustomerList(new EpaycoCallback() {
//            @Override
//            public void onSuccess(JSONObject data) throws JSONException {
//                System.out.println("dataEpayco: " + data);
//            }
//
//            @Override
//            public void onError(Exception error) {
//                System.out.println("dataEpayco: " + error.getMessage());
//            }
//        });
//
//        Plan plan = new Plan();
//
//        plan.setIdPlan("cursocarpinteria");
//        plan.setName("Curso de carpintería");
//        plan.setDescription("En este curso aprenderás carpintería");
//        plan.setAmount("30000");
//        plan.setCurrency("COP");
//        plan.setInterval("month");
//        plan.setIntervalCount("1");
//        plan.setTrialDays("0");
////
//        epayco.createPlan(plan, new EpaycoCallback() {
//            @Override
//            public void onSuccess(JSONObject data) throws JSONException {
//                System.out.println("dataEpayco: " + data);
//            }
//
//            @Override
//            public void onError(Exception error) {
//                System.out.println("dataEpayco: " + error.getMessage());
//            }
//        });
//
//        epayco.getPlan("cursocarpinteria", new EpaycoCallback() {
//            @Override
//            public void onSuccess(JSONObject data) throws JSONException {
//                System.out.println("dataEpayco: " + data);
//            }
//
//            @Override
//            public void onError(Exception error) {
//                System.out.println("dataEpayco: " + error.getMessage());
//            }
//        });
//
//        epayco.getCustomerList(new EpaycoCallback() {
//            @Override
//            public void onSuccess(JSONObject data) throws JSONException {
//                System.out.println("dataEpayco: " + data);
//            }
//
//            @Override
//            public void onError(Exception error) {
//                System.out.println("dataEpayco: " + error.getMessage());
//            }
//        });
//
//        Subscription subscription = new Subscription();
//
//        subscription.setTokenCard("2L9R3ey5paBTN8gzr");
//        subscription.setCustomer("oDFmhNLasGwAhSDWw");
//        subscription.setIdPlan("cursocarpinteria");
////
////
//        epayco.createSubscription(subscription, new EpaycoCallback() {
//            @Override
//            public void onSuccess(JSONObject data) throws JSONException {
//                System.out.println("dataEpayco: " + data);
//            }
//
//            @Override
//            public void onError(Exception error) {
//                System.out.println("dataEpayco: " + error.getMessage());
//            }
//        });
//
//        epayco.getSubscription("9Wehdwd3ahBXqZtLS", new EpaycoCallback() {
//            @Override
//            public void onSuccess(JSONObject data) throws JSONException {
//                System.out.println("dataEpayco: " + data);
//            }
//
//            @Override
//            public void onError(Exception error) {
//                System.out.println("dataEpayco: " + error.getMessage());
//            }
//        });
//
//        epayco.getSubscriptionList(new EpaycoCallback() {
//            @Override
//            public void onSuccess(JSONObject data) throws JSONException {
//                System.out.println("dataEpayco: " + data);
//            }
//
//            @Override
//            public void onError(Exception error) {
//                System.out.println("dataEpayco: " + error.getMessage());
//            }
//        });
//
//        ChargeSub sub = new ChargeSub();
//
//        sub.setIdPlan("cursocarpinteria");
//        sub.setCustomer("oDFmhNLasGwAhSDWw");
//        sub.setTokenCard("2L9R3ey5paBTN8gzr");
//        sub.setDocType("CC");
//        sub.setDocNumber("5234567");
////
//        epayco.chargeSubscription(sub, new EpaycoCallback() {
//            @Override
//            public void onSuccess(JSONObject data) throws JSONException {
//                System.out.println("dataEpayco: " + data);
//            }
//
//            @Override
//            public void onError(Exception error) {
//                System.out.println("dataEpayco: " + error.getMessage());
//            }
//        });
//
//        Pse pse = new Pse();
//
//        //Schema
//        pse.setBank("1022");
//        pse.setTypePerson("0");
//
//        //Required
//        pse.setDocType("CC");
//        pse.setDocNumber("1035851980");
//        pse.setName("John");
//        pse.setLastName("Doe");
//        pse.setEmail("example@email.com");
//        pse.setInvoice("OR-1234");
//        pse.setDescription("Test Payment");
//        pse.setValue("116000");
//        pse.setTax("16000");
//        pse.setTaxBase("100000");
//        pse.setPhone("3010000001");
//        pse.setCurrency("COP");
//        pse.setCountry("CO");
//        pse.setUrlResponse("https:/secure.payco.co/restpagos/testRest/endpagopse.php");
//        pse.setUrlConfirmation("https:/secure.payco.co/restpagos/testRest/endpagopse.php");
//
//        //Optional
//        pse.setExtra1("");
//        pse.setExtra2("");
//        pse.setExtra3("");
//        pse.setCity("");
//        pse.setDepto("");
//        pse.setAddress("");
////
//        epayco.createPseTransaction(pse, new EpaycoCallback() {
//            @Override
//            public void onSuccess(JSONObject data) throws JSONException {
//                System.out.println("dataEpayco: " + data);
//            }
//
//            @Override
//            public void onError(Exception error) {
//                System.out.println("dataEpayco: " + error.getMessage());
//            }
//        });
//
//          epayco.getPseTransaction("1253350", new EpaycoCallback() {
//              @Override
//              public void onSuccess(JSONObject data) throws JSONException {
//                  System.out.println("dataEpayco: " + data);
//              }
//
//              @Override
//              public void onError(Exception error) {
//                  System.out.println("dataEpayco: " + error.getMessage());
//              }
//          });
//
//        Cash cash = new Cash();
//
//        //Schema
//        cash.setType("efecty");
//        cash.setEndDate("2017-12-05");
//
//        //Required
//        cash.setDocType("CC");
//        cash.setDocNumber("1035851980");
//        cash.setName("John");
//        cash.setLastName("Doe");
//        cash.setEmail("example@email.com");
//        cash.setInvoice("OR-1234");
//        cash.setDescription("Test Payment");
//        cash.setValue("116000");
//        cash.setTax("16000");
//        cash.setTaxBase("100000");
//        cash.setPhone("3010000001");
//        cash.setCurrency("COP");
//
//        //Optional
//        cash.setUrlResponse("");
//        cash.setUrlConfirmation("");
//        cash.setExtra1("");
//        cash.setExtra2("");
//        cash.setExtra3("");
//        cash.setCountry("");
//        cash.setCity("");
//        cash.setDepto("");
//        cash.setAddress("");
////
//        epayco.createCashTransaction(cash, new EpaycoCallback() {
//            @Override
//            public void onSuccess(JSONObject data) throws JSONException {
//                System.out.println("dataEpayco: " + data);
//            }
//
//            @Override
//            public void onError(Exception error) {
//                System.out.println("dataEpayco: " + error.getMessage());
//            }
//        });
//
//        epayco.getReferencePayment("275369", new EpaycoCallback() {
//            @Override
//            public void onSuccess(JSONObject data) throws JSONException {
//                System.out.println("dataEpayco: " + data);
//            }
//
//            @Override
//            public void onError(Exception error) {
//                System.out.println("dataEpayco: " + error.getMessage());
//            }
//        });
//
//        Charge charge = new Charge();
//
//        //Schema
//        charge.setTokenCard("2L9R3ey5paBTN8gzr");
//        charge.setCustomerId("oDFmhNLasGwAhSDWw");
//
//        //Required
//        charge.setDocType("CC");
//        charge.setDocNumber("1035851980");
//        charge.setName("John");
//        charge.setLastName("Doe");
//        charge.setEmail("example@email.com");
//        charge.setInvoice("OR-1234");
//        charge.setDescription("Test Payment");
//        charge.setValue("116000");
//        charge.setTax("16000");
//        charge.setTaxBase("100000");
//        charge.setCurrency("COP");
//        charge.setDues("12");
//
//        //Optional
//        charge.setUrlResponse("");
//        charge.setUrlConfirmation("");
//        charge.setExtra1("");
//        charge.setExtra2("");
//        charge.setExtra3("");
//        charge.setCity("");
//        charge.setDepartament("");
//        charge.setCountry("");
//        charge.setAddress("");
//
////
//        epayco.createCharge(charge, new EpaycoCallback() {
//            @Override
//            public void onSuccess(JSONObject data) throws JSONException {
//                System.out.println("dataEpayco: " + data);
//            }
//
//            @Override
//            public void onError(Exception error) {
//                System.out.println("dataEpayco: " + error.getMessage());
//            }
//        });
    }
}
