package com.palmieri.demo.config.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.http.HttpServletRequest;

public class CustomRememberMeServices extends PersistentTokenBasedRememberMeServices {
    public CustomRememberMeServices(String key, UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
        super(key, userDetailsService, tokenRepository);
    }

    protected boolean rememberMeRequested(HttpServletRequest request, String parameter)
    {
        String isRegularLogin = request.getParameter("isRegularLogin");

        if (isRegularLogin != null && "true".equals(isRegularLogin))
        {
            return super.rememberMeRequested(request, parameter);
        }
        else
        {
            return true;
        }
    }

}
