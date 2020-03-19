Epayco android
=====

## Description

Epayco-android facilitates the collection of credit card information without having sensitive data on your server.

These epayco methods can be used to generate data in your android application. If you have an application that charges by credit card, you must use the ePayco-android library to prevent sensitive information from remaining on your server.

## Installation

Add maven https://jitpack.io to repositories

```gradle
allprojects {
  repositories {
    ......
    maven { url "https://jitpack.io" }
  }
}
```

Add the dependency

```gradle

  implementation 'com.github.epayco:epayco-android:fa55ec8329'


```

## Usage

```java
Authentication auth = new Authentication();

auth.setApiKey("YOU_PUBLIC_API_KEY");
auth.setPrivateKey("YOU_PRIVATE_API_KEY");
auth.setLang("ES");
auth.setTest(true);

Epayco epayco = new Epayco(auth);
```

## Validators

```java
//Validate number credit card
epayco.validNumberCard("4575623182290326");

//Validate dates
epayco.validExpiryMonth("12");
epayco.validExpiryYear("2018");

epayco.validateExpiryDate("12", "2018");

//Validate CVC
epayco.validateCVC("123");

//Validate Credit Card (card is a model card)
epayco.validCard(card);
```

### Create Token

```java
Card card = new Card();

card.setNumber("4575623182290326");
card.setMonth("12");
card.setYear("2018");
card.setCvc("123");

epayco.createToken(card, new EpaycoCallback() {
    @Override
    public void onSuccess(JSONObject data) throws JSONException {}

    @Override
    public void onError(Exception error) {}
});
```

### Customers

#### Create

```java
Client client = new Client();

client.setTokenId("Az9wdX4Wj3JRmr9NC");
client.setName("Cliente epayco");
client.setEmail("cliente@epayco.co");
client.setPhone("305274321");
client.setDefaultCard(true);

epayco.createCustomer(client, new EpaycoCallback() {
    @Override
    public void onSuccess(JSONObject data) throws JSONException {}

    @Override
    public void onError(Exception error) {}
});
```

#### Retrieve

```java
epayco.getCustomer("id_customer", new EpaycoCallback() {
    @Override
    public void onSuccess(JSONObject data) throws JSONException {}

    @Override
    public void onError(Exception error) {}
});
```

#### List

```java
epayco.getCustomerList(new EpaycoCallback() {
    @Override
    public void onSuccess(JSONObject data) throws JSONException {}

    @Override
    public void onError(Exception error) {}
});
```

#### Delete token customer

```java
Client client = new Client();
client.setFranchise("visa");
client.setMask("457562******0326");
client.setCustomer_id("id_customer");

epayco.deleteTokenCustomer(client, new EpaycoCallback() {
    @Override
    public void onSuccess(JSONObject data) throws JSONException {}

    @Override
    public void onError(Exception error) {}
});
```

#### add new token default to card existed

```java
Client client = new Client();

client.setCustomer_id("id_customer");
client.setTokenId("**********zL4gFB");
client.setFranchise("american-express");
client.setMask("373118*****7642");

epayco.addTokenDefault(client, new EpaycoCallback() {
    @Override
    public void onSuccess(JSONObject data) throws JSONException {}

    @Override
    public void onError(Exception error) {}
});
```

#### add new token to customer existed

```java
Client client = new Client();

client.setTokenId("Az9wdX4Wj3JRmr9NC");
client.setCustomer_id("id_customer");

epayco.addNewToken(client, new EpaycoCallback() {
    @Override
    public void onSuccess(JSONObject data) throws JSONException {}

    @Override
    public void onError(Exception error) {}
});
```


### Plans

#### Create

```java
Plan plan = new Plan();

plan.setIdPlan("reactcourse");
plan.setName("Course React");
plan.setDescription("Course react advanced");
plan.setAmount("30000");
plan.setCurrency("COP");
plan.setInterval("month");
plan.setIntervalCount("1");
plan.setTrialDays("0");

epayco.createPlan(plan, new EpaycoCallback() {
    @Override
    public void onSuccess(JSONObject data) throws JSONException {}

    @Override
    public void onError(Exception error) {}
});
```

#### Retrieve

```java
epayco.getPlan("reactcourse", new EpaycoCallback() {
    @Override
    public void onSuccess(JSONObject data) throws JSONException {}

    @Override
    public void onError(Exception error) {}
});
```

#### List

```java
epayco.getPlanList(new EpaycoCallback() {
    @Override
    public void onSuccess(JSONObject data) throws JSONException {}

    @Override
    public void onError(Exception error) {}
});
```

### Subscriptions

#### Create

```java
Subscription subscription = new Subscription();

subscription.setTokenCard("id_token");
subscription.setCustomer("id_customer");
subscription.setIdPlan("reactcourse");
subscription.setDoc_type("CC");
subscription.setDoc_number("5234567");
/*Optional parameter: if these parameter it's not send, system get ePayco dashboard's url_confirmation*/
subscription.setUrlConfirmation("https:/secure.payco.co/restpagos/testRest/endpagopse.php");



epayco.createSubscription(subscription, new EpaycoCallback() {
    @Override
    public void onSuccess(JSONObject data) throws JSONException {}

    @Override
    public void onError(Exception error) {}
});
```

#### Retrieve

```java
epayco.getSubscription("id_subscription", new EpaycoCallback() {
    @Override
    public void onSuccess(JSONObject data) throws JSONException {}

    @Override
    public void onError(Exception error) {}
});
```

#### List

```java
epayco.getSubscriptionList(new EpaycoCallback() {
    @Override
    public void onSuccess(JSONObject data) throws JSONException {}

    @Override
    public void onError(Exception error) {}
});
```

#### Pay Subscription

```java
ChargeSub sub = new ChargeSub();

sub.setIdPlan("reactcourse");
sub.setCustomer("id_customer");
sub.setTokenCard("id_token");
sub.setDocType("CC");
sub.setDocNumber("5234567");
sub.setIp("190.000.000.000");/*This is the client's IP, it is required*/

epayco.chargeSubscription(sub, new EpaycoCallback() {
    @Override
    public void onSuccess(JSONObject data) throws JSONException {}

    @Override
    public void onError(Exception error) {}
});
```

### PSE

#### Create

```java
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
pse.setInvoice("OR-1234");
pse.setDescription("Test Payment");
pse.setValue("116000");
pse.setTax("16000");
pse.setTaxBase("100000");
pse.setPhone("3010000001");
pse.setCurrency("COP");
pse.setCountry("CO");
pse.setUrlResponse("https:/secure.payco.co/restpagos/testRest/endpagopse.php");
pse.setUrlConfirmation("https:/secure.payco.co/restpagos/testRest/endpagopse.php");
pse.setIp("190.000.000.000");/*This is the client's IP, it is required*/

//Optional
pse.setExtra1("");
pse.setExtra2("");
pse.setExtra3("");
pse.setCity("");
pse.setDepto("");
pse.setAddress("");

epayco.createPseTransaction(pse, new EpaycoCallback() {
    @Override
    public void onSuccess(JSONObject data) throws JSONException {}

    @Override
    public void onError(Exception error) {}
});
```

#### Retrieve

```java
epayco.getPseTransaction("transaction_id", new EpaycoCallback() {
    @Override
    public void onSuccess(JSONObject data) throws JSONException {}

    @Override
    public void onError(Exception error) {}
});
```

#### Split Payments

Previous requirements: https://docs.epayco.co/tools/split-payment

```java
Pse pse = new Pse();
//Other customary parameters...
                pse.setSplitpayment ("true");
                pse.setSplit_app_id ("19520");
                pse.setSplit_merchant_id ("17511");
                pse.setSplit_type ("02");
                pse.setSplit_primary_receiver ("19520");
                pse.setSplit_primary_receiver_fee ("10");

```

### Cash

#### Create

```java
Cash cash = new Cash();

//Schema
cash.setType("efecty");
cash.setEndDate("2017-12-05");

//Required
cash.setDocType("CC");
cash.setDocNumber("1035851980");
cash.setName("John");
cash.setLastName("Doe");
cash.setEmail("example@email.com");
cash.setInvoice("OR-1234");
cash.setDescription("Test Payment");
cash.setValue("116000");
cash.setTax("16000");
cash.setTaxBase("100000");
cash.setPhone("3010000001");
cash.setCurrency("COP");
cash.setIp("190.000.000.000");/*This is the client's IP, it is required*/

//Optional
cash.setUrlResponse("");
cash.setUrlConfirmation("");
cash.setExtra1("");
cash.setExtra2("");
cash.setExtra3("");
cash.setCountry("");
cash.setCity("");
cash.setDepto("");
cash.setAddress("");

epayco.createCashTransaction(cash, new EpaycoCallback() {
    @Override
    public void onSuccess(JSONObject data) throws JSONException {}

    @Override
    public void onError(Exception error) {}
});

```

#### List

```java
 cash.setType("redservi");
 cash.setType("gana");
 cash.setType("baloto"); //expiration date can not be longer than 30 days
 cash.setType("redservi");//expiration date can not be longer than 30 days
 cash.setType("puntored");//expiration date can not be longer than 30 days
```

#### Retrieve

```java
epayco.getReferencePayment("epayco_reference", new EpaycoCallback() {
    @Override
    public void onSuccess(JSONObject data) throws JSONException {}

    @Override
    public void onError(Exception error) {}
});
```

#### Split Payments

Previous requirements: https://docs.epayco.co/tools/split-payment

```java
Cash cash = new Cash();
//Other customary parameters...
                cash.setSplitpayment ("true");
                cash.setSplit_app_id ("19520");
                cash.setSplit_merchant_id ("17511");
                cash.setSplit_type ("02");
                cash.setSplit_primary_receiver ("19520");
                cash.setSplit_primary_receiver_fee ("10");

```


### Payment

#### Create

```java
Charge charge = new Charge();

//Schema
charge.setTokenCard("2L9R3ey5paBTN8gzr");
charge.setCustomerId("oDFmhNLasGwAhSDWw");

//Required
charge.setDocType("CC");
charge.setDocNumber("1035851980");
charge.setName("John");
charge.setLastName("Doe");
charge.setEmail("example@email.com");
charge.setInvoice("OR-1234");
charge.setDescription("Test Payment");
charge.setValue("116000");
charge.setTax("16000");
charge.setTaxBase("100000");
charge.setCurrency("COP");
charge.setDues("12");
charge.setIp("190.000.000.000");/*This is the client's IP, it is required*/

//Optional
charge.setUse_default_card_customer(true);/*if the user wants to be charged with the card that the customer currently has as default =true*/
charge.setUrlResponse("");
charge.setUrlConfirmation("");
charge.setExtra1("");
charge.setExtra2("");
charge.setExtra3("");
charge.setCity("");
charge.setDepartament("");
charge.setCountry("");
charge.setAddress("");

epayco.createCharge(charge, new EpaycoCallback() {
    @Override
    public void onSuccess(JSONObject data) throws JSONException {}

    @Override
    public void onError(Exception error) {}
});
```

#### Retrieve

```java
epayco.getReferencePayment("epayco_reference", new EpaycoCallback() {
    @Override
    public void onSuccess(JSONObject data) throws JSONException {}

    @Override
    public void onError(Exception error) {}
});
```
#### Split Payments

Previous requirements: https://docs.epayco.co/tools/split-payment

```java
Charge charge = new Charge();
//Other customary parameters...
                charge.setSplitpayment ("true");
                charge.setSplit_app_id ("19520");
                charge.setSplit_merchant_id ("17511");
                charge.setSplit_type ("02");
                charge.setSplit_primary_receiver ("19520");
                charge.setSplit_primary_receiver_fee ("10");

```
