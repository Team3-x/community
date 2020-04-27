package com.team3.community.service;

import com.team3.community.dto.PaginationDTO;
import com.team3.community.dto.QuestionDTO;
import com.team3.community.mapper.UserMapper;
import com.team3.community.model.Question;
import com.team3.community.model.QuestionExample;
import com.team3.community.model.User;
import com.team3.community.model.UserExample;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() == 0) {
            //插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        } else {
            //更新
            User dbUser = users.get(0);
            User updateUser = new User();
            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setAvatarUrl(user.getAvatarUrl());
            updateUser.setName(user.getName());
            updateUser.setToken(user.getToken());
            UserExample example = new UserExample();
            example.createCriteria()
                    .andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser, example);
        }
    }

    public User selectUser(String email, String password) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andEmailEqualTo(email)
                .andPasswordEqualTo(password);
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() == 0) {
            return null;
        } else {
            return users.get(0);
        }
    }
    public User selectAdmin(String account, String password) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(account)
                .andPasswordEqualTo(password);
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() == 0) {
            return null;
        } else {
            return users.get(0);
        }
    }

    public Boolean selectUserByEmail(String email) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andEmailEqualTo(email);
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean selectUserByName(String name) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andNameEqualTo(name);
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public PaginationDTO userList(Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();

        Integer totalPage;
        UserExample userExample = new UserExample();
        userExample.createCriteria();
        Integer totalCount = (int)userMapper.countByExample(userExample);

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage, page);

        //size*(page-1)
        Integer offset = size * (page - 1);
        UserExample example = new UserExample();
        example.createCriteria();
        List<User> users = userMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
//        List<User> userList = new ArrayList<>();

//        for (User user : users) {
//            userList.add(user);
//        }

        paginationDTO.setData(users);
        return paginationDTO;
    }

    public void update(User user) {
//        User updateUser = new User();
//        updateUser.setName(user.getName());
//        updateUser.setEmail(user.getEmail());
//        updateUser.setPassword(user.getPassword());
//        updateUser.setGmtModified(System.currentTimeMillis());
//        UserExample example = new UserExample();
//        example.createCriteria()
//                .andIdEqualTo(user.getId());
//        userMapper.updateByExampleSelective(updateUser, example);
        userMapper.updateByPrimaryKeySelective(user);
    }

    public boolean selectUserByPassword(String password) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andPasswordEqualTo(password);
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

//    public void updateIcon(User user) {
//        userMapper.updateByPrimaryKeySelective(user);
//    }
}
