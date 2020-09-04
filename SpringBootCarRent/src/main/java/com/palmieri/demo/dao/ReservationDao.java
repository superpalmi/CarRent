package com.palmieri.demo.dao;

import com.palmieri.demo.entities.Reservation;
import com.palmieri.demo.entities.User;
import com.palmieri.demo.entities.Vehicle;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
@Repository("reservationDao")
public interface ReservationDao extends PagingAndSortingRepository<Reservation, String> {
    List<Reservation> findAll();
    List<Reservation> findByUserId(int userid);
    List<Reservation> findByVehicleId(int vehicleid);
    List<Reservation> findByDataInizio(Date dataInizio);
    List<Reservation> findByDataFine(Date dataFine);
    List<Reservation> findByDataInizioAfter(Date dataInizio);
    List<Reservation> findByDataFineAfter(Date dataFine);
    Reservation findById(int id);
    Reservation findByUserAndVehicle(User user, Vehicle vehicle);






}
