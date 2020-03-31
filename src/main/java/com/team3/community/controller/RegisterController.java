package com.team3.community.controller;

import com.team3.community.mapper.UserMapper;
import com.team3.community.model.User;
import com.team3.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @GetMapping
    public String registerPage(){
        return "register";
    }

    @PostMapping
    public String register(
            @RequestParam(value = "name",required = false) String name,
            @RequestParam(value = "email",required = false) String email,
            @RequestParam(value = "password",required = false) String password,
            Model model) {

        if (userService.selectUserByEmail(email) == null) {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setAvatarUrl("/images/default-avatar.jpg");
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            return "redirect:/login";
        } else {
            model.addAttribute("error", "用户已存在");
            return "redirect:/register";


        }

    }

//    https://github.com/login/oauth/authorize?client_id=7cf67dc1ef6f4d06a8a1&redirect_uri=http://localhost:8887/callback&scope=user&state=1
}
