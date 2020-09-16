package com.palmieri.demo.service;

import com.palmieri.demo.dao.ReservationDao;
import com.palmieri.demo.entities.Reservation;
import com.palmieri.demo.entities.User;
import com.palmieri.demo.entities.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service("reservationService")
@Transactional
public class ReservationServiceImp implements ReservationService{
    @Autowired
    private VehicleService vehicleService;
    @Qualifier("reservationDao")
    @Autowired
    ReservationDao reservationRepository;

    @Override
    public List<Reservation> readByDataInizio(Date inizio) {
        List<Reservation> reservations = reservationRepository.findByDataInizio(inizio);
        return reservations;
    }

    @Override
    public List<Reservation> readByDataFine(Date fine) {
        List<Reservation> reservations = reservationRepository.findByDataFine(fine);
        return reservations;
    }

    @Override
    public List<Reservation> readByUser(User user) {
        List<Reservation> reservations = reservationRepository.findByUserId(user.getId());
        return reservations;
    }

    @Override
    public List<Reservation> readByVehicle(Vehicle vehicle) {
        List<Reservation> reservations = reservationRepository.findByVehicleId(vehicle.getId());
        return reservations;
    }
    @Override
    public List<Reservation> readAll(){
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations;

    }
    @Override
    public Reservation readById(int id){
        Reservation reservation = reservationRepository.findById(id);
        return reservation;
    }



    @Override
    public void create(Reservation reservation) {


            reservationRepository.save(reservation);


    }



    @Override
    public void update(Reservation reservation) {
        reservationRepository.save(reservation);

    }

    @Override
    public void delete(Reservation reservation) {
        reservationRepository.delete(reservation);

    }


    public boolean checkUser(Reservation res){

        User user = res.getUser();

        List <Reservation> userReservations = new ArrayList<>(user.getReservations());

        boolean result = checkReservation(res, userReservations);
        return result;


    }

    private boolean checkReservation(Reservation res, List<Reservation> reservations){
        boolean result = false;

        Date dataInizio = res.getDataInizio();
        Date dataFine = res.getDataFine();


        for(Reservation r: reservations){
            Date dI =r.getDataInizio();
            Date dF =r.getDataFine();

            if(((dataInizio.after(dI) && dataInizio.before(dF)) || (dataFine.after(dI) && dataFine.before(dF)))||((dataInizio.equals(dI))&&(dataFine.equals(dF)))){
                return result;

            }


        }
        return result = true;



    }

    public List<Vehicle> bookableVehicles(Reservation res){
        List<Reservation> userReservations=  readByUser(res.getUser());
        List<Vehicle> vehicles=vehicleService.readAll();
        List<Vehicle> bookable= new ArrayList<Vehicle>();
        for(Vehicle v: vehicles){
            List<Reservation> vehicleReservations=new ArrayList<>(v.getReservations());



            if(checkReservation(res,vehicleReservations)){
                bookable.add(v);
            }

        }

        if(checkUser(res)){
            return bookable;
        }else return new ArrayList<>();




    }




}
