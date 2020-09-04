package com.palmieri.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.palmieri.demo.entities.User;
import com.palmieri.demo.exception.BindingException;
import com.palmieri.demo.exception.DuplicateException;
import com.palmieri.demo.exception.NotFoundException;
import com.palmieri.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("api/user")
public class UserController

{

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    //da utilizzare tramite le lambda
    List<User> MainRecordSet;
    @Autowired
    private ResourceBundleMessageSource error;

    /*@Autowired
    private BCryptPasswordEncoder passwordEncoder;*/

    private void GetAllUsers(){
        MainRecordSet=userService.readAll();

    }
    @GetMapping
    public String getUsers(Model model){
        logger.info("ottenendo tutti gli user");
        List<Object> recordset;
        long NumRecords=0;
        GetAllUsers();
        if(MainRecordSet!=null){
           recordset=MainRecordSet.stream().collect(Collectors.toList());

        }
        return null;

    }
    //user/insert


    @PostMapping(value="/insert")
    public ResponseEntity<User> insertUser(@Valid @RequestBody User user, BindingResult result) throws BindingException, DuplicateException {
        logger.info("Salvo user "+ user);
        if(result.hasErrors()){
            String msg = error.getMessage(result.getFieldError(), LocaleContextHolder.getLocale());
            logger.warn(msg);
            throw new BindingException(msg);
        }
        userService.create(user);
        return new ResponseEntity<User>(new HttpHeaders(), HttpStatus.CREATED);

    }





    @RequestMapping(value="/detail/{userName}", produces = "application/json")
    public ResponseEntity<User> getUserByUsername(@PathVariable("userName") String userName) throws
            NotFoundException {
        logger.warn(userName);
        //String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = (User) userService.readByUsername(userName);
        if(user==null){
            String error="L'username: " + userName + "non è stato trovato";
            logger.warn(error);
            throw new NotFoundException(error);
            //return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<User>(user, HttpStatus.OK);

        }








    }
    @RequestMapping(value="/delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteUser(@PathVariable("id") int id) throws NotFoundException {
        logger.info("elimino l'articolo con codice" + id);
        User user = userService.readById(id);
        if(user==null){
            String msg="articolo "+ id + "non presente in anagrafica";
            logger.warn(msg);
            throw new NotFoundException(msg);
        }
        userService.delete(user);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode responseNode = mapper.createObjectNode();
        responseNode.put("code", HttpStatus.OK.toString());
        responseNode.put("message", "Eliminazione Utente "+ id + " eseguita con successo");
        return new ResponseEntity<>(responseNode, new HttpHeaders(), HttpStatus.OK);






    }
    /*
    @RequestMapping(value="/update/{id}", method=RequestMethod.POST)
    public String updateUser(@ModelAttribute("newUser") User user,BindingResult result, Model model, HttpServletRequest request){
        if(result.hasErrors()){
            return "insertUser";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userService.update(user);
        model.addAttribute("user", user);
        return "userDetail";

    }*/


    @RequestMapping(value="/readall/", method=RequestMethod.GET)
    public String readAll(Model model){
        List<User> users=userService.readAll();
        model.addAttribute("users", users);
        return "userReadAll";




    }









}
