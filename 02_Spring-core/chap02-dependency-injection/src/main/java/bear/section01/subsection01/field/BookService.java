package bear.section01.subsection01.field;

import bear.section01.common.BookDAO;
import bear.section01.common.BookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("bookServiceField")
public class BookService {

    /*
    * 스피링이 BookService 빈을 생성한 뒤, BookDAO 타입의 빈을 찾아 이 필드에 주입한다.
    * (여기서는 BookDAO의 구현체인 BookDAOImpl 빈이 주입)
    * */
    @Autowired // 주입 받을 때 적어야하는 어노테이션
    private BookDAO bookDAO;
    /*
    * - 필드를 final로 선언할 수 없어 의존관계의 불변성 보장 어려움
    * */

    public List<BookDTO> selectAllBook(){
        return bookDAO.selectBookList();
    }

    public BookDTO searchBookBySequence(int sequence) {
        return bookDAO.selectOneBook(sequence);
    }
}
