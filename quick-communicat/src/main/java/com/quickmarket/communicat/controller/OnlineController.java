package com.quickmarket.communicat.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @program: quickmarket
 * @author: cx
 * @create: 2022-03-12 11:17
 * @description: 在线咨询页面
 **/
@Controller
public class OnlineController {

    @Value("${netty.ws}")
    private String ws;

    /**
     * 客服界面
     */
    @GetMapping(value = {"/index", "/customer","/"})
    public String index(Model model) {
        model.addAttribute("ws", ws);
        return "customer";
    }


    /**
     * 游客页面
     */
    @GetMapping("/tourist")
    public String tourist(Model model) {
        model.addAttribute("ws", ws);
        return "tourist";
    }

}
