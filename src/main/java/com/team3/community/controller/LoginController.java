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
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "password", required = false) String password,
            HttpServletResponse response,
            Model model) {
        //查询数据库中是否存在该用户信息
        User user = userService.selectUser(email, password);
        if (user != null) {
            String token = user.getToken();
            //添加cookie实现持久化登录
            response.addCookie(new Cookie("token", token));
            //返回首页
            return "redirect:/";
        } else {
            //返回错误信息
            model.addAttribute("error", "邮箱或密码错误");
            //返回登录页面
            return "login";
        }
    }

}
