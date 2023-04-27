package com.fa.ims.security.filter;

import com.fa.ims.constant.CommonConstants;
import com.fa.ims.entity.User;
import com.fa.ims.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(2)
public class UserFilter extends OncePerRequestFilter {
    @Autowired
    private AuthenticationService authenticationService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            filterChain.doFilter(request,response);
        }
        else {
            if (request.getSession().getAttribute(CommonConstants.USERNAME_SESSION) == null) {
                User user = authenticationService
                        .findByUsername(authentication.getName())
                        .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
                request.getSession()
                        .setAttribute(CommonConstants.USERNAME_SESSION, user.getUserName());
                request.getSession()
                        .setAttribute(CommonConstants.FULLNAME_OF_USER_IN_SESSION, user.getUserFullname());
                request.getSession()
                        .setAttribute(CommonConstants.DEPARTMENT_OF_USER, user.getUserDepartment().getDepartmentName());
            }
            filterChain.doFilter(request, response);
        }
    }
}
