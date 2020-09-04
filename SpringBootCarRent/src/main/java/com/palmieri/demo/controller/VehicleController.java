package com.palmieri.demo.controller;

import com.palmieri.demo.entities.Reservation;
import com.palmieri.demo.entities.Vehicle;
import com.palmieri.demo.service.VehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
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
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("api/vehicle")
public class VehicleController {



    private static final Logger logger = LoggerFactory.getLogger(VehicleController.class);
    @Autowired
    private VehicleService vehicleService;





    List<Vehicle> MainRecordSet;

    @InitBinder
    public void bindingPreparation(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        CustomDateEditor orderDateEditor = new CustomDateEditor(dateFormat, true);
        binder.registerCustomEditor(Date.class, orderDateEditor);
    }
    //aggiungere filtri in base alle date inserite

    private void GetAllVehicles(){

        MainRecordSet=vehicleService.readAll();

    }


    @RequestMapping(value="/insert", method = RequestMethod.GET)
    public String getInsertVehicle(Model model){
        Vehicle vehicle=new Vehicle();
        model.addAttribute("Titolo", "Inserisci il Veicolo");
        model.addAttribute("newVehicle", vehicle);
        return "insertVehicle";
    }

    @RequestMapping(value={"/insert","/update/{id}"}, method=RequestMethod.POST)
    public String insertVehicle( @Valid @ModelAttribute("newVehicle") Vehicle vehicle, BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("vehicle", vehicle);
            if(vehicle.getId()!=0){
                model.addAttribute("action", "update");
            }
            return "insertVehicle";
        }
        if(vehicle.getId()==0){
            if(vehicleService.exists(vehicle)){
                model.addAttribute("message", "la targa è già stata usata");
                model.addAttribute("vehicle", vehicle);
                return "insertVehicle";

            }
        }

        vehicleService.saveOrUpdate(vehicle);
        List<Vehicle> vehicles=vehicleService.readAll();
        model.addAttribute("vehicles", vehicles);
        return "vehicleReadAll";

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

    @RequestMapping(value="/delete/{id}", method = RequestMethod.GET)
    public String deleteVehicle(@PathVariable("id") int id, Model model, HttpServletRequest request){

        Vehicle vehicle = vehicleService.readById(id);
        vehicleService.delete(vehicle);
        List<Vehicle> vehicles = vehicleService.readAll();
        model.addAttribute("vehicles", vehicles);
        return "vehicleReadAll";


    }

    @RequestMapping(value="/update/{id}", method=RequestMethod.GET)
    public String getUpdateVehicle(@PathVariable("id") int id, Model model, HttpServletRequest request){
        Vehicle vehicle = vehicleService.readById(id);

        model.addAttribute("Titolo", "Aggiorna Veicolo");
        //databinding
        model.addAttribute("action", "update");
        model.addAttribute("newVehicle", vehicle);
        return "insertVehicle";



    }




    @RequestMapping(value="/detail/{id}", method=RequestMethod.GET)
    public String getVehicleById(@PathVariable("id") int id, Model model, HttpServletRequest request){
        Vehicle vehicle=null;
        vehicle=vehicleService.readById(id);
        model.addAttribute("Titolo", "Veicolo Inserito");
        model.addAttribute("vehicle", vehicle);

        return "vehicleDetail";




    }
    @RequestMapping(value="/readall/", method = RequestMethod.GET)
    public String readAll(Model model){
        List<Vehicle> vehicles=vehicleService.readAll();
        model.addAttribute("vehicles", vehicles);
        return "vehicleReadAll";

    }
    @RequestMapping(value="/filter/", method = RequestMethod.GET)
    public String getVehicleFilter(Model model){
        model.addAttribute("Titolo", "Inserisci le date");
        Reservation res= new Reservation();
        model.addAttribute("newReservation", res);
        return "vehicleFilter";



    }
    @RequestMapping(value="/filter/", method = RequestMethod.POST)
    public String vehicleFilter(@ModelAttribute("newReservation") Reservation res, BindingResult result, Model model){

        Set<Vehicle> vehicles=vehicleService.readByDate(res.getDataInizio(), res.getDataFine());
        model.addAttribute("Titolo", "Inserisci le date");
        model.addAttribute("newReservation", res);
        if(vehicles!=null){
            model.addAttribute("vehicles", vehicles);
        }else{
            model.addAttribute("message", "non ci sono veicoli disponibili nelle date selezionate");
        }

        return "vehicleFilter";

    }













}
