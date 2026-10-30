package section01.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

// Aspect : 로깅, 성능측정, 예외 처리같은 공통기능을 모아둔 클래스
// Advice : 실제 공통기능이 작성된 메서드
@Component
@Aspect
public class MemberAspect {

    // excution() 기본형태
    // excution(반환형 패키지.클래스.메서드명(매개변수))
    // * : 무엇이든 가능
    // (..) : 매개변수의 갯수와 타입은 무엇이든 가능
    @Pointcut("execution(* section01.aop.MemberServiceImpl.processMember(..))")
    public void advicePointCut() {
    }

    @Before("advicePointCut()")
    public void beforeAdvice() {
        System.out.println("[Before] 메서드 실행 전...");
    }

    @AfterReturning(value = "advicePointCut()", returning = "result")
    public void AfterReturningAdvice(Object result) {
        System.out.println("[AfterReturning] 반환값 : " + result);
    }

    @After("advicePointCut()")
    public void AfterAdvice() {
        System.out.println("[After] 메서드 실행 후...");
    }

    @AfterThrowing(pointcut = "advicePointCut()", throwing = "exception")
    public void afterThrowingAdvice(Exception exception) {
        System.out.println("[AfterThrowing] 예외 : " + exception.getMessage());
    }

    @Around("advicePointCut()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.nanoTime();
        System.out.println("[Around - 전] 메서드 실행 전");

        try {
            // proceed() 가 Target 메서드를 실행한다.
            return joinPoint.proceed();
        } finally {
            long executionTime = System.nanoTime() - startTime;
            System.out.println("[Around - 후] 실행 시간 : " + executionTime + "ns");
        }
    }

    /*
    Within : 해당 클래스 내부를 선택
    MemberServiceImpl 클래스가 실행되기 전에 실행 && 반환형 상관 없고 메서드 명은 selectmembers()에 해당하면 출력해라
    */
    @Before("within(section01.aop.MemberServiceImpl) && execution(* selectMembers(..))")
    public void withinPointCut() {
        System.out.println("[within] MemberServiceImpl 적용");
    }

    /*
    this : 해당 타입을 선택
    */
    @Before("this(section01.aop.MemberService) && execution(* checkMember(..))")
    public void thisPointCut() {
        System.out.println("[this] MemberService 적용");
    }

    /*
    args : 해당 매개변수를 가진 메소드에 적용
    */
    @Before(value = "args(name, age) && execution(* registerMember(..))", argNames = "name, age")
    public void argsPointCut(String name, int age) {
        System.out.println("[args] MemberService 적용");
    }

    // 어노테이션 만들기
    @Before("@annotation(section01.aop.AopLog)")
    public void annotationPointCut() {
        System.out.println("[annotation] @AopLog 적용");
    }

    @Before("execution(* section01.aop.MemberServiceImpl.selectMember(..))")
    public void executionPointCut() {
        System.out.println("[execution] MemberServiceImpl 적용");
    }
}
