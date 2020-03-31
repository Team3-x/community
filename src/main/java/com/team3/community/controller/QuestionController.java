package com.team3.community.controller;

import com.team3.community.dto.CommentDTO;
import com.team3.community.dto.QuestionDTO;
import com.team3.community.enums.CommentTypeEnum;
import com.team3.community.mapper.UserMapper;
import com.team3.community.model.User;
import com.team3.community.service.CommentService;
import com.team3.community.service.QuestionService;
import com.team3.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id,
                           HttpServletRequest request,
                           Model model){
        QuestionDTO questionDTO = questionService.getById(id);
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        List<CommentDTO> comments = commentService.listByTargetId(id, CommentTypeEnum.QUESTIN);
        //累加阅读数
        questionService.incView(id);
        model.addAttribute("question", questionDTO);
        model.addAttribute("comments", comments);
        model.addAttribute("relatedQuestions", relatedQuestions);

        //加载关注状态
        User user = (User)request.getSession().getAttribute("user");
        String followId = user.getFollowId();
        if (followId != null) {
            String[] questionId = followId.split(",");
            if (Arrays.asList(questionId).contains(String.valueOf(id))) {
                model.addAttribute("type", 1);
            } else {
                model.addAttribute("type", 0);
            }
        } else {
            model.addAttribute("type", 0);
        }


        return "question";
    }

//    @GetMapping("/question")
//    public String questionPage(){
//        return "question";
//    }
//
//    @PostMapping(value = "/follow")
//    public String question(
//            @RequestParam(value = "email", required = false) String email){
//        System.out.println(email);
////            @RequestParam(value = "qid",required = false) Long id,
////                       HttpServletRequest request,
////                       Model model
//
//        //关注
////        User user = (User)request.getSession().getAttribute("user");
////        user.setFollowId(String.valueOf(id));
////        String followId = user.getFollowId();
////        if (followId != null) {
////            String[] questionId = followId.split(",");
////            if (!Arrays.asList(questionId).contains(String.valueOf(id))) {
////                user.setFollowId(followId + "," + id);
////            }
////        } else {
////            user.setFollowId(String.valueOf(id));
////        }
////        User user = new User();
////        user.setFollowId(String.valueOf(id));
////        userMapper.insert(user);
////        model.addAttribute("followed", true);
//        return "redirect:/";
//    }

}
