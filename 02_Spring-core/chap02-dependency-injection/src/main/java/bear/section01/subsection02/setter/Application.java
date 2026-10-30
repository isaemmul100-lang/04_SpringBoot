package bear.section01.subsection02.setter;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext("bear.section01");

        BookService bookService = context.getBean("bookServiceSetter", BookService.class);

        // 동작확인 코드
        bookService.selectAllBook().forEach(System.out::println);
        System.out.println(bookService.searchBookBySequence(1));
    }
}
