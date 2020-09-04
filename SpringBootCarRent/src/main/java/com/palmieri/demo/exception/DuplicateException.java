package com.palmieri.demo.exception;

public class DuplicateException extends Exception{
    String messaggio;

    public DuplicateException(String messaggio) {
        this.messaggio = messaggio;
    }

    public DuplicateException(String message, String messaggio) {
        super(message);
        this.messaggio = messaggio;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }



}
