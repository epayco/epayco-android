package co.epayco.android.models;

public class PlanUpdate {
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
    int transactionalLimit;
    Float additionalChargePercentage;
    String afterPayment;
    public PlanUpdate() {}


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
    public String  getAfterPayment() {
        return afterPayment;
    }
    public void setAfterPayment(String afterPayment) {
        this.afterPayment = afterPayment;
    }

}
