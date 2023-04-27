package com.fa.ims.controller;

import com.fa.ims.dto.EmailDto;
import com.fa.ims.dto.UserDto;
import com.fa.ims.dto.UserLoginDto;
import com.fa.ims.service.UserService;
import com.fa.ims.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class LoginController extends BaseController {

    @Autowired
    private UserService userService;

    @GetMapping("/loginSub")
    public String showLogin(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            model.addAttribute("userLoginDto", new UserLoginDto());
            return "auth/loginSub";
        }
        return "redirect:/";
    }

    @GetMapping("/loginSub/error")
    public String showLoginError(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            model.addAttribute("userLoginDto", new UserLoginDto());
            model.addAttribute("invalid", true);
            return "auth/loginSub";
        }
        return "redirect:/";
    }

    @GetMapping("/forgot")
    public String showForgot(Model model) {
        model.addAttribute("emailDto", new EmailDto());
        return "auth/forgot";
    }

    @PostMapping("/forgot")
    public String resetPassword(@ModelAttribute("emailDto") @Valid EmailDto emailDto,
                                BindingResult bindingResult, Model model) {
        if(!userService.isEmailExists(emailDto.getEmail())) {
            model.addAttribute("isEmailNotExist", true);
            return ("/auth/forgot");
        }
        if(bindingResult.hasErrors()) {
            return ("/auth/forgot");
        }
        userService.resetPassWord(emailDto.getEmail());

        System.out.println(emailDto.getEmail());
        return "redirect:/loginSub";
    }

}
