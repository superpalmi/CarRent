package com.palmieri.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Utente o Veicolo assente")
public class NoItemFoundException extends RuntimeException
{
    private static final long serialVersionUID = 809850541299086904L;

    private int id;

    public NoItemFoundException(int id)
    {
      this.id=id;
    }

    public int getId(){
        return this.id;
    }

}
