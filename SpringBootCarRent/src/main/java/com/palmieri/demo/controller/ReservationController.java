package com.palmieri.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.palmieri.demo.entities.Reservation;
import com.palmieri.demo.entities.User;
import com.palmieri.demo.entities.Vehicle;
import com.palmieri.demo.exception.BindingException;
import com.palmieri.demo.exception.NotFoundException;
import com.palmieri.demo.service.ReservationService;
import com.palmieri.demo.service.UserService;
import com.palmieri.demo.service.VehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@CrossOrigin(origins = "*", maxAge = 10800, allowedHeaders = "*")
@RequestMapping("api/reservation")
public class ReservationController {

    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private UserService userService;
    @Autowired
    private ResourceBundleMessageSource error;


    @InitBinder
    public void bindingPreparation(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        CustomDateEditor orderDateEditor = new CustomDateEditor(dateFormat, true);
        binder.registerCustomEditor(Date.class, orderDateEditor);
    }
    @GetMapping(value ="/showall",produces = "application/json")
    public ResponseEntity<List<Reservation>> getReservations(){
        logger.info("ottenendo tutte le reservations");

        List<Reservation> reservations= reservationService.readAll();
        return new ResponseEntity<List<Reservation>>(reservations, HttpStatus.OK);

    }
    @GetMapping(value ="/personal/{id}",produces = "application/json")
    public ResponseEntity<List<Reservation>> getReservationsByUserId(@PathVariable ("id") int id){
        logger.info("ottenendo tutte le reservations");
        User user = userService.readById(id);
        List<Reservation> reservations= reservationService.readByUser(user);
        return new ResponseEntity<List<Reservation>>(reservations, HttpStatus.OK);

    }

    @GetMapping(value ="/vehicle/{id}",produces = "application/json")
    public ResponseEntity<List<Reservation>> getReservationsByVehicleId(@PathVariable ("id") int id){
        logger.info("ottenendo tutte le reservations");
        Vehicle vehicle=vehicleService.readById(id);
        List<Reservation> reservations= reservationService.readByVehicle(vehicle);
        return new ResponseEntity<List<Reservation>>(reservations, HttpStatus.OK);

    }




    @PostMapping(value="/insert")
    public ResponseEntity<Reservation> insertReservation(@Valid @RequestBody Reservation reservation, BindingResult result) throws BindingException {
        logger.info("Salvo reservation "+ reservation);
        if(result.hasErrors()){
            String msg = error.getMessage(result.getFieldError(), LocaleContextHolder.getLocale());
            logger.warn(msg);
            throw new BindingException(msg);
        }
        reservationService.create(reservation);
        return new ResponseEntity<Reservation>(new HttpHeaders(), HttpStatus.CREATED);


    }
    @PostMapping(value="/update")
    public ResponseEntity<Reservation> updateReservation(@Valid @RequestBody Reservation reservation, BindingResult result) throws BindingException {
        logger.info("Salvo reservation "+ reservation);
        if(result.hasErrors()){
            String msg = error.getMessage(result.getFieldError(), LocaleContextHolder.getLocale());
            logger.warn(msg);
            throw new BindingException(msg);
        }
        reservationService.create(reservation);
        return new ResponseEntity<Reservation>(new HttpHeaders(), HttpStatus.CREATED);


    }



    @RequestMapping(value="/delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteReservation(@PathVariable("id") int id, Model model, HttpServletRequest request) throws NotFoundException {
        Reservation res = reservationService.readById(id);
        if(res==null){
            String msg = "articolo non presente";
            logger.warn(msg);
            throw new NotFoundException();
        }
        reservationService.delete(res);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode responseNode = mapper.createObjectNode();
        responseNode.put("code", HttpStatus.OK.toString());
        responseNode.put("message", "Eliminazione Resrvation "+ id + " eseguita con successo");
        return new ResponseEntity<>(responseNode, new HttpHeaders(), HttpStatus.OK);




    }

    @RequestMapping(value="/detail/{id}", produces="application/json")
    public ResponseEntity<Reservation> getReservationById(@PathVariable("id") int id ) throws NotFoundException{
        Reservation reservation = (Reservation) reservationService.readById(id);
        if(reservation==null){
            String error="La reservation " + id + "non è stato trovato";
            logger.warn(error);
            throw new NotFoundException(error);
            //return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<Reservation>(reservation, HttpStatus.OK);

        }

    }


}
