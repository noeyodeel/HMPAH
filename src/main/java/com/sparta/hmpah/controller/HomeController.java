package com.sparta.hmpah.controller;

import com.sparta.hmpah.service.KakaoService;
import com.sparta.hmpah.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("")
@Controller
public class HomeController {


    @GetMapping("/")
    public String home() {
        return "index";
    }
}