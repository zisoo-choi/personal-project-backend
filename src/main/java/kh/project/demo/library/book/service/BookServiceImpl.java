package kh.project.demo.library.book.service;

import kh.project.demo.library.book.controller.form.request.RegisterBookForm;
import kh.project.demo.library.book.entity.Book;
import kh.project.demo.library.book.repository.BookRepository;
import kh.project.demo.library.member.entity.MemberRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService{

    final private BookRepository bookRepository;

    @Override
    public boolean registerationBook(RegisterBookForm requestForm) {
        // 등록하는 사람의 역할 검증
        if (requestForm.getMemberRole().equals(MemberRole.NORMAL)){
            log.info("관리자가 아니므로 권한이 없습니다.");
            return false;
        }

        // 도서 존재 여부 확인
        Optional<Book> maybeBook = bookRepository.findByIsbn(requestForm.getIsbn());

        if (maybeBook.isPresent()) {
            Book book = maybeBook.get();
            book.bookAddAmount(1); // 존재하는 도서는 수량 +1
            log.info("존재하는 도서 이므로 수량이 증가합니다.");
            return false;
        }

        // 존재하지 않는 도서라면
        if (maybeBook.isEmpty()) {
            // 저장해준다.
            bookRepository.save(requestForm.registerBook());
            log.info("도서 등록이 완료 되었습니다.");
            return true;
        }

        return false;
    }

    @Override
    public List<Book> registerationDateSort(){

        return null; // 코드 작성해야 함
    }
}
