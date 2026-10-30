package org.example.requestmapping.chap02;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/order/*")
public class ClassMappingTestController {

    // /order/regist GET
    @GetMapping("/regist")
    public String registOrder(Model model) {

        model.addAttribute("message", "/order/regist 요청 결과");

        return "mappingResult";
    }

    // 여러개의 요청 매핑 : @RequestMapping의 value 속성에 중괄호를 이용해 매핑할 URL을 나열 가능 -> 일반적으로 사용 X
    // /order/modify
    // /order/delete
    @RequestMapping(value = {"modify", "delete"}, method = {RequestMethod.DELETE, RequestMethod.POST})
    public String modifyAndDelete(Model model) {

        model.addAttribute("message", "/order/modify 와 /order/delete 요청 결과");

        return "mappingResult";
    }

    // PathVariable : URL에서 변수를 받아올 수 있음
    // @PathVariable(Path에 적힌 변수명) 타입 변수명
    // @PathVariable 타입 Path에 적힌 변수명
    @GetMapping("/detail/{orderNo}") // {[역할]} : 역할을 부여하겠음 -> 핸들러 메소드로 가지고 올수있음
//    public String selectOrderDetail(Model model, @PathVariable int orderNo") {
    public String selectOrderDetail(Model model, @PathVariable("orderNo") int orderNo) {

        model.addAttribute("message", orderNo + "번 주문 상제조회합니다.");

        return "mappingResult";
    }

    @RequestMapping // URL에 뭐를 치든 이 메시지 출력
    public String anyRequest(Model model) {

        model.addAttribute("message", "그 외 모든 요청을 여기서 처리합니다.");

        return "mappingResult";
    }
}
