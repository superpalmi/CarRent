package com.palmieri.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static String REALM="REALM";
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    @Override
    public UserDetailsService userDetailsService(){
        User.UserBuilder users = User.builder();
        InMemoryUserDetailsManager manager=new InMemoryUserDetailsManager();
        manager.createUser(users.username("superpalmi").password(new BCryptPasswordEncoder().encode("1234"))
        .roles("USER", "SUPERUSER").build());
        return manager;
    }

    private static final String[] SUPERUSER_USER_MATCHER =
            {
                    "api/user/readall/**",
                    "api/user/delete/**",
                    "api/user/update/**"
            };

    private static final String[]  SUPERUSER_VEHICLE_MATCHER =
            {
                    "api/vehicle/delete/**",
                    "api/vehicle/update/**",
                    "api/vehicle/insert/**"
            };

    private static final String[]  SUPERUSER_RESERVATION_MATCHER =
            {
                    "api/reservation/delete/**",
                    "api/reservation/update/**",
                    "api/reservation/readall/**"
            };

    @Override
    protected void configure(final HttpSecurity http) throws Exception
    {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("api/login/**").permitAll()
                .antMatchers("api/user/insert").permitAll()
                .antMatchers("api/user/extract").permitAll()

                .antMatchers(SUPERUSER_USER_MATCHER).access("hasRole('SUPERUSER')")
                .antMatchers(SUPERUSER_VEHICLE_MATCHER).access("hasRole('SUPERUSER')")
                .antMatchers(SUPERUSER_RESERVATION_MATCHER).access("hasRole('SUPERUSER')")
                .antMatchers("api/user/**").access("hasAnyRole('SUPERUSER', 'USER')")
                .antMatchers("api/vehicle/readall/**").access("hasAnyRole('SUPERUSER', 'USER')")
                .antMatchers("api/vehicle/**").access("hasAnyRole('SUPERUSER', 'USER')")
                .antMatchers("api/reservation/insert/**").access("hasAnyRole('SUPERUSER', 'USER')")
                .antMatchers("api/reservation/**").access("hasAnyRole('SUPERUSER','USER')")
                .antMatchers("api/").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic().realmName(REALM).authenticationEntryPoint(getBasicAuthEntryPoint())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //.logoutSuccessHandler(customLogoutSuccessHandler);
        //.invalidateHttpSession(true)
        //.logoutSuccessHandler(customLogoutSuccessHandler)
        //.clearAuthentication(true);
        //.and().csrf().disable();

    }

    @Bean
    public AuthEntryPoint getBasicAuthEntryPoint(){
        return new AuthEntryPoint();
    }

    @Override
    public void configure(WebSecurity web) throws Exception{
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "*/**");
    }

}
