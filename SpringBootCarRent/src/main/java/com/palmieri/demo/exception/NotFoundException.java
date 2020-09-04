package com.palmieri.demo.exception;

public class NotFoundException extends Exception {
    private String messaggio="Elemento ricercato non trovato";
    public NotFoundException(){
        super();

    }
    public NotFoundException(String messaggio){
        super(messaggio);
        this.messaggio=messaggio;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }
}
