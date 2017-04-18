package co.epayco.android.models;

public class Card {

    String number;
    String month;
    String year;
    String cvc;

    public Card() {}

    public Card(String number, String month, String year, String cvc) {
        this.number = number;
        this.month = month;
        this.year = year;
        this.cvc = cvc;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }
}