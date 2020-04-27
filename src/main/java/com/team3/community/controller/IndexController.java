package com.team3.community.controller;

import com.team3.community.dto.PaginationDTO;
import com.team3.community.dto.QuestionDTO;
import com.team3.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "5") Integer size,
                        @RequestParam(name = "search", required = false) String search ){
        PaginationDTO pagination = questionService.list(search, page, size);

        QuestionDTO questionDTO = questionService.getById(1L);
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);

        model.addAttribute("relatedQuestions", relatedQuestions);

        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);
        return "index";
    }
}



