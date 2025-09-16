package com.epayco.android.models;

public class DaviplataConfirm {
    String refPayco;
    String idSessionToken;
    String otp;

    public DaviplataConfirm() {}

    public String getRefPayco() {
        return refPayco;
    }

    public void setRefPayco(String refPayco) {
        this.refPayco = refPayco;
    }

    public String getIdSessionToken() {
        return idSessionToken;
    }

    public void setIdSessionToken(String idSessionToken) {
        this.idSessionToken = idSessionToken;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
