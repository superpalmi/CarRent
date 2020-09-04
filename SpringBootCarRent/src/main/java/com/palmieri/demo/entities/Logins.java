package com.palmieri.demo.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "logins")
public class Logins  implements Serializable
{

    private static final long serialVersionUID = -7252895579150453684L;

    @Id
    @Basic(optional = false)
    private String serie;

    @Basic(optional = false)
    private String nomeutente;

    @Basic(optional = false)
    private String token;

    @Temporal(TemporalType.TIME)
    @Basic(optional = false)
    private Date usato;

    public Logins()
    {}

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getNomeutente() {
        return nomeutente;
    }

    public void setNomeutente(String nomeutente) {
        this.nomeutente = nomeutente;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getUsato() {
        return usato;
    }

    public void setUsato(Date usato) {
        this.usato = usato;
    }


}
