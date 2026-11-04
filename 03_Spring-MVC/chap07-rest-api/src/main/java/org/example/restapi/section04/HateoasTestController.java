package org.example.restapi.section04;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/*
* Hateoas (Hypermedia As The Of Application State)
* - REST API 응답에 현재 데이터 뿐 아니라, 해당 데이터와 관련된 다음 요청의 링크도 함께 제공하자!
* */
@RestController
@RequestMapping("/hateoas")
public class HateoasTestController {

    private List<UserDTO> users;
    private final DtoModelAssembler assembler;

    public HateoasTestController(DtoModelAssembler assembler) {
        this.assembler = assembler;
        this.users = new ArrayList<>();

        users.add(new UserDTO(1, "user01", "pass01", "홍길동", new Date()));
        users.add(new UserDTO(2, "user02", "pass02", "유관순", new Date()));
        users.add(new UserDTO(3, "user03", "pass03", "이순신", new Date()));
    }


    @GetMapping("/user/{userNo}")
    public ResponseEntity<ResponseMessage> findUserByUseNo(@PathVariable int userNo) {
        /*
         * applicadtion/json : HTTP의 응답이 JSON 형식의 데이터임을 의미
         * */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                new MediaType("application", "json", StandardCharsets.UTF_8)
        );

        UserDTO foundUser = new UserDTO();

        // 일치하는 회원 찾기
        for (UserDTO user : users) {
            if (user.getNo() == userNo) {
                foundUser = user;
                break;
            }
        }

        // 응답 데이터
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("user", foundUser);

        // 메서드 체이닝 + 빌더 패턴
        return ResponseEntity
                .ok() // 200 상태코드
                .headers(headers)
                .body(new ResponseMessage(200, "조회성공", responseMap));
    }

    // 유저 목록 전체 조회 -> 각각의 유저 정보가 전달되는데, 해당 정보를 단건조회할 수 있게 링크를 제공
    @GetMapping("/users")
    public ResponseEntity<ResponseMessage> findAllUsers() {

        // 사용자 목록을 EntityModel로 반환하기 위해 Assembler의 toModel 메서드 사용
        List<EntityModel<UserDTO>> userWithRel = users.stream()
                .map(user -> assembler.toModel(user))
//                .map(assembler::toModel)
                .toList();

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("users", userWithRel);

        ResponseMessage responseMessage = new ResponseMessage(200, "조회성공", responseMap);

        /*
         * application/json : HTTP의 응답이 JSON 형식의 데이터임을 의미
         * */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }
}
