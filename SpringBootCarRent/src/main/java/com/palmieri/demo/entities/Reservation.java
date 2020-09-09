package com.palmieri.demo.entities;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table (name = "reservation")
public class Reservation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    @Column(name="datainizio", nullable = false)
    @Temporal(TemporalType.DATE)

    private Date dataInizio;
    @Column(name="datafine", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataFine;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name= "vehicle", nullable = false)
    private Vehicle vehicle;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name= "user", nullable = false)
    private User user;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        id = id;
    }

    public Date getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(Date dataInizio) {
        this.dataInizio = dataInizio;
    }

    public Date getDataFine() {
        return dataFine;
    }

    public void setDataFine(Date dataFine) {
        this.dataFine = dataFine;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User userId) {
        this.user= userId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicleId) {
        this.vehicle = vehicleId;
    }



}
