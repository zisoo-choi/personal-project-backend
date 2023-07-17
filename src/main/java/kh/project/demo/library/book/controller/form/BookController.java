package kh.project.demo.library.book.controller.form;

import kh.project.demo.library.book.controller.form.request.RegisterBookForm;
import kh.project.demo.library.book.controller.form.request.RequestBookBoardForm;
import kh.project.demo.library.book.entity.Book;
import kh.project.demo.library.book.entity.KoreanDecimalClassification;
import kh.project.demo.library.book.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/book-list")
public class BookController {

    final private BookService bookService;

    // 도서 등록
    @PostMapping("/register-book")
    public Book registerBook(@RequestBody RegisterBookForm requestForm) {
        return bookService.register(requestForm);
    }

    // 도서 수정
    @PutMapping("/{bookNumber}")
    public Book modifyBook (@PathVariable("bookNumber") Long bookNumber,
                            @RequestBody RequestBookBoardForm requestBookBoardForm) {
        log.info("modifyBook(): "+ requestBookBoardForm +", id: "+bookNumber);

        return bookService.modify(bookNumber, requestBookBoardForm);
    }

    // 도서 삭제
    @DeleteMapping("/{bookNumber}")
    public boolean deleteBook (@PathVariable("bookNumber") Long bookNumber) {
        log.info("deleteBook()");
        return bookService.delete(bookNumber);
    }

    // 신간 도서 요청
    @GetMapping("/registration-date")
    public List<Book> newBook() {
        log.info("신간 도서 요청 !");
        return bookService.registerationDateSort();
    }

    // 분야별 도서 요청
    @GetMapping("/{categorizationSymbol}")
    public List<Book> categoryBook(
            @PathVariable("categorizationSymbol") KoreanDecimalClassification categorizationSymbol) {
        log.info("분야별 도서 요청 !");
        return bookService.listByfield(categorizationSymbol);
    }

}
