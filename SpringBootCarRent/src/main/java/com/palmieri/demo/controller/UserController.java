package com.palmieri.demo.controller;

import com.palmieri.demo.entities.User;
import com.palmieri.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private BCryptPasswordEncoder passwordEncoder;

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


    @RequestMapping(value={"/insert", "/update/{id}"}, method = RequestMethod.POST) //sia creazione che update
    public String insertUser(@ModelAttribute(value = "action") String action,@Valid @ModelAttribute(value = "newUser") User user, BindingResult result, Model model ){
        if(result.hasErrors()){
            model.addAttribute("user", user);
            if(user.getId()!=0){
                model.addAttribute("action", "update");
            }
            return "insertUser";
        }
        if(user.getId()==0){

            if(userService.exists(user)){
                model.addAttribute("message", "username o email non disponibili");
                model.addAttribute("user", user);
                return "insertUser";
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userService.saveOrUpdate(user);






        model.addAttribute("user", user);
        return "userDetail";

    }





    @RequestMapping(value="/detail/{userName}", produces = "application/json")
    public ResponseEntity<User> getUserByUsername(@PathVariable("userName") String userName){
        logger.warn(userName);
        //String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = (User) userService.readByUsername(userName);
        if(user==null){
            String error="L'username: " + userName + "non è stato trovato";
            logger.warn(error);
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<User>(user, HttpStatus.OK);

        }








    }
    @RequestMapping(value="/delete/{id}", method = RequestMethod.GET)
    public String deleteUser(@PathVariable("id") int id, Model model, HttpServletRequest request){

        User user = userService.readById(id);
        userService.delete(user);
        return "redirect:/user/readall/";






    }
    @RequestMapping(value="/update/{id}", method=RequestMethod.GET)
    public String getUpdateUser(@PathVariable("id") int id, Model model, HttpServletRequest request){
        User user = userService.readById(id);

        model.addAttribute("Titolo", "Aggiorna Utente");
        //databinding
        model.addAttribute("action", "update");
        model.addAttribute("newUser", user);
        return "insertUser";



    }
    @ExceptionHandler(com.palmieri.demo.exception.NoItemFoundException.class)
    public ModelAndView handleError(HttpServletRequest request, com.palmieri.demo.exception.NoItemFoundException exception)
    {
        ModelAndView mav = new ModelAndView();

        mav.addObject("id", exception.getId());
        mav.addObject("exception", exception);
        mav.addObject("url", request.getRequestURL() + "?" + request.getQueryString());

        mav.setViewName("noitemfound");

        return mav;
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
