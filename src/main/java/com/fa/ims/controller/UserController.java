package com.fa.ims.controller;


import com.fa.ims.constant.CommonConstants;
import com.fa.ims.dto.UserDto;
import com.fa.ims.entity.User;
import com.fa.ims.service.DepartService;
import com.fa.ims.service.RoleService;
import com.fa.ims.service.UserService;
import com.fa.ims.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;


@Controller
@RequestMapping("/admin")
public class UserController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;
    @Autowired
    private DepartService departService;

    @GetMapping("/user/create")
    public String userPage(Model model) {
        model.addAttribute("userDto", new UserDto());
        model.addAttribute("rolesList", roleService.getAllRole());
        model.addAttribute("departList", departService.getAllDepart());

        return "user-page/create-user";
    }

    @PostMapping("/user/create")
    public String createUser(@ModelAttribute("userDto") @Valid UserDto userDto,
                             BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        model.addAttribute("rolesList", roleService.getAllRole());
        model.addAttribute("departList", departService.getAllDepart());
        model.addAttribute("userDto", userDto);
        Boolean isExists = userService.isEmailExists(userDto.getUserEmail());
        if(isExists || bindingResult.hasErrors()) {
            model.addAttribute("isEmailExist", isExists);
            model.addAttribute("messFail", Message.MESSAGE_031);
            return ("user-page/create-user");
        }
        if(userService.saveNewUser(userDto)) {
            redirectAttributes.addFlashAttribute("messSuccess", Message.MESSAGE_032);
            return "redirect:/admin/user/create";
        }
        redirectAttributes.addFlashAttribute("messFail", Message.MESSAGE_031);
        return "redirect:/admin/user/create";
    }


    @GetMapping("/user/list")
    public String findPaginated(Model model, @RequestParam(required = false, defaultValue = "1") int pageNo,
                                 @RequestParam(required = false, defaultValue = "") String keyword,
                                 @RequestParam(required = false, defaultValue = "") String role) {

        Page<User> page = userService.findUserByKeyword(pageNo, CommonConstants.PAGE_SIZE,keyword.trim(), role);
        model.addAttribute("usersList", page);

        model.addAttribute("pageNo", pageNo);
        model.addAttribute("pageSize", CommonConstants.PAGE_SIZE);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElements", page.getTotalElements());
        model.addAttribute("roleList",roleService.getAllRole());
        model.addAttribute("keyword",keyword);
        model.addAttribute("role",role);
        return "user-page/list-user";
    }


    @GetMapping("/user/detail/{id}")
    public String detailUser(@PathVariable("id") Long id, Model model) {
        Optional<User> user = userService.findUserById(id);
        model.addAttribute("userDto1", user.orElseThrow());
        model.addAttribute("id", id);
        return "user-page/detail-user";
    }

    @GetMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Long id, Model model) {
        model.addAttribute("rolesList", roleService.getAllRole());
        model.addAttribute("departList", departService.getAllDepart());
        model.addAttribute("userDto2", userService.mapperUserDtoFromUser(userService.findUserById(id).orElseThrow()));
        model.addAttribute("id", id);
        return "user-page/update-user";
    }

    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute("userDto2") @Valid UserDto userDto2,
                             BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        Boolean isExists = userService.isEmailExists(userDto2.getUserEmail().trim(), id);
        if (bindingResult.hasErrors() || isExists) {
            model.addAttribute("isEmailExist", isExists);
            model.addAttribute("rolesList", roleService.getAllRole());
            model.addAttribute("departList", departService.getAllDepart());
            model.addAttribute("userDto2", userDto2);
            model.addAttribute("id", id);
            model.addAttribute("messFail", Message.MESSAGE_033);
            return ("user-page/update-user");
        }
        if(userService.saveUpdatedUser(userDto2, id) != null) {
            redirectAttributes.addFlashAttribute("messSuccess", Message.MESSAGE_034);
            return "redirect:/admin/user/detail/" + id;
        }
        redirectAttributes.addFlashAttribute("messFail", Message.MESSAGE_033);
        return "redirect:/admin/user/detail/" + id;
    }

    @GetMapping("/user/delete/{id}")
    public String updateDeleteUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        if(userService.delete(id)!= null) {
            redirectAttributes.addFlashAttribute("messSuccess", Message.MESSAGE_036);
            return "redirect:/admin/user/list";
        }
        redirectAttributes.addFlashAttribute("messFail", Message.MESSAGE_035);
        return "redirect:/admin/user/list";
    }
}
