  package co.epayco.android;

import static java.sql.DriverManager.println;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import co.epayco.android.models.Authentication;
import co.epayco.android.models.Card;
import co.epayco.android.models.ChargeSub;
import co.epayco.android.models.Client;
import co.epayco.android.models.ClientList;
import co.epayco.android.models.Plan;
import co.epayco.android.models.PlanUpdate;
import co.epayco.android.models.Pse;
import co.epayco.android.models.Subscription;
import co.epayco.android.util.EpaycoCallback;

  public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Log.e("epayco","loading...");
        Authentication auth = new Authentication();

        auth.setApiKey("a1f2bdbe22c70b092e69e1f2cb7b7ea4");
        auth.setPrivateKey("9314c11e93925e91acd2bab9651849c0");
        auth.setLang("ES");
        auth.setTest(true);

        Epayco epayco = new Epayco(auth);

        /*var card = new Card();

        card.setNumber("4575623182290326");
        card.setMonth("12");
        card.setYear("2026");
        card.setCvc("123");
        card.setHasCvv(true);

        epayco.createToken(card, new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                Log.e("token: ",data.toString());
            }

            @Override
            public void onError(Exception error) {
                Log.e("tokenError: ",error.getMessage().toString());
            }
        });
*/
        String tokenId = "7d30f6d8abf75199e0a72d0";
        /*Client client = new Client();
        client.setTokenId(tokenId);
        client.setName("Mariangel salazar");
        client.setEmail("matiangel123@epayco.co");
        client.setPhone("304274321");
        client.setAddress("calle 109 # 67 - 12");
        client.setCity("Bogota");
        client.setDefaultCard(true);

        epayco.createCustomer(client, new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                Log.e("customer: ",data.toString());
            }

            @Override
            public void onError(Exception error) {
                Log.e("customerError: ",error.getMessage().toString());
            }
        });*/
        String customerId = "7d3115b43bd4a482604a01d";
        /*epayco.getCustomer(customerId, new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                Log.e("customer: ",data.toString());
            }

            @Override
            public void onError(Exception error) {
                Log.e("customerError: ",error.getMessage().toString());
            }
        });*/
        /*ClientList clients = new ClientList();
        clients.setPage("6");
        clients.setPerPage("10");
        epayco.getCustomerList(clients, new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                Log.e("customer: ",data.toString());
            }

            @Override
            public void onError(Exception error) {
                Log.e("customerError: ",error.getMessage().toString());
            }
        });*/
        /*Plan plan = new Plan();

        plan.setIdPlan("androidcourse");
        plan.setName("Course Java");
        plan.setDescription("Java advanced");
        plan.setAmount("30000");
        plan.setCurrency("COP");
        plan.setInterval("month");
        plan.setIntervalCount("1");
        plan.setTrialDays("10");
        plan.setIp("181.134.248.47");
        plan.setIva(5700.0F);
        plan.setIco(Float.valueOf("0"));
        plan.setPlanLink("https://github.com/epayco");
        plan.setGreetMessage("discounted Java");
        plan.setLinkExpirationDate("2025-03-15");
        plan.setSubscriptionLimit("8");
        plan.setImgUrl("https://epayco.com/wp-content/uploads/2023/04/logo-blanco.svg");
        plan.setDiscountValue(Float.valueOf("4000"));
        plan.setDiscountPercentage(19);
        plan.setTransactionalLimit(3);
        plan.setAdditionalChargePercentage(Float.valueOf("0.0"));
        plan.setFirstPaymentAdditionalCost(Float.valueOf("45700"));
        epayco.createPlan(plan, new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                Log.e("plan: ",data.toString());
            }

            @Override
            public void onError(Exception error) {
                Log.e("planError: ",error.getMessage().toString());
            }
        });
        */
/*
        epayco.getPlan("androidcourse", new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                Log.e("plan: ",data.toString());
            }

            @Override
            public void onError(Exception error) {
                Log.e("planError: ",error.getMessage().toString());
            }
        });*/
        /*PlanUpdate plan = new PlanUpdate();
        plan.setName("Course Java");
        plan.setDescription("Java advanced");
        plan.setAmount("35700");
        plan.setCurrency("COP");
        plan.setInterval("month");
        plan.setIntervalCount("1");
        plan.setTrialDays("0");
        plan.setIp("181.134.248.46");
        plan.setIva(5700.0F);
        plan.setIco(Float.valueOf("0"));
        //plan.setTransactionalLimit(2);
        plan.setAdditionalChargePercentage(Float.valueOf("0.0"));
        plan.setAfterPayment("massage after payment");
        epayco.update("androidcourse",plan, new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                Log.e("plan: ",data.toString());
            }

            @Override
            public void onError(Exception error) {
                Log.e("planError: ",error.getMessage().toString());
            }
        });*/

/*
        Subscription subscription = new Subscription();

        subscription.setTokenCard(tokenId);
        subscription.setCustomer(customerId);
        subscription.setIdPlan("androidcourse");
        subscription.setDoc_type("CC");
        subscription.setDoc_number("5234567");
        subscription.setUrlConfirmation("https:/secure.payco.co/restpagos/testRest/endpagopse.php");


        epayco.createSubscription(subscription, new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                Log.e("subscription: ",data.toString());
            }

            @Override
            public void onError(Exception error) {
               Log.e("subscriptionError: ",error.getMessage().toString());
            }
        });
        */

        /*
        epayco.getSubscription("7d345dcb9c0b84497019a22", new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                Log.e("subscription: ",data.toString());
            }

            @Override
            public void onError(Exception error) {
                Log.e("subscriptionError: ",error.getMessage().toString());
            }
        });
*//*
        epayco.getSubscriptionList(new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                Log.e("subscription: ",data.toString());
            }

            @Override
            public void onError(Exception error) {
                Log.e("subscriptionError: ",error.getMessage().toString());
            }
        });*/
        /*epayco.cancelSubscription("7d345dcb9c0b84497019a22", new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                Log.e("subscription: ",data.toString());
            }

            @Override
            public void onError(Exception error) {
                Log.e("subscriptionError: ",error.getMessage().toString());

            }
        });*/

        /*
        ChargeSub sub = new ChargeSub();

        sub.setIdPlan("androidcourse");
        sub.setCustomer(customerId);
        sub.setTokenCard(tokenId);
        sub.setDocType("CC");
        sub.setDocNumber("5234567");
        sub.setIp("181.134.248.46");

        epayco.chargeSubscription(sub, new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                Log.e("subscription: ",data.toString());
            }

            @Override
            public void onError(Exception error) {
                Log.e("subscriptionError: ",error.getMessage().toString());
            }
        });
*/
/*
        epayco.getBanks(new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                Log.e("pse: ",data.toString());
            }

            @Override
            public void onError(Exception error) {
                Log.e("pseError: ",error.getMessage().toString());
            }
        });*/
/*
        Pse pse = new Pse();

//Schema
        pse.setBank("1022");
        pse.setTypePerson("0");

//Required
        pse.setDocType("CC");
        pse.setDocNumber("1035851980");
        pse.setName("John");
        pse.setLastName("Doe");
        pse.setEmail("example@email.com");
        pse.setInvoice("OR-123466");
        pse.setDescription("Test Payment");
        pse.setValue("116000");
        pse.setTax("16000");
        pse.setTaxBase("100000");
        pse.setPhone("3010000001");
        pse.setCurrency("COP");
        pse.setCountry("CO");
        pse.setUrlResponse("https:/secure.payco.co/restpagos/testRest/endpagopse.php");
        pse.setUrlConfirmation("https:/secure.payco.co/restpagos/testRest/endpagopse.php");
        pse.setIp("190.000.000.000");

//Optional
        pse.setMethodConfirmation("GET");
        pse.setIco("0");
        pse.setExtra1("");
        pse.setExtra2("");
        pse.setExtra3("");
        pse.setCity("");
        pse.setDepto("");
        pse.setAddress("");

        epayco.createPseTransaction(pse, new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                Log.e("pse: ",data.toString());
            }

            @Override
            public void onError(Exception error) {
                Log.e("pseError: ",error.getMessage().toString());
            }
        });*/
        epayco.getPseTransaction("4633522", new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                Log.e("pse: ",data.toString());
            }

            @Override
            public void onError(Exception error) {
                Log.e("pseError: ",error.getMessage().toString());
            }
        });
        //rintln("Datos recibidos: ${data.toString()}")
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}