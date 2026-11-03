package org.example.restapi.section01.response;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
* ResponseBody
* - 반환값이 응답 본문(Body)에 직접 쓰여짐 (View를 거치지 않는다)
* - 주로 JSON, XML 같은 형식의 데이터를 HTTP로 반환하고자할 때 사용
* */
//@Controller
//@ResponseBody
@RestController
@RequestMapping("/response")
public class ResponseRestController {

    // 문자열 응답
    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    // 기본 자료형 응답
    @GetMapping("/random")
    public int random() {
        return (int) (Math.random() * 10) + 1;
    }

    // Object 응답
    @GetMapping("/message")
    public Message getMessage() {
        return new Message(200, "나만의 메시지 응답");
    }

    /*
    * list : 배열로 인식
      [
        "사과",
        "바나나",
        "복숭아"
      ]
       * map, 일반 클래스 형식 : 객체로 인식
      {
        "404": "페이지를 찾을 수 없습니다.",
        "500": "개발자 잘못",
        "200": "정상응답"
      }
    * */

    /*
    * JSON : Javascript Object Natation
    * - 자바 스크립트가 인지할 수 있는 객체 형태의 문자열
    * 배열 -> [] 형태로 표기 (리스트)
    * 객체 -> {} 형태로 표기 (클래스타입, 맵)
    * */
    @GetMapping("/list")
    public List<String> getList() {
        return List.of(new String[] {"사과", "바나나", "복숭아"});
    }

    @GetMapping("/map")
    public Map<Integer, String> getMap() {
        List<Message> messageList = new ArrayList<>();
        messageList.add(new Message(200, "정상응답"));
        messageList.add(new Message(404, "페이지를 찾을 수 없습니다."));
        messageList.add(new Message(500, "개발자 잘못"));

        return messageList.stream().collect(Collectors.toMap(Message::getHttpStatusCode, Message::getMessage));
    }

    /*
    * 정적 리소스(이미지파일) 응답
    * - produces 설정을 하지 않으면 text/html로 응답하기 때문에 이미지가 전송되지 않음
    * */
    @GetMapping(value = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage() throws IOException {
        return getClass().getResourceAsStream("/static/sandw.jpg").readAllBytes();
    }

    /*
    * ResponseEntity
    * - Http 요청에 대한 응답을 구성할 때 사용하는 클래스
    * - 응답 본문, 상태코드, 응답 헤더 등을 포함한 전체 HTTP 응답을 제어할 수 있다.
    * */
    @GetMapping("/entity")
    public ResponseEntity<Message> getEntity() {
//        return ResponseEntity.ok(new Message(200, "응답 성공!"));
        return ResponseEntity.status(500).body(new Message(200, "응답 성공!"));
    }
}
