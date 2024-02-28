package com.sparta.hmpah.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Home", description = "Home API")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("")
@Controller
public class HomeController {

    @Operation(summary = "메인", description = "메인페이지")
    @GetMapping("/")
    public String home() {
        return "main";
    }
}