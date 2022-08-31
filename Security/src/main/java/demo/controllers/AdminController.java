package demo.controllers;


import demo.model.Role;
import demo.model.User;
import demo.repository.RoleRepository;
import demo.service.RoleService;
import demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private UserService userService;
    private RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    AdminController(UserService userService,RoleService roleService,PasswordEncoder passwordEncoder){
        this.userService = userService;
        this.roleService=roleService;
        this.passwordEncoder=passwordEncoder;
    }

    @GetMapping("/{id}")//МЕТОД ЮЗЕРА, А ОСТАЛЬНЫЕ ПЕРЕНЕСЕМ В AdminController
    public String getUser(Model model,@PathVariable("id") long id){ //Principal principal){//@PathVariable("id") long id){
        model.addAttribute("user", userService.findById(id));
        // model.addAttribute("user",userService.findById(id));
        return "show";
    }

    @GetMapping()
    public String getUsers(Model model){
        model.addAttribute("users",userService.findAll());
        return "all";
    }

    @GetMapping("/new")
    public String createUsersForm(@ModelAttribute("user") User user,Model model) {
        model.addAttribute("allRoles", roleService.findAll());
        return "new";
    }

    @PostMapping()
    public String createUser(@ModelAttribute("user") User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String userDelete(@PathVariable("id") long id){
        userService.findById(id).setRoles(Collections.emptyList());
        userService.removeUserById(id);
        return "redirect:/admin";
    }

    @GetMapping("/update/{id}")
    public String updateUserForm(@PathVariable("id") long id,Model model){
        model.addAttribute("user",userService.findById(id));
        model.addAttribute("roles",roleService.findAll());
        return "add";//"update";
    }

    @PostMapping("/{id}")
    public String updateUser(/*@Validated @RequestBody User user){//*/@ModelAttribute("user") User user){
        System.out.println(user.getUsername());
        System.out.println(user.getLastName());
        System.out.println(user.getPassword());
        System.out.println(user.getAge());
        System.out.println(user.getRoles());
        user.setPassword(userService.findById(user.getId()).getPassword());
        //user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println(user.getPassword());
        userService.update(user);
        System.out.println();
        return "redirect:/admin";
    }

    @GetMapping("/add/{id}")
    public String addForm(@PathVariable("id") long id, Model model){
        model.addAttribute("user",userService.findById(id));
        return "add";
    }

  //  @PostMapping("/add/{id}/{name}")
  //  public String addRole(@PathVariable("id") long id,@PathVariable("name") String name){
  //      userService.findById(id).getRoles().add(repository.findByName(name));
  //      return "redirect:/admin";
  //  }
}
