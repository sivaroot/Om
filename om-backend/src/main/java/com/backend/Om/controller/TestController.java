package com.backend.Om.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @RequestMapping("/backend-test")
    public String test(){
        return "Backend Ok!";
    }
}
