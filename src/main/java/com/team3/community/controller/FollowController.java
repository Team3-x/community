package com.team3.community.controller;

import com.team3.community.dto.CommentCreateDTO;
import com.team3.community.dto.CommentDTO;
import com.team3.community.dto.FollowDTO;
import com.team3.community.dto.ResultDTO;
import com.team3.community.enums.CommentTypeEnum;
import com.team3.community.exception.CustomizeErrorCode;
import com.team3.community.mapper.UserMapper;
import com.team3.community.model.Comment;
import com.team3.community.model.User;
import com.team3.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.util.*;
import java.util.List;

@Controller
public class FollowController {

    @Autowired
    private UserMapper userMapper;

    @ResponseBody
    @RequestMapping(value = "/follow", method = RequestMethod.POST)
    public String follow(@RequestBody FollowDTO followDTO,
                       HttpServletRequest request,
                       Model model){

        User user = (User)request.getSession().getAttribute("user");
//        user.setFollowId(String.valueOf(followDTO.getQId()));
        String followId = user.getFollowId();
        if (followDTO.getType() == 0) {
            if (followId != null) {
                String[] questionId = followId.split(",");
                if (!Arrays.asList(questionId).contains(String.valueOf(followDTO.getQId()))) {
                    user.setFollowId(followId + "," + followDTO.getQId());
                }
            } else {
                user.setFollowId(String.valueOf(followDTO.getQId()));
            }
            model.addAttribute("type", 1);

        } else {
            if (followId != null) {
                String[] questionId = followId.split(",");
                //list.remove报错,用ArrayList.remove
                List<String> list = Arrays.asList(questionId);
                ArrayList<String> arrayList = new ArrayList<>(list);
                String s = String.valueOf(followDTO.getQId());
                arrayList.removeIf(str -> str.equals(s));
                String questionId2 = arrayList.toString();
                int end = questionId2.length() - 1;
                String substring = questionId2.substring(1, end);
                if ("".equals(substring)){
                    substring = null;
                }
                user.setFollowId(substring);
            }
            model.addAttribute("type", 0);
        }
        userMapper.updateByPrimaryKeySelective(user);
        return "redirect:/question";
    }

//    @ResponseBody
//    @RequestMapping(value = "/question/{id}", method = RequestMethod.GET)
//    public String type(@PathVariable(name = "id") Long qid,
//                           HttpServletRequest request,
//                           Model model){
//        User user = (User)request.getSession().getAttribute("user");
//        String followId = user.getFollowId();
//        if (followId != null) {
//            String[] questionId = followId.split(",");
//            if (Arrays.asList(questionId).contains(String.valueOf(qid))) {
//                model.addAttribute("type", 1);
//            } else {
//                model.addAttribute("type", 0);
//            }
//        } else {
//            model.addAttribute("type", 0);
//        }
//        return "question";
//    }
}
