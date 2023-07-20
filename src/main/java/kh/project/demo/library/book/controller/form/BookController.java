package kh.project.demo.library.book.controller.form;

import kh.project.demo.library.book.controller.form.request.RegisterBookForm;
import kh.project.demo.library.book.controller.form.request.ModifyBookForm;
import kh.project.demo.library.book.entity.Book;
import kh.project.demo.library.book.entity.KoreanDecimalClassification;
import kh.project.demo.library.book.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public Book registerBook(@RequestBody RegisterBookForm requestForm,
                             @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails != null) {
            String userId = userDetails.getUsername();
            return bookService.register(requestForm, userId);
        }

        return null;
    }

    // 도서 수정
    @PutMapping("/modify-book/{bookNumber}")
    public Book modifyBook (@PathVariable("bookNumber") Long bookNumber,
                            @RequestBody ModifyBookForm modifyBookForm,
                            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("modifyBook(): "+ modifyBookForm +", id: "+bookNumber);

        if(userDetails != null) {
            String userId = userDetails.getUsername();
            return bookService.modify(bookNumber, modifyBookForm, userId);
        }
        return null;
    }

    // 도서 삭제
    @DeleteMapping("/delete-book/{bookNumber}")
    public boolean deleteBook (@PathVariable("bookNumber") Long bookNumber) {
        log.info("deleteBook()");
        return bookService.delete(bookNumber);
    }

    // 도서 상세 페이지 읽기
    @GetMapping("/read-book/{bookNumber}")
    public Book readBook (@PathVariable("bookNumber") Long bookNumber) {
        log.info("readBook()");
        return bookService.read(bookNumber);
    }

    // 신간 도서 요청
    @GetMapping("/registration-date")
    public List<Book> newBook() {
        log.info("신간 도서 요청 !");
        return bookService.registerationDateSort();
    }

    // 분야별 도서 요청
    @GetMapping("/category-book/{categorizationSymbol}")
    public List<Book> categoryBook(
            @PathVariable("categorizationSymbol") KoreanDecimalClassification categorizationSymbol) {
        log.info("분야별 도서 요청 !");
        return bookService.listByfield(categorizationSymbol);
    }

    // 전체 도서 요청
    @GetMapping("/whole-book")
    public List<Book> wholeBook() {
        log.info("전체 도서 목록 요청 !");

        List<Book> returnedBookList = bookService.list();
        return returnedBookList;
    }

}
