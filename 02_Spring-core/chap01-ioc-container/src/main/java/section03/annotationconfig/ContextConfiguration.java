package section03.annotationconfig;

import common.MemberDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;

// @Configuration이 정의된 클래스가 설정파일임을 의미(bean)
/*
* ComponentScan : @Component 계열의 어노테이션을 스캔해 모두 Bean으로 등록 -> @Controllor / @Service / @Repository / @Entity ...
* basePackages = 어디서 찾을지 지정해줌
* includeFilters = 그 범위 안에서 어떤 것을 포함할지
* excludeFilters = 그 범위 안에서 어떤 것을 제외할지
* */
@ComponentScan(basePackages = "common", useDefaultFilters = false /*기본필터 끄기*/,
        includeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {MemberDTO.class})})
@Configuration("configurationSection03")
public class ContextConfiguration {
}
