package co.epayco.android.models;

public class Subscription {

    String idPlan;
    String customer;
    String tokenCard;
    String ip;
      //Optional
    String urlConfirmation;

    public Subscription() {}

    public String getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(String idPlan) {
        this.idPlan = idPlan;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getTokenCard() {
        return tokenCard;
    }

    public void setTokenCard(String tokenCard) {
        this.tokenCard = tokenCard;
    }

    public String getUrlConfirmation() {
        return urlConfirmation;
    }

    public void setUrlConfirmation(String urlConfirmation) {
        this.urlConfirmation = urlConfirmation;
    }
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}