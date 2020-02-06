package co.epayco.android.models;


public class Cash {

    //Schema
    String endDate;
    String type;

    //Required
    String docType;
    String docNumber;
    String name;
    String lastName;
    String email;
    String invoice;
    String description;
    String value;
    String tax;
    String phone;
    String taxBase;
    String currency;
    String ip;

    //Optional
    String urlResponse;
    String urlConfirmation;
    String extra1;
    String extra2;
    String extra3;
    String city;
    String depto;
    String country;
    String address;
    String splitpayment;
    String split_app_id;
    String split_merchant_id;
    String split_type;
    String split_primary_receiver;
    String split_primary_receiver_fee;
    

    public Cash() {}

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTaxBase() {
        return taxBase;
    }

    public void setTaxBase(String taxBase) {
        this.taxBase = taxBase;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getUrlResponse() {
        return urlResponse;
    }

    public void setUrlResponse(String urlResponse) {
        this.urlResponse = urlResponse;
    }

    public String getUrlConfirmation() {
        return urlConfirmation;
    }

    public void setUrlConfirmation(String urlConfirmation) {
        this.urlConfirmation = urlConfirmation;
    }

    public String getExtra1() {
        return extra1;
    }

    public void setExtra1(String extra1) {
        this.extra1 = extra1;
    }

    public String getExtra2() {
        return extra2;
    }

    public void setExtra2(String extra2) {
        this.extra2 = extra2;
    }

    public String getExtra3() {
        return extra3;
    }

    public void setExtra3(String extra3) {
        this.extra3 = extra3;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDepto() {
        return depto;
    }

    public void setDepto(String depto) {
        this.depto = depto;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

     public String getSplitpayment() {
        return splitpayment;
    }

    public void setSplitpayment(String splitpayment) {
        this.splitpayment = splitpayment;
    }

     public String getSplit_app_id() {
        return split_app_id;
    }

    public void setSplit_app_id(String split_app_id) {
        this.split_app_id = split_app_id;
    }

     public String getSplit_merchant_id() {
        return split_merchant_id;
    }

    public void setSplit_merchant_id(String split_merchant_id) {
        this.split_merchant_id = split_merchant_id;
    }
    
     public String getSplit_type() {
        return split_type;
    }

    public void setSplit_type(String split_type) {
        this.split_type = split_type;
    }
    
    public String getSplit_primary_receiver() {
        return split_primary_receiver;
    }

    public void setSplit_primary_receiver(String split_primary_receiver) {
        this.split_primary_receiver = split_primary_receiver;
    }

    public String getSplit_primary_receiver_fee() {
        return split_primary_receiver_fee;
    }

    public void setSplit_primary_receiver_fee(String split_primary_receiver_fee) {
        this.split_primary_receiver_fee = split_primary_receiver_fee;
    }

        public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
