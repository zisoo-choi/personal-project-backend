package kh.project.demo.library.book.controller.form.request;

import kh.project.demo.library.book.entity.Book;
import kh.project.demo.library.book.entity.KoreanDecimalClassification;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RegisterBookForm {

    private String bookName;
//    private String isbn;
    private Long registerManagerNumber; // 등록 관리자 번호
    private String author;
    private String publishCompany;
    private String content;
    private KoreanDecimalClassification categorizationSymbol;
    private Integer bookAmount;

    public Book toRegisterBook() {
        return Book.builder()
                .managerNumber(registerManagerNumber)
                .bookName(bookName)
                .author(author)
                .publishCompany(publishCompany)
                .content(content)
                .categorizationSymbol(categorizationSymbol)
                .bookAmount(bookAmount)
                .build();
    }
}
