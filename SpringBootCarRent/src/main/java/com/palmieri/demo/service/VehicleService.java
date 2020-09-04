package com.palmieri.demo.service;

import com.palmieri.demo.entities.User;
import com.palmieri.demo.entities.Vehicle;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface VehicleService {
    List<Vehicle> readByBrand(String brand);

    List<Vehicle> readByModel(String model);

    List<Vehicle> readAll();

    List<Vehicle> readByImmDate(Date immdate);

    List<Vehicle> readByType(String type);
    Set<Vehicle> readByDate(Date dataInizio, Date dataFine);

    Vehicle readByPlate(String plate);
    Vehicle readById(int id);
    boolean isPlateAlreadyInUse(String plate);
    public boolean exists(Vehicle vehicle);



    void saveOrUpdate(Vehicle vehicle);



    void create(Vehicle vehicle);
    void update(Vehicle vehicle);
    void delete(Vehicle vehicle);
}
