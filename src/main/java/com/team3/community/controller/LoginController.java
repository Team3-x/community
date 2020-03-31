package com.team3.community.controller;

import com.team3.community.model.User;
import com.team3.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam(value = "email",required = false) String email,
            @RequestParam(value = "password",required = false) String password,
            HttpServletResponse response,
            Model model){
        User user = userService.selectUser(email, password);
        if (user != null) {
            String token = user.getToken();
            response.addCookie(new Cookie("token",token));
            return "redirect:/";
        } else {
            model.addAttribute("error", "邮箱或密码错误");
            return "login";
        }
    }

//    https://github.com/login/oauth/authorize?client_id=7cf67dc1ef6f4d06a8a1&redirect_uri=http://localhost:8887/callback&scope=user&state=1
}
