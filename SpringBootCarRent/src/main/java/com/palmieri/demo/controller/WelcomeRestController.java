package com.palmieri.demo.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class WelcomeRestController {


    @GetMapping(value="/test")
    public String getGreetings()
    {
        return "Ciao sono il tuo primo web service";
    }

    @GetMapping(value="/welcome/{nome}")
    public String getName(@PathVariable("nome") String nome)
    {
        if(nome.equals("Riccardo")){
            throw new RuntimeException("L'utente Riccardo è disabilitato");
        }
        return "\"Ciao "+nome+" sono il tuo primo web service\"";
    }
}
