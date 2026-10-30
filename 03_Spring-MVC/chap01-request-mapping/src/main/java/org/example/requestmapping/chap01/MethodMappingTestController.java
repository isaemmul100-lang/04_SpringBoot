package org.example.requestmapping.chap01;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// Dispatcher Servlet이 처리를 위임하는 컨트롤러 클래스
@Controller
public class MethodMappingTestController {

    // 핸들러 메소드 지정
    // @RequestMapping 에 URL만 작성할 경우
    // 모든 HTTP 메소드 (GET, POST, DELETE, PUT)이 하나의 핸들러 메소드로 종작함
    @RequestMapping("/menu/regist")
    public String registMenu(Model model) {
        model.addAttribute("message", "신규 등록된 핸들러 메소드 호출함");
        return "mappingResult";
    }

//    @RequestMapping(value = "/menu/modify", method = RequestMethod.GET)
    @RequestMapping("menu/modify")
    public String modifyMenu(Model model) {
        model.addAttribute("message", "GET 방식의 핸들러메소드 호출함");
        return "mappingResult";
    }

    /*
    * 요청 전용 어노테이션
    * @GetMapping
    * @PostMapping
    * @PutMapping
    * @DeleteMapping
    * @PatchMapping
    * */
}
