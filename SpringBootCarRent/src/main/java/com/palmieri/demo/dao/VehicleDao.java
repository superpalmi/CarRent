package com.palmieri.demo.dao;

import com.palmieri.demo.entities.Vehicle;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.ListView;
import java.util.Date;
import java.util.List;
import java.util.Set;
@Repository("vehicleDao")
public interface VehicleDao extends PagingAndSortingRepository<Vehicle, String> {

    List<Vehicle> findAll();
    List<Vehicle> findByBrand(String brand);
    List<Vehicle> findByModel(String model);
    List<Vehicle> findByType(String type);
    List<Vehicle> findByImmdate(Date immdate);
    Vehicle findById(int id);
    Vehicle findByPlate(String plate);










}
