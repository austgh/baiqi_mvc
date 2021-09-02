package com.baiqi.controller;

import com.baiqi.bean.User;
import com.baiqi.service.UserService;
import com.springmvc.annotation.AutoWired;
import com.springmvc.annotation.Controller;
import com.springmvc.annotation.RequestMapping;
import com.springmvc.annotation.ResponseBody;

/**
 * @author 白起老师
 */
@Controller
public class UserController {

       @AutoWired(value="userService")
       private UserService userService;


       //定义方法
       //TODO 参数如何进行映射获取还需要处理
       @RequestMapping("/findUser")
       public  String  findUser(String name){
           System.out.println("1");
           //调用服务层
           userService.findUser();
           return "forward:/success.jsp";
       }

    @RequestMapping("/getData")
    @ResponseBody  //返回json格式的数据
    public User getData(){
        //调用服务层
        return userService.getUser();
    }
}
