package kh.project.demo.library.book.service;

import kh.project.demo.library.book.controller.form.request.RegisterBookForm;
import kh.project.demo.library.book.controller.form.request.RequestBookBoardForm;
import kh.project.demo.library.book.entity.Book;

import java.util.List;

public interface BookService {
    List<Book> registerationDateSort();

    boolean registerationBook(RegisterBookForm requestForm);

    Book modify(Long bookId, RequestBookBoardForm requestBookBoardForm);
}
