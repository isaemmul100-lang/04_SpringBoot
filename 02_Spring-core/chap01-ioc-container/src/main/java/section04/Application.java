package section04;

import common.MemberDTO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import section02.javaconfig.ContextConfiguration;

public class Application {

    public static void main(String[] args) {

        // 일반적인 객체 만드는 방법
        MemberDTO javaMember1 = new MemberDTO(1, "user01", "pass01", "홍길동");
        MemberDTO javaMember2 = new MemberDTO(1, "user01", "pass01", "홍길동");

        System.out.println("javaMember1 == javaMember2 : " + (javaMember1 == javaMember2));

        ApplicationContext context =
                new AnnotationConfigApplicationContext(ContextConfiguration.class);

        // Singleton -> SpringContext Bean으로 등록되는 객체는 기본적으로 Singleton이다.
        // 객체를 직접 만들지 않음에도 ApplicationContext가 객체를 만들어서 가져와줌.
        MemberDTO member1 = context.getBean("member", MemberDTO.class);
        MemberDTO member2 = context.getBean("member", MemberDTO.class);

        System.out.println("member1 == member2 : " + (member1 == member2));

    }
}