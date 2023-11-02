package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.model.User;

import javax.validation.Valid;

@Controller
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;


    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping("/user/{id}")
    public String getUserPage(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "/user";
    }

    @GetMapping(value = "/admin")
    public String getAdminPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "index";
    }

    @GetMapping("/user/{id}/edit")
    public String getEditPage(Model model, @PathVariable("id") Long id, Model roles) {
        roles.addAttribute("listRoles", roleService.findAll());
        model.addAttribute("user", userService.getUserById(id));
        return "edit";
    }

    @PatchMapping("/user/{id}")
    public String updateUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit";
        } else {
            userService.update(user);
            return "redirect:/admin";
        }
    }

    @GetMapping("/new")
    public String getNewUserPage(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("listRoles", roleService.findAll());
        return "new";
    }

    @PostMapping("/user")
    public String createUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "new";
        } else {
            userService.save(user);
            return "redirect:/admin";
        }
    }

    @DeleteMapping("/{id}")
    public String deleteUserById(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
