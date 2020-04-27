package com.team3.community.controller;

import com.team3.community.dto.RegisterEmailDTO;
import com.team3.community.dto.RegisterNameDTO;
import com.team3.community.dto.ResultDTO;
import com.team3.community.mapper.UserMapper;
import com.team3.community.model.User;
import com.team3.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "password", required = false) String password) {
        //判断数据库中是否存在邮箱和用户名
        if (userService.selectUserByEmail(email)
                && userService.selectUserByName(name)) {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setAvatarUrl("/images/default-avatar.jpg");
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            //将用户数据存入数据库
            userMapper.insert(user);
            //返回登录页面
            return "redirect:/login";
        } else {
            //返回注册页面
            return "redirect:/register";
        }
    }

    @PostMapping("/checkName")
    @ResponseBody
    public Object checkName(@RequestBody RegisterNameDTO registerNameDTO) {
        String name = registerNameDTO.getName();
        if(name != null && userService.selectUserByName(name)) {
            return ResultDTO.okOf();
        } else {
            return ResultDTO.errorOf(2010,"用户名已经存在");
        }
    }

    @PostMapping("/checkEmail")
    @ResponseBody
    public Object checkEmail(@RequestBody RegisterEmailDTO registerEmailDTO) {
        String email = registerEmailDTO.getEmail();
        if(email != null && userService.selectUserByEmail(email)) {
            return ResultDTO.okOf();
        } else {
            return ResultDTO.errorOf(2011,"邮箱已经存在");
        }
    }

//    https://github.com/login/oauth/authorize?client_id=7cf67dc1ef6f4d06a8a1&redirect_uri=http://localhost:8887/callback&scope=user&state=1
}
