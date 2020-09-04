package com.palmieri.demo.exception;

import lombok.Data;

import java.util.Date;

public class ErrorResponse {
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCodice() {
        return codice;
    }

    public void setCodice(int codice) {
        this.codice = codice;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    private Date date=new Date();
    private int codice;
    private String messaggio;
}
