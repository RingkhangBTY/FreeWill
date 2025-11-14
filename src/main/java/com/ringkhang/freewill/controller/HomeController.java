package com.ringkhang.freewill.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.http.HttpRequest;

@RestController
public class HomeController {

    @GetMapping("/")
    public void Home(HttpServletResponse response) throws IOException {
        response.sendRedirect("/swagger-ui.html");
    }


}
