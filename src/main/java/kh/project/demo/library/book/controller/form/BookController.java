package kh.project.demo.library.book.controller.form;

import kh.project.demo.library.book.entity.Book;
import kh.project.demo.library.book.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/book-list")
public class BookController {

    final private BookService bookService;

    @GetMapping("/registration-date")
    public List<Book> newBook() {
        log.info("신간 도서 요청 !");
        return bookService.registerationDateSort();
    }
}
