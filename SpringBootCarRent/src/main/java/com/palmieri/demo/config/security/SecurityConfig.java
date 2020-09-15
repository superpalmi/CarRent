package com.palmieri.demo.config.security;

import com.palmieri.demo.config.security.jwt.AuthTokenFilter;
import com.palmieri.demo.config.security.jwt.JWTAuthEntryPoint;
import com.palmieri.demo.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        // securedEnabled = true,
        // jsr250Enabled = true,
        prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    CustomUserDetailsService userDetailsService;

    @Autowired
    JWTAuthEntryPoint entrypointhandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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


    /*
    @Override
    public void configure(WebSecurity web) throws Exception {
        // JwtAuthenticationTokenFilter will ignore the below paths
        web.ignoring().antMatchers(
                HttpMethod.POST,
                "/api/auth/**"
        );



     .antMatchers(SUPERUSER_USER_MATCHER).access("hasRole('SUPERUSER')")
                .antMatchers(SUPERUSER_VEHICLE_MATCHER).access("hasRole('SUPERUSER')")
                .antMatchers(SUPERUSER_RESERVATION_MATCHER).access("hasRole('SUPERUSER')")
                .antMatchers("api/user/insert").permitAll()
                .antMatchers("api/user/**").access("hasAnyRole('SUPERUSER', 'USER')")
                .antMatchers("api/vehicle/readall/**").access("hasAnyRole('SUPERUSER', 'USER')")
                .antMatchers("api/vehicle/filter/**").access("hasAnyRole('SUPERUSER', 'USER')")
                .antMatchers("api/vehicle/**").access("hasAnyRole('SUPERUSER', 'USER')")
                .antMatchers("api/reservation/insert/**").access("hasAnyRole('SUPERUSER', 'USER')")
                .antMatchers("api/reservation/**").access("hasAnyRole('SUPERUSER','USER')")
    */

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("api/auth/**").permitAll()
                .antMatchers("api/user/insert/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(entrypointhandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity.ignoring().antMatchers("/api/**");
    }
}







