package kh.project.demo.domain.book.service;

import kh.project.demo.domain.book.controller.form.request.RegisterBookForm;
import kh.project.demo.domain.book.controller.form.request.ModifyBookForm;
import kh.project.demo.domain.book.entity.Book;
import kh.project.demo.domain.book.entity.KoreanDecimalClassification;

import java.util.List;

public interface BookService {

    List<Book> registerationDateSort();

    List<Book> listByfield(KoreanDecimalClassification categorizationSymbol);

    Book read(Long bookNumber);

    List<Book> list();

    Book register(RegisterBookForm requestForm, String userId);

    Book modify(Long bookNumber, ModifyBookForm modifyBookForm, String userId);

//    boolean delete(Long bookNumber, int deleteCount);
    boolean delete(Long bookNumber);
}
