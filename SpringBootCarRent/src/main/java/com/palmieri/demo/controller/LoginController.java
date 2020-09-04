package com.palmieri.demo.controller;

import com.palmieri.demo.entities.User;
import com.palmieri.demo.service.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/login/form")
public class LoginController {


    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    @Qualifier("persistentTokenRepository")
    private PersistentTokenRepository persistentTokenRepository;



    @GetMapping
    public String getLogin(Model model){

        return "login";
    }
    @PostMapping
    public String getLoginPost(HttpServletRequest request, HttpServletResponse response){


        String[] test = request.getParameterValues("logout");

        if (test != null)
        {
            Cookie cookieWithSlash = new Cookie("JSESSIONID", null);
            //Tomcat adds extra slash at the end of context path (e.g. "/foo/")
            cookieWithSlash.setPath(request.getContextPath() + "/");
            cookieWithSlash.setMaxAge(0);

            Cookie cookieWithoutSlash = new Cookie("JSESSIONID", null);
            //JBoss doesn't add extra slash at the end of context path (e.g. "/foo")
            cookieWithoutSlash.setPath(request.getContextPath());
            cookieWithoutSlash.setMaxAge(0);

            //Remove cookies on logout so that invalidSessionURL (session timeout) is not displayed on proper logout event
            response.addCookie(cookieWithSlash); //For cookie added by Tomcat
            response.addCookie(cookieWithoutSlash); //For cookie added by JBoss


            if (test.length == 2)
            {
                logger.info("utente: " + test[1]);
                persistentTokenRepository.removeUserTokens(test[1]);
            }


        }

        return "redirect:/login/form?logout";

    }



}
