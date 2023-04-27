/*-============================================================================
 * PizSoft. PROPRIETARY
 * Copyright© 2021 PizSoft.
 * UNPUBLISHED WORK
 * ALL RIGHTS RESERVED
 *
 * This software is the confidential and proprietary information of
 * PizSoft. ("Proprietary Information"). Any use, reproduction,
 * distribution or disclosure of the software or Proprietary Information,
 * in whole or in part, must comply with the terms of the license
 * agreement, nondisclosure agreement or contract entered into with
 * PizSoft. providing access to this software.
 *
 *=============================================================================
 */
package com.fa.ims.security;

import com.fa.ims.constant.CommonConstants;
import com.fa.ims.entity.User;
import com.fa.ims.repository.UserRepository;
import com.fa.ims.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        User user = authenticationService
                .findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
        request.getSession()
                .setAttribute(CommonConstants.USERNAME_SESSION, user.getUserName());
        request.getSession()
                .setAttribute(CommonConstants.FULLNAME_OF_USER_IN_SESSION, user.getUserFullname());
        request.getSession()
                .setAttribute(CommonConstants.DEPARTMENT_OF_USER, user.getUserDepartment().getDepartmentName());

        super.onAuthenticationSuccess(request, response, authentication);
    }

    /**
     * Determine target url based on role
    */
    @Override
    protected String determineTargetUrl(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) {
        return "/";
    }
}
