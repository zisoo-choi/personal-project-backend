package kh.project.demo.library.book.service;

import kh.project.demo.library.book.controller.form.request.RegisterBookForm;
import kh.project.demo.library.book.controller.form.request.ModifyBookForm;
import kh.project.demo.library.book.entity.Book;
import kh.project.demo.library.book.entity.KoreanDecimalClassification;

import java.util.List;

public interface BookService {

    boolean delete(Long bookNumber);

    List<Book> registerationDateSort();

    List<Book> listByfield(KoreanDecimalClassification categorizationSymbol);

    Book read(Long bookNumber);

    List<Book> list();

    Book register(RegisterBookForm requestForm, String userId);

    Book modify(Long bookNumber, ModifyBookForm modifyBookForm, String userId);
}
