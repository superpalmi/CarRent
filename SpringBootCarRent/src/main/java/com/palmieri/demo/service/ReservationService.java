package com.palmieri.demo.service;

import com.palmieri.demo.entities.Reservation;
import com.palmieri.demo.entities.User;
import com.palmieri.demo.entities.Vehicle;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface ReservationService {
    public List<Reservation> readByDataInizio(Date inizio);
    public List<Reservation> readByDataFine(Date fine);
    public List<Reservation> readByUser(User user);
    public List<Reservation> readByVehicle(Vehicle vehicle);
    public List<Reservation> readAll();
    public Reservation readById(int id);
    public boolean checkVehicle(Reservation res);
    public boolean checkUser(Reservation res);

    void create(Reservation reservation);
    void update(Reservation reservation);
    void delete(Reservation reservation);




}
