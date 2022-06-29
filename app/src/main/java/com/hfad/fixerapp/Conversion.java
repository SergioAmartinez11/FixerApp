package com.hfad.fixerapp;

public class Conversion{
    private String from;
    private String to;
    private String amount;
    private String result;

    public Conversion(){

    }

    public Conversion(String from, String to, String amount, String result) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
