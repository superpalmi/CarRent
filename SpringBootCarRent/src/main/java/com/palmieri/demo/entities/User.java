package com.palmieri.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.palmieri.demo.validation.UniqueEmail;
import com.palmieri.demo.validation.UniqueUserName;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@Entity
@Table(name = "user")
public class User implements Serializable {
    private static final long serialVersionUID = -667971422269719485L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;
    @Column(name="username", nullable = false)
    @NotEmpty(message = "il campo username non può essere vuoto")
    @Size(min=1, max=30, message = "il campo  deve essere compreso tra 1 e 30 caratteri")
    private String userName;
    @NotEmpty(message = "il campo password non può essere vuoto")
    @Size(min=1, max=30, message = "il campo  deve essere compreso tra 1 e 30 caratteri")
    @Column(name="password", nullable = false)
    private String password;
    @Column(name="email", nullable = false)
    @Size(min=1, max=30, message = "il campo  deve essere compreso tra 1 e 30 caratteri")
    @NotEmpty(message = "il campo email non può essere vuoto")
    @Email
    private String email;
    @Column(name="phone", nullable = false)
    @Size(min=1, max=30, message = "il campo  deve essere compreso tra 1 e 30 caratteri")
    private String phone;
    @Column(name="city", nullable = false)
    @Size(min=1, max=30, message = "il campo  deve essere compreso tra 1 e 30 caratteri")
    private String city;
    @NotEmpty(message ="il campo role non può essere vuoto")
    @Column(name="role", nullable = true)
    private String role;

    @OneToMany (mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Reservation> reservations = new HashSet<Reservation>();



    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    /*
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    } */

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations( Reservation reservation) {
        this.reservations.add(reservation) ;
    }



}
