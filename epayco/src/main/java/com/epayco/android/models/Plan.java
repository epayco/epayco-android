package com.epayco.android.models;

public class Plan {

    String idPlan;
    String name;
    String description;
    String amount;
    String currency;
    String interval;
    String intervalCount;
    String trialDays;
    String ip;
    Float iva;
    Float ico;
    String planLink;
    String greetMessage;
    String linkExpirationDate;
    String subscriptionLimit;
    String imgUrl;
    Float discountValue;
    int discountPercentage;
    int transactionalLimit;
    Float additionalChargePercentage;
    Float firstPaymentAdditionalCost;
    String afterPayment;
    public Plan() {}
    public String getIdPlan() {
        return idPlan;
    }
    public void setIdPlan(String idPlan) {
        this.idPlan = idPlan;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public String getInterval() {
        return interval;
    }
    public void setInterval(String interval) {
        this.interval = interval;
    }
    public String getIntervalCount() {
        return intervalCount;
    }
    public void setIntervalCount(String intervalCount) {
        this.intervalCount = intervalCount;
    }
    public String getTrialDays() {
        return trialDays;
    }
    public void setTrialDays(String trialDays) {
        this.trialDays = trialDays;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public Float getIva() {
        return iva;
    }
    public void setIva(Float iva) {
        this.iva = iva;
    }
    public Float getIco() {
        return ico;
    }
    public void setIco(Float ico) {
        this.ico = ico;
    }
    public String getPlanLink() {
        return planLink;
    }
    public void setPlanLink(String planLink) {
        this.planLink = planLink;
    }
    public String getGreetMessage() {
        return greetMessage;
    }
    public void setGreetMessage(String greetMessage) {
        this.greetMessage = greetMessage;
    }
    public String getLinkExpirationDate() {
        return linkExpirationDate;
    }
    public void setLinkExpirationDate(String linkExpirationDate) {
        this.linkExpirationDate = linkExpirationDate;
    }
    public String getSubscriptionLimit() {
        return subscriptionLimit;
    }
    public void setSubscriptionLimit(String subscriptionLimit) {
        this.subscriptionLimit = subscriptionLimit;
    }
    public String getImgUrl() {
        return imgUrl;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    public Float getDiscountValue() {
        return discountValue;
    }
    public void setDiscountValue(Float discountValue) {
        this.discountValue = discountValue;
    }
    public int getDiscountPercentage() {
        return discountPercentage;
    }
    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
    public int getTransactionalLimit() {
        return transactionalLimit;
    }
    public void setTransactionalLimit(int transactionalLimit) {
        this.transactionalLimit = transactionalLimit;
    }
    public Float getAdditionalChargePercentage() {
        return additionalChargePercentage;
    }
    public void setAdditionalChargePercentage(Float additionalChargePercentage) {
        this.additionalChargePercentage = additionalChargePercentage;
    }
    public Float getFirstPaymentAdditionalCost() {
        return firstPaymentAdditionalCost;
    }
    public void setFirstPaymentAdditionalCost(Float firstPaymentAdditionalCost) {
        this.firstPaymentAdditionalCost = firstPaymentAdditionalCost;
    }

    public String  getAfterPayment() {
        return afterPayment;
    }
    public void setAfterPayment(String afterPayment) {
        this.afterPayment = afterPayment;
    }


}