package kh.project.demo.library.book.service;

import kh.project.demo.library.book.controller.form.request.RegisterBookForm;
import kh.project.demo.library.book.controller.form.request.ModifyBookForm;
import kh.project.demo.library.book.entity.Book;
import kh.project.demo.library.book.entity.KoreanDecimalClassification;

import java.util.List;

public interface BookService {
    Book modify(Long bookId, ModifyBookForm modifyBookForm);

    Book register(RegisterBookForm requestForm);

    boolean delete(Long bookNumber);

    List<Book> registerationDateSort();

    List<Book> listByfield(KoreanDecimalClassification categorizationSymbol);

    Book read(Long bookNumber);

    List<Book> list();
}
