package com.palmieri.demo.service;

import com.palmieri.demo.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService
{
    @Autowired
    private UserService userService;




    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.readByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("Utente non trovato");
        }
        org.springframework.security.core.userdetails.User.UserBuilder builder = null;
        builder=org.springframework.security.core.userdetails.User.withUsername(user.getUserName());
        String role = ("ROLE_"+user.getRole().toUpperCase());
        builder.authorities(role);

        builder.password(user.getPassword());
        return builder.build();

    }

}
