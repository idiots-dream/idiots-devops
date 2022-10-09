package com.idiots.devops.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author blue-light
 * Date 2022-10-09
 * Description
 */
@Controller
public class TerminalController {
    @GetMapping(value = "/")
    public String path() {
        return "index";
    }
}
