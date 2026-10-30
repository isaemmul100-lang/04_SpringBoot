package common;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

// @Repository : 특별한 기능수행이 없이 클래스 계층의 의미를 담고있는 어노테이션
//@Component
@Repository
public class MemberDAO {
    private final Map<Integer, MemberDTO> memberMap;

    public MemberDAO() {
        memberMap = new HashMap<>();

        memberMap.put(1, new MemberDTO(1, "user01", "pass01", "홍길동"));
        memberMap.put(2, new MemberDTO(2, "user02", "pass02", "유관순"));
    }

    public MemberDTO selectMember(int sequence) {
        return memberMap.get(sequence);
    }

    public boolean insertMember(MemberDTO newMember) {

        int before = memberMap.size();

        memberMap.put(newMember.getSequence(), newMember);

        int after = memberMap.size();

        return after > before;
    }
}
