package org.csu.gameshopms.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error"; // 自定义错误页面
    }
}
