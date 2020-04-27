package com.team3.community.controller;

import com.team3.community.dto.*;
import com.team3.community.mapper.UserMapper;
import com.team3.community.model.User;
import com.team3.community.service.CommentService;
import com.team3.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/user/{id}")
    public String user(@PathVariable(name = "id") Long id,
                           Model model){
        User user = userMapper.selectByPrimaryKey(id);
        model.addAttribute("name", user.getName());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("avatar", user.getAvatarUrl());
        model.addAttribute("password", user.getPassword());
        model.addAttribute("id", user.getId());
        return "user";
    }

    @PostMapping(value = "/user")
    public String saveImage(@RequestParam(value = "file",required = false) MultipartFile file,
                            HttpServletRequest request,
                            @RequestParam(value = "id",required = false) Long id,
                            @RequestParam(value = "name",required = false) String name,
                            @RequestParam(value = "email",required = false) String email,
                            @RequestParam(value = "password",required = false) String password,
                            @RequestParam(value = "password1",required = false) String password1){
//        User user1 = (User)request.getSession().getAttribute("user");
//        Long id = user1.getId();
        String staticPath = ClassUtils.getDefaultClassLoader().getResource("static").getPath();
        //生成文件路径
        // 文件名
        String fileName = file.getOriginalFilename();
        String iconUrl;
        if (!file.isEmpty()) {
            // 图片存储目录及图片名称
            String url_path = "images" + File.separator + fileName;
            //图片保存路径
            String savePath = staticPath + File.separator + url_path;

            //后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            if (!".jpg".equals(suffixName) && !".jpeg".equals(suffixName) && !".png".equals(suffixName) && !".bmp".equals(suffixName)
                    && !".gif".equals(suffixName)) {
                return "上传失败:无效图片文件类型";
            }
            long fileSize = file.getSize();
            if(fileSize>1024*500){
                return "图片过大";
            }
            File saveFile = new File(savePath);
            if (!saveFile.exists()){
                saveFile.mkdirs();
            }
            try {
                file.transferTo(saveFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            iconUrl="/images/"+fileName;
            User user = new User();
            user.setId(id);
            user.setAvatarUrl(iconUrl);
            user.setName(name);
            user.setEmail(email);
            if (password1 != null) {
                user.setPassword(password1);
            } else {
                user.setPassword(password);
            }
            user.setGmtModified(System.currentTimeMillis());
            userService.update(user);
        } else {
            User user = new User();
            user.setId(id);
            user.setName(name);
            user.setEmail(email);
            if (password1 != null) {
                user.setPassword(password1);
            } else {
                user.setPassword(password);
            }
            user.setGmtModified(System.currentTimeMillis());
            userService.update(user);
        }
        String str = "user/"+id;
        return "redirect:/"+str;
    }

    @PostMapping("/user/check")
    @ResponseBody
    public Object register(@RequestBody PasswordDTO passwordDTO,
                           HttpServletRequest request) {
        User user = (User)request.getSession().getAttribute("user");
        String password1 = user.getPassword();
        String password = passwordDTO.getPassword();
        if(password.equals(password1)) {
            return ResultDTO.okOf(2011,"密码正确");
        } else {
            return ResultDTO.errorOf(2010,"错误");
        }
    }



    @DeleteMapping("/user/delete")
    public String delete(@RequestBody IdDTO userIdDTO){
        userMapper.deleteByPrimaryKey(userIdDTO.getId());
        return "redirect:/admin/users";
    }

}
