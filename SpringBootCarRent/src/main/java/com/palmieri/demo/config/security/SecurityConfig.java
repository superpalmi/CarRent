package com.palmieri.demo.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter
{
    @Autowired
    @Qualifier("customUserDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    @Qualifier("persistentTokenRepository")
    private PersistentTokenRepository persistentTokenRepository;

    @Autowired
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;


    @Autowired
    DataSource dataSource;

    @Bean
    public BCryptPasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall()
    {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        firewall.setAllowSemicolon(true);

        return firewall;
    }

    @Override
    public void configure(WebSecurity web) throws Exception
    {
        super.configure(web);

        web.httpFirewall(allowUrlEncodedSlashHttpFirewall());

    }

    @Override
    public void configure(final AuthenticationManagerBuilder auth) throws Exception
    {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider()
    {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    private static final String[] SUPERUSER_USER_MATCHER =
            {
                    "/user/readall/**",
                    "/user/delete/**",
                    "/user/update/**"
            };

    private static final String[]  SUPERUSER_VEHICLE_MATCHER =
            {
                    "/vehicle/delete/**",
                    "/vehicle/update/**",
                    "/vehicle/insert/**"
            };

    private static final String[]  SUPERUSER_RESERVATION_MATCHER =
            {
                    "/reservation/delete/**",
                    "/reservation/update/**",
                    "/reservation/readall/**"
            };

    @Override
    protected void configure(final HttpSecurity http) throws Exception
    {
        http
                .authorizeRequests()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/user/insert").permitAll()

                .antMatchers(SUPERUSER_USER_MATCHER).access("hasRole('SUPERUSER')")
                .antMatchers(SUPERUSER_VEHICLE_MATCHER).access("hasRole('SUPERUSER')")
                .antMatchers(SUPERUSER_RESERVATION_MATCHER).access("hasRole('SUPERUSER')")
                .antMatchers("/user/**").access("hasAnyRole('SUPERUSER', 'USER')")
                .antMatchers("/vehicle/readall/**").access("hasAnyRole('SUPERUSER', 'USER')")
                .antMatchers("/vehicle/filter/**").access("hasAnyRole('SUPERUSER', 'USER')")
                .antMatchers("/vehicle/**").access("hasAnyRole('SUPERUSER', 'USER')")
                .antMatchers("/reservation/insert/**").access("hasAnyRole('SUPERUSER', 'USER')")
                .antMatchers("/reservation/**").access("hasAnyRole('SUPERUSER','USER')")
                .antMatchers("/").permitAll()
                .and()
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .loginPage("/login/form")
                .loginProcessingUrl("/login")
                .failureUrl("/login/form?error")
                .usernameParameter("username")
                .passwordParameter("password")
                .and()
                .exceptionHandling()
                .accessDeniedPage("/login/form?forbidden")
                .and()
                .logout()
                 .logoutUrl("/login/form?logout");
        //.logoutSuccessHandler(customLogoutSuccessHandler);
        //.invalidateHttpSession(true)
        //.logoutSuccessHandler(customLogoutSuccessHandler)
        //.clearAuthentication(true);
        //.and().csrf().disable();

    }

    public AuthenticationFilter authenticationFilter()
            throws Exception
    {

        AuthenticationFilter filter = new AuthenticationFilter();

        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setAuthenticationFailureHandler(failureHandler());
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        filter.setRememberMeServices(customRememberMeServices());


        return filter;

    }

    public SimpleUrlAuthenticationFailureHandler failureHandler()
    {
        return new SimpleUrlAuthenticationFailureHandler("/login/form?error");
    }

    @Bean
    public SavedRequestAwareAuthenticationSuccessHandler authenticationSuccessHandler()
    {
        SavedRequestAwareAuthenticationSuccessHandler auth = new SavedRequestAwareAuthenticationSuccessHandler();
        auth.setTargetUrlParameter("targetUrl");

        return auth;
    }

    @Bean
    public PersistentTokenBasedRememberMeServices customRememberMeServices()
    {
        String Key = "ricordamiKey";

        PersistentTokenBasedRememberMeServices rememberMeServices =
                new CustomRememberMeServices(Key, userDetailsService, persistentTokenRepository);

        rememberMeServices.setCookieName("ricordami");
        rememberMeServices.setTokenValiditySeconds(60 * 60 * 4);
        rememberMeServices.setParameter("ricordami");
        rememberMeServices.setUseSecureCookie(false); //todo Abilitare con l'SSL

        return rememberMeServices;


    }

	/*
	@Bean
    public PersistentTokenRepository persistentTokenRepository()
	{
        JdbcTokenRepositoryImpl tokenRepositoryImpl = new JdbcTokenRepositoryImpl();
        tokenRepositoryImpl.setDataSource(dataSource);

        return tokenRepositoryImpl;
    }
    */

}



/*
@Bean
@Override
public UserDetailsService userDetailsService()
{
	UserBuilder users = User.builder();

	InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

	//Utente 1
	manager.createUser(
			users
			.username("Nicola")
			.password(new BCryptPasswordEncoder().encode("123Stella"))
			.roles("USER")
			.build());

	manager.createUser(
			users
			.username("Admin")
			.password(new BCryptPasswordEncoder().encode("VerySecretPwd"))
			.roles("USER", "ADMIN")
			.build());

	return manager;

}
*/
