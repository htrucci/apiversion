package com.htrucci.apiversion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {

    @RequestMapping("test55")
    public String test55(){
        return "test";
    }
}
