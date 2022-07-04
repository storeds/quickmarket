package com.quickmarket.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: quickmarket
 * @author: cx
 * @create: 2022-02-15 17:02
 * @description:
 **/
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @RequestMapping("/getCurrentUser")
    public Object getCurrentUser(Authentication authentication){
        log.info("{}",authentication.getName());
        return authentication.getPrincipal();
    }

}
