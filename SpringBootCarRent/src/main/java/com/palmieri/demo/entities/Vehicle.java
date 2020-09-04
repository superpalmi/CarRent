package com.palmieri.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.palmieri.demo.validation.UniquePlate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "vehicle")
public class Vehicle implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "il campo non può essere vuoto")
    @Column(name="plate")
    private String plate;
    @NotEmpty(message = "il campo non può essere vuoto")
    @Column(name="brand")
    private String brand;
    @NotEmpty(message = "il campo non può essere vuoto")
    @Column(name="model")
    @NotEmpty(message = "il campo non può essere vuoto")
    private String model;
    @Column(name="immdate")
    @Temporal(TemporalType.DATE)
    private Date immdate;
    @NotEmpty(message = "il campo non può essere vuoto")
    @Column(name="type")
    private String type;
    @JsonIgnore
    @OneToMany (mappedBy = "vehicle", cascade = {CascadeType.REMOVE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Reservation> reservations = new HashSet<Reservation>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Date getImmdate() {
        return immdate;
    }

    public void setImmdate(Date immdate) {
        this.immdate = immdate;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations( Reservation reservation) {
        this.reservations.add(reservation) ;
    }
}
