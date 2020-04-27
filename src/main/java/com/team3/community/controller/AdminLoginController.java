package com.team3.community.controller;

import com.team3.community.model.User;
import com.team3.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AdminLoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/admin-login")
    public String loginPage(){
        return "admin-login";
    }

    @PostMapping("/admin-login")
    public String login(
            @RequestParam(value = "account",required = false) String account,
            @RequestParam(value = "password",required = false) String password,
            HttpServletResponse response,
            Model model){
        User user = userService.selectAdmin(account, password);
        if (user != null) {
            String token = user.getToken();
            response.addCookie(new Cookie("token",token));
            return "redirect:/admin/questions";
        } else {
            model.addAttribute("error", "邮箱或密码错误");
            return "admin-login";
        }
    }

}
