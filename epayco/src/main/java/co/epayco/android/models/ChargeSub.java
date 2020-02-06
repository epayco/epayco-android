package co.epayco.android.models;


public class ChargeSub {

    String idPlan;
    String customer;
    String tokenCard;
    String docType;
    String docNumber;
    String ip;
    public ChargeSub() {}

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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
