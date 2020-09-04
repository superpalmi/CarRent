package com.palmieri.demo.service;

import com.palmieri.demo.dao.ReservationDao;
import com.palmieri.demo.entities.Reservation;
import com.palmieri.demo.entities.User;
import com.palmieri.demo.entities.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service("reservationService")
@Transactional
public class ReservationServiceImp implements ReservationService{
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

        Set<Reservation> userReservations = user.getReservations();

        boolean result = checkReservation(res, userReservations);
        return result;


    }

    private boolean checkReservation(Reservation res, Set<Reservation> reservations){
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

    public boolean checkVehicle(Reservation res){
        Vehicle vehicle;
        Set<Reservation> vehicleReservations=res.getVehicle().getReservations();
        boolean result = false;



        result = checkReservation(res, vehicleReservations);
        return result;


    }




}
