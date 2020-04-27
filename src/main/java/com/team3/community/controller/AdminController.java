package com.team3.community.controller;

import com.team3.community.dto.PaginationDTO;
import com.team3.community.dto.QuestionDTO;
import com.team3.community.mapper.UserMapper;
import com.team3.community.model.User;
import com.team3.community.service.NotificationService;
import com.team3.community.service.QuestionService;
import com.team3.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/admin/{action}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "action") String action,
                          Model model,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "5") Integer size,
                          @RequestParam(name = "search", required = false) String search ){


        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "问题管理");
            PaginationDTO paginationDTO = questionService.questionList(page, size);
            model.addAttribute("pagination", paginationDTO);
        } else if("users".equals(action)) {
            model.addAttribute("section", "users");
            model.addAttribute("sectionName", "用户管理");
            PaginationDTO paginationDTO = userService.userList(page, size);
            model.addAttribute("pagination", paginationDTO);
        }


        return "admin";

    }

}
