package com.palmieri.demo.exception;

public class BindingException extends Exception{
    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    private String messaggio;

    public BindingException(String messaggio) {
        this.messaggio = messaggio;
    }

    public BindingException(String message, String messaggio) {
        super(message);
        this.messaggio = messaggio;
    }
}
