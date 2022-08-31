package com.qingqiao.securitydemo2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("hello")
    public String hello(){
        return "hello";
    }

    @GetMapping("admin/hello")
    public String admin(){
        return "admin";
    }

    @GetMapping("user/hello")
    public String user(){
        return "user";
    }

    @GetMapping("/administrator")
    public String administrator() {
        return "administrator";
    }
    @GetMapping("/rememberme")
    public String rememberme() {
        return "rememberme";
    }
}
