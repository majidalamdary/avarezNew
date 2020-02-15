package com.sputa.avarez.model;

public class ItemDriving {


    private String City;
    private String Amount;
    private String BillId;
    private String PaymentId;
    private String Location;
    private String Type;
    private String DateTime;
    private String Delivery;
    private String PaymentUrl;

    public void setPaymentUrl(String paymentUrl) {
        PaymentUrl = paymentUrl;
    }

    public String getPaymentUrl() {
        return PaymentUrl;
    }

    public void setCity(String city) {
        City = city;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public void setBillId(String billId) {
        BillId = billId;
    }

    public void setPaymentId(String paymentId) {
        PaymentId = paymentId;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public void setDelivery(String delivery) {
        Delivery = delivery;
    }

    public String getCity() {
        return City;
    }

    public String getAmount() {
        return Amount;
    }

    public String getBillId() {
        return BillId;
    }

    public String getPaymentId() {
        return PaymentId;
    }

    public String getLocation() {
        return Location;
    }

    public String getType() {
        return Type;
    }

    public String getDateTime() {
        return DateTime;
    }

    public String getDelivery() {
        return Delivery;
    }

    public ItemDriving(String city, String amount, String billId, String paymentId, String location, String type, String dateTime, String delivery, String paymentUrl) {
        City = city;
        Amount = amount;
        BillId = billId;
        PaymentId = paymentId;
        Location = location;
        Type = type;
        DateTime = dateTime;
        Delivery = delivery;
        PaymentUrl = paymentUrl;
    }
}
