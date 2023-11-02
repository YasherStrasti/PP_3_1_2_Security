package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.model.User;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;


    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping(value = "/{id}")
    public String getUserPage(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "/user";
    }

    @GetMapping(value = "")
    public String getAdminPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "index";
    }

    @GetMapping("/{id}/edit")
    public String getEditPage(Model model, @PathVariable("id") Long id, Model roles) {
        roles.addAttribute("listRoles", roleService.findAll());
        model.addAttribute("user", userService.getUserById(id));
        return "edit";
    }

    @PatchMapping("/{id}")
    public String updateUser(@ModelAttribute("user") User user) {
        userService.update(user);
        return "redirect:/admin";
    }

    @GetMapping("/new")
    public String getNewUserPage(@ModelAttribute("user") User user, Model model, @RequestParam(required = false) String username) {
        model.addAttribute("listRoles", roleService.findAll());

        if (username != null) {
            System.out.println("Пользователь существует!");
        } else {
            System.out.println("Пользователя не существует!");
        }

        return "new";
    }

    @PostMapping("/user")
    public String createUser(@ModelAttribute("user") User user) {
        boolean isNotExistUser = userService.save(user);

        if (!isNotExistUser) {
            return "new";
        }

        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUserById(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
