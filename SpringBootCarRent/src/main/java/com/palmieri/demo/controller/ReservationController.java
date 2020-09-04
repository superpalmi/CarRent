package com.palmieri.demo.controller;

import com.palmieri.demo.entities.Reservation;
import com.palmieri.demo.entities.User;
import com.palmieri.demo.entities.Vehicle;
import com.palmieri.demo.service.ReservationService;
import com.palmieri.demo.service.UserService;
import com.palmieri.demo.service.VehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("api/reservation")
public class ReservationController {

    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private UserService userService;

    List<Reservation> MainRecordSet;

    @InitBinder
    public void bindingPreparation(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        CustomDateEditor orderDateEditor = new CustomDateEditor(dateFormat, true);
        binder.registerCustomEditor(Date.class, orderDateEditor);
    }

    @RequestMapping(value="/insert/{id}", method= RequestMethod.GET)
    public String getInsertReservation(@PathVariable("id") int id, Model model){
        Vehicle vehicle=vehicleService.readById(id);
        Reservation reservation = new Reservation();
        reservation.setVehicle(vehicle);
        //String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //User user =  userService.readByUsername(username);
        //reservation.setUser(user);




        model.addAttribute("Titolo", "Prenota il veicolo");
        model.addAttribute("newReservation", reservation);




        return "insertReservation";




    }
    @RequestMapping(value="/update/{id}", method=RequestMethod.GET)
    public String getUpdateReservation(@PathVariable("id") int id, Model model){
        Reservation res = reservationService.readById(id);
        model.addAttribute("Titolo", "Modifica la Prenotazione");
        model.addAttribute("newReservation", res);
        return "insertReservation";


    }
    @RequestMapping(value="/update/{id}", method = RequestMethod.POST)
    public String updateReservation(@PathVariable("id") int id, @Valid @ModelAttribute("newReservation") Reservation res, BindingResult result, Model model){
        if(result.hasErrors()){
            return "insertReservation";
        }

        Reservation old = reservationService.readById(id);
        res.setUser(old.getUser());
        res.setVehicle(old.getVehicle());




        if(reservationService.checkUser(res)&&reservationService.checkVehicle(res)){
           old.setDataInizio(res.getDataInizio());
           old.setDataFine(res.getDataFine());
            reservationService.update(old);

            User attr = userService.readById(old.getUser().getId());
            model.addAttribute("user", attr);
            return "userDetail";
        }else {
            model.addAttribute("message", "veicolo non disponibile nelle date selezionate");
            model.addAttribute("Titolo", "modifica la prenotazione");
            model.addAttribute("newReservation", old);
            return "insertReservation";
        }



    }
    @RequestMapping(value="/insert/{id}", method=RequestMethod.POST)
    public String insertReservation(@PathVariable("id") int id,@Valid @ModelAttribute("newReservation") Reservation res, BindingResult result, Model model){
        if(result.hasErrors()){
            return "insertReservation";
        }
        Vehicle vehicle=vehicleService.readById(id);
        res.setVehicle(vehicle);
       // String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //User user =  userService.readByUsername(username);
        //res.setUser(user);



        if(reservationService.checkUser(res)&&reservationService.checkVehicle(res)){
            reservationService.create(res);
            //User attr = userService.ReadById(res.getUser().getId());


            model.addAttribute("user", userService.readById(res.getUser().getId()));
            return "userDetail";
        }else {
            model.addAttribute("message", "veicolo non disponibile nelle date selezionate");
            model.addAttribute("Titolo", "modifica la prenotazione");
            model.addAttribute("newReservation", res);
            return "insertReservation";
        }


    }
    @RequestMapping(value="/readall/", method = RequestMethod.GET)
    public String reservationReadall(Model model ){
        List<Reservation> reservations=reservationService.readAll();
        model.addAttribute("reservations", reservations);
        return "reservationReadall";


    }

    @RequestMapping(value="/delete/{id}", method = RequestMethod.GET)
    public String deleteReservation(@PathVariable("id") int id, Model model, HttpServletRequest request)
    {
        Reservation reservation = reservationService.readById(id);
        reservationService.delete(reservation);
        model.addAttribute("reservations", reservationService.readAll());
        return "reservationReadall";




    }


}
