package org.example.exceptionhandler.controller;

import org.example.exceptionhandler.exception.MemberRegistException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.GeneralSecurityException;

@Controller
public class OtherController {

    @GetMapping("other-controller-null")
    public String otherControllerException() {
        String str = null;
        System.out.println(str.charAt(0));

        return "/";
    }

    @GetMapping("other-controller-user")
    public String userException() throws Exception {
        boolean check = true;
        if (check) {
            throw new MemberRegistException("[OtherControllerUser] 당신은 우리서비스의 회원이 될 수 없습니다.");
        }
        return "/";
    }
}
