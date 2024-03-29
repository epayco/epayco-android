package co.epayco.android.models;

public class Safetypay {
    String cash;
    String endDate;
    String docType;
    String document;
    String name;
    String lastName;
    String email;
    String indCountry;
    String phone;
    String country;
    String city;
    String address;
    String ip;
    String currency;
    String invoice;
    String description;
    Float value;
    Float tax;
    Float ico;
    Float taxBase;
    Boolean testMode;
    String urlResponse;
    String urlResponsePointer;
    String urlConfirmation;
    String methodConfirmation;
    String typeIntegration;

    public Safetypay() { }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getDocType() { return this.docType; }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndCountry() {
        return indCountry;
    }

    public void setIndCountry(String indCountry) {
        this.indCountry = indCountry;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getTax() {
        return tax;
    }

    public void setTax(Float tax) {
        this.tax = tax;
    }

    public Float getTaxBase() {
        return taxBase;
    }

    public void setTaxBase(Float taxBase) {
        this.taxBase = taxBase;
    }

    public Float getIco() {
        return ico;
    }

    public void setIco(Float ico) {
        this.ico = ico;
    }

    public Boolean getTestMode() {
        return testMode;
    }

    public void setTestMode(Boolean testMode) {
        this.testMode = testMode;
    }

    public String getUrlResponse() {
        return urlResponse;
    }

    public void setUrlResponse(String urlResponse) {
        this.urlResponse = urlResponse;
    }

    public String getUrlResponsePointer() {
        return urlResponsePointer;
    }

    public void setUrlResponsePointer(String urlResponsePointer) {
        this.urlResponsePointer = urlResponsePointer;
    }

    public String getUrlConfirmation() {
        return urlConfirmation;
    }


    public void setUrlConfirmation(String urlConfirmation) {
        this.urlConfirmation = urlConfirmation;
    }

    public String getMethodConfirmation() {
        return methodConfirmation;
    }

    public void setMethodConfirmation(String methodConfirmation) {
        this.methodConfirmation = methodConfirmation;
    }

    public String getTypeIntegration() {
        return typeIntegration;
    }

    public void setTypeIntegration(String typeIntegration) {
        this.typeIntegration = typeIntegration;
    }
}
