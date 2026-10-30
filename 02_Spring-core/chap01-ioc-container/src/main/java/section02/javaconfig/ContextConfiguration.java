package section02.javaconfig;

import common.MemberDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// @Configuration이 정의된 클래스가 설정파일임을 의미(bean)
@Configuration("configurationSection02")
public class ContextConfiguration {

    /*
    * 직접 빈을 등록해야하는 경우만 JAVA Config 방식을 사용함.
    * - 외부 라이브러리를 사용할 때 : 외부라이브러리에 어노테이션을 붙일 수 없음.
    * - 객체를 꼭 초기화해야할 때 : 설정값을 넣어서 Bean으로 등록함.
    * */

    @Bean(name="member")
    public MemberDTO getMember() {
        return new MemberDTO(1, "use01", "psaa01", "bear");
    }
}
