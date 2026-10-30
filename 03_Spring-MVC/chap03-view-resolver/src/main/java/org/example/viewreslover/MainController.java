package org.example.viewreslover;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    /*
    * @Controller 가 붙은 메소드ㅔㅇ서 반환하는 문자열은
    * 응답이 아니라 ViewResolver가 해석할 '논리 뷰'이름이다.
    * */
    @RequestMapping(value = {"/", "/main"})
    public String main() {
        return "main";
    }
}
