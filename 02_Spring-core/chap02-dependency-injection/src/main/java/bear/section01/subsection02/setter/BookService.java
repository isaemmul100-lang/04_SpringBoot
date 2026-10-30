package bear.section01.subsection02.setter;

import bear.section01.common.BookDAO;
import bear.section01.common.BookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("bookServiceSetter")
public class BookService {

    // final 키워드 사용불가
    // setter 를 이용해 의존 객체 교체 가능
    private BookDAO bookDAO;

    // 세터 주입
    // BookService 빈을 생성할 때 BookDAO 타입의 빈을 세터 인자로 전달한다.
    @Autowired
    public void setBookDAO(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    // 도서목록 조회
    public List<BookDTO> selectAllBook(){
        return bookDAO.selectBookList();
    }

    // sequence로 도서 조회
    public BookDTO searchBookBySequence(int sequence) {
        return bookDAO.selectOneBook(sequence);
    }
}
