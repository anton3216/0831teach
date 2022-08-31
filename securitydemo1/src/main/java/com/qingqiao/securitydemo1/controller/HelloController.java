package com.qingqiao.securitydemo1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/admin/hello")
    public String admin() {
        return "admin";
    }

    @GetMapping("/user/hello")
    public String user() {
        return "user";
    }

    @GetMapping("index")
    public String test(HttpSession session){
        return "index";
    }

    @PostMapping("doLogin")
    public String doLogin(String username,String password){
        System.out.println(username);
        System.out.println(password);
        return "login success";
    }
}
