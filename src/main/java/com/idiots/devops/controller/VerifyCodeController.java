package com.idiots.devops.controller;

import com.idiots.devops.utils.VerifyCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author neo-w7
 * Date 2022-11-23
 * Description
 */
@RestController
public class VerifyCodeController {
    @GetMapping("/verify")
    public void verify(HttpServletRequest req, HttpServletResponse response, HttpSession session){
        // 设置响应的类型格式为图片格式
        response.setContentType("image/jpeg");
        // 禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", " no-cache");
        response.setDateHeader("Expires", 0);
        VerifyCode vCode = new VerifyCode(100, 30, 5, 10);
        session.setAttribute("code", vCode.getCode());
        try {
            vCode.write(response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
