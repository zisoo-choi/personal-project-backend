package kh.project.demo.library.book.service;

import kh.project.demo.library.book.controller.form.request.RegisterBookForm;
import kh.project.demo.library.book.controller.form.request.RequestBookBoardForm;
import kh.project.demo.library.book.entity.Book;
import kh.project.demo.library.book.entity.KoreanDecimalClassification;

import java.util.List;

public interface BookService {
    Book modify(Long bookId, RequestBookBoardForm requestBookBoardForm);

    Book register(RegisterBookForm requestForm);

    boolean delete(Long bookNumber);

    List<Book> registerationDateSort();

    List<Book> listByfield(KoreanDecimalClassification categorizationSymbol);

    Book read(Long bookNumber);
}
