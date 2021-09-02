package com.baiqi.service.impl;

import com.baiqi.bean.User;
import com.baiqi.service.UserService;
import com.springmvc.annotation.Service;

/**
 * @author 白起老师
 */
@Service(value="userService")
public class UserServiceImpl implements UserService {

    @Override
    public  void  findUser(){
        System.out.println("====调用UserServiceImpl==findUser===");
    }

    @Override
    public User getUser(){
       return new User(1,"老王","admin");
    }
}
