package com.palmieri.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.palmieri.demo.entities.Reservation;
import com.palmieri.demo.entities.User;
import com.palmieri.demo.entities.Vehicle;
import com.palmieri.demo.exception.BindingException;
import com.palmieri.demo.exception.DuplicateException;
import com.palmieri.demo.exception.NotFoundException;
import com.palmieri.demo.service.ReservationService;
import com.palmieri.demo.service.VehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
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
import java.util.Set;

@RestController
@CrossOrigin(origins = "*", maxAge = 10800, allowedHeaders = "*")
@RequestMapping("api/vehicle")
public class VehicleController {



    private static final Logger logger = LoggerFactory.getLogger(VehicleController.class);
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ResourceBundleMessageSource error;







    @InitBinder
    public void bindingPreparation(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        CustomDateEditor orderDateEditor = new CustomDateEditor(dateFormat, true);
        binder.registerCustomEditor(Date.class, orderDateEditor);
    }
    //aggiungere filtri in base alle date inserite

    @GetMapping(value ="/showall",produces = "application/json")
    public ResponseEntity<List<Vehicle>> getVehicles(Model model){
        logger.info("ottenendo tutti gli user");

        List<Vehicle> vehicles= vehicleService.readAll();
        return new ResponseEntity<>(vehicles, HttpStatus.OK);

    }


    @PostMapping(value="/insert")
    public ResponseEntity<Vehicle> insertVehicle(@Valid @RequestBody Vehicle vehicle, BindingResult result) throws BindingException, DuplicateException {
        if(result.hasErrors()){
            String msg = error.getMessage(result.getFieldError(), LocaleContextHolder.getLocale());
            logger.warn(msg);
            throw new BindingException(msg);
        }
        vehicleService.create(vehicle);
        return new ResponseEntity<Vehicle>(new HttpHeaders(), HttpStatus.CREATED);

    }


    @PostMapping(value="/update")
    public ResponseEntity<Vehicle> updateVehicle(@Valid @RequestBody Vehicle vehicle, BindingResult result) throws BindingException, DuplicateException {
        if(result.hasErrors()){
            String msg = error.getMessage(result.getFieldError(), LocaleContextHolder.getLocale());
            logger.warn(msg);
            throw new BindingException(msg);
        }
        vehicleService.create(vehicle);
        return new ResponseEntity<Vehicle>(new HttpHeaders(), HttpStatus.CREATED);

    }

    @RequestMapping(value="/detail/{id}", produces = "application/json")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable("id") int id) throws
            NotFoundException {
        //String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Vehicle vehicle = (Vehicle) vehicleService.readById(id);
        if(vehicle==null){
            String error="L'username: " + id + "non è stato trovato";
            logger.warn(error);
            throw new NotFoundException(error);
            //return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<Vehicle>(vehicle, HttpStatus.OK);

        }

    }

    @RequestMapping(value="/detail/{plate}", produces = "application/json")
    public ResponseEntity<Vehicle> getVehicleByPlate(@PathVariable("plate") String plate) throws
            NotFoundException {
        logger.warn(plate);
        //String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Vehicle vehicle = (Vehicle) vehicleService.readByPlate(plate);
        if(vehicle==null){
            String error="L'username: " + plate + "non è stato trovato";
            logger.warn(error);
            throw new NotFoundException(error);
            //return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<Vehicle>(vehicle, HttpStatus.OK);

        }

    }

    @RequestMapping(value="/bookable/", method=RequestMethod.GET, produces="application/json" )
    public ResponseEntity<List<Vehicle>> getBookableVehicles(@Valid @RequestBody Reservation res, BindingResult result) throws NotFoundException, BindingException {
        if(result.hasErrors()){
            String msg = error.getMessage(result.getFieldError(), LocaleContextHolder.getLocale());
            logger.warn(msg);
            throw new BindingException(msg);
        }
        List<Vehicle> vehicles = reservationService.bookableVehicles(res);
        return new ResponseEntity<List<Vehicle>>(vehicles, HttpStatus.OK);
    }



    /*
    @RequestMapping(value="/update/{id}", method=RequestMethod.POST)
    public String updateVehicle(@PathVariable("id") int id, @Valid @ModelAttribute("newVehicle") Vehicle vehicle,BindingResult result, Model model){
        if(result.hasErrors()){
            return "insertVehicle";
        }
        vehicleService.update(vehicle);
        List<Vehicle> vehicles=vehicleService.readAll();
        model.addAttribute("vehicles", vehicles);


        return "vehicleReadAll";

    }*/

    @RequestMapping(value="/delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteVehicle(@PathVariable("id") int id) throws NotFoundException {
        logger.info("elimino il veicolo con id" + id);
        Vehicle vehicle = vehicleService.readById(id);
        if(vehicle==null){
            String msg="articolo "+ id + "non presente in anagrafica";
            logger.warn(msg);
            throw new NotFoundException(msg);
        }
        vehicleService.delete(vehicle);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode responseNode = mapper.createObjectNode();
        responseNode.put("code", HttpStatus.OK.toString());
        responseNode.put("message", "Eliminazione Veicolo "+ id + " eseguita con successo");
        return new ResponseEntity<>(responseNode, new HttpHeaders(), HttpStatus.OK);






    }














}
