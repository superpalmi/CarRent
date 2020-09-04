package com.palmieri.demo.config.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class CustomLogoutSuccessHandler  implements LogoutSuccessHandler
{
    private static final Logger logger = LoggerFactory.getLogger(CustomLogoutSuccessHandler.class);

    @Autowired
    @Qualifier("persistentTokenRepository")
    private PersistentTokenRepository persistentTokenRepository;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException
    {

        if (authentication != null)
        {
            logger.info("utente: " + authentication.getName());
            String userName = authentication.getName();
            persistentTokenRepository.removeUserTokens(userName);


            String refererUrl = request.getHeader("Referer");
            logger.info("Logout from: " + refererUrl);


			 /*
			 String URL = request.getContextPath() + "/";

			 response.setStatus(HttpStatus.OK.value());
			 response.sendRedirect(URL);
			 */


        }



    }


}
