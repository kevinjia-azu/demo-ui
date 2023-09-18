package com.pinyougou.shop.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName LoginController
 * @Description TODO
 * @Author kevin_Azu
 * @Date 2018.12.28 21:23
 * @Version 1.0
 **/
@RestController
@RequestMapping("/login")
public class LoginController {


    @RequestMapping("/name")
    public Map loginName(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String, String> map = new HashMap<>();
        map.put("loginName",name);
        return map;
    }

}
