package com.fa.ims.security;

import com.fa.ims.security.filter.UserFilter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;

@AllArgsConstructor
@EnableWebSecurity
@Order(1)
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        prePostEnabled = true,
        jsr250Enabled = true
)
public class SecurityConfiguration {
    @Autowired
    private UserFilter userFilter;
    private final AuthenticationSuccessHandler loginSuccessHandler;
    private final AuthenticationFailureHandler loginFailureHandler;
    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // @formatter:off
        http.csrf().disable()

                .formLogin()
                .loginPage("/loginSub") // Define login url
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)
                .loginProcessingUrl("/authentication")// POST Mapping handle authentication
                .failureUrl("/loginSub/error")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .deleteCookies("JSESSIONID")
                .permitAll()
                .and()
                .rememberMe()
                .key("AbcdefgHiJKlmnOpqrsut0123456789")
                .and()
                .authorizeRequests()
                .antMatchers("/css/**","/static/**", "/resources/**","/.*.css", "/forgot").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/interview-schedule/edit/**","/interview-schedule/create").hasAnyRole("ADMIN","MANAGER","RECRUITER")
                .anyRequest()
                .authenticated() // Public page;
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                .and()
                .addFilterAfter(userFilter, RememberMeAuthenticationFilter.class);


        return http.build();
        // @formatter:on
    }


}
