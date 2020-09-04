package com.palmieri.demo.service;

import com.palmieri.demo.dao.VehicleDao;
import com.palmieri.demo.entities.Reservation;
import com.palmieri.demo.entities.User;
import com.palmieri.demo.entities.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("vehicleService")
@Transactional
public class VehicleServiceImp implements VehicleService{
    @Qualifier("vehicleDao")
    @Autowired
    private VehicleDao vehicleRepository;
    @Override
    public List<Vehicle> readByBrand(String brand) {
       List<Vehicle> vehicles=vehicleRepository.findByBrand(brand);
       return vehicles;
    }

    @Override
    public List<Vehicle> readByModel(String model) {
        List<Vehicle> vehicles=vehicleRepository.findByModel(model);
        return vehicles;
    }

    @Override
    public List<Vehicle> readAll() {
        List<Vehicle> vehicles=vehicleRepository.findAll();
        return vehicles;
    }

    @Override
    public List<Vehicle> readByImmDate(Date immdate) {
        List<Vehicle> vehicles=vehicleRepository.findByImmdate(immdate);
        return vehicles;
    }

    @Override
    public List<Vehicle> readByType(String type) {
        List<Vehicle> vehicles=vehicleRepository.findByType(type);
        return vehicles;
    }

    @Override
    public Vehicle readByPlate(String plate) {
        Vehicle vehicle=vehicleRepository.findByPlate(plate);
        return vehicle;
    }
    @Override
    public Vehicle readById(int id) {
        Vehicle vehicle=vehicleRepository.findById(id);
        return vehicle;
    }

    @Override
    public boolean isPlateAlreadyInUse(String plate) {
        boolean vehicleInDb=true;
        if(readByPlate(plate)==null) vehicleInDb=false;
        return vehicleInDb;

    }

    @Override
    public boolean exists(Vehicle vehicle) {
        boolean exists=false;
        Vehicle v=readByPlate(vehicle.getPlate());
        if(v!=null){
            return exists=true;
        }
        return exists;
    }

    @Override
    public void saveOrUpdate(Vehicle vehicle) {
        if (exists(vehicle)){
            update(vehicle);
        }else create(vehicle);

    }


    @Override
    public void create(Vehicle vehicle) {
        vehicleRepository.save(vehicle);


    }

    @Override
    public void update(Vehicle vehicle) {
        vehicleRepository.save(vehicle);


    }

    @Override
    public void delete(Vehicle vehicle) {

        vehicleRepository.delete(vehicle);


    }

    @Override
    public Set<Vehicle> readByDate(Date dataInizio, Date dataFine) {
        List<Vehicle> allvehicles = readAll();
        Set<Vehicle> vehicles=new HashSet<Vehicle>();
        boolean isFree=false;
        for(int i=0; i<allvehicles.size(); i++){
            Set<Reservation> reservations = allvehicles.get(i).getReservations();
            for (Reservation res: reservations) {
                if((res.getDataInizio().before(dataInizio)&&res.getDataFine().before(dataFine))||(res.getDataInizio().after(dataInizio)&&res.getDataFine().after(dataFine))){
                    isFree=true;
                }else isFree=false;

            }
            if(isFree==true){
                vehicles.add(allvehicles.get(i));
            }
        }
        return vehicles;


    }
}
