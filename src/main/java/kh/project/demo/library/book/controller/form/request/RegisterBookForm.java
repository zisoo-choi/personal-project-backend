package kh.project.demo.library.book.controller.form.request;

import kh.project.demo.library.book.entity.Book;
import kh.project.demo.library.book.entity.BookState;
import kh.project.demo.library.book.entity.KoreanDecimalClassification;
import kh.project.demo.library.member.entity.MemberRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RegisterBookForm {

    private String bookName;
//    private String isbn;
    private Long registerManagerNumber; // 등록 관리자 번호
    private String author;
    private String publicCompany;
    private String content;
    private KoreanDecimalClassification categorizationSymbol;
    private Integer bookAmount;
    private MemberRole memberRole; // 등록하는 사용자의 역할
    private BookState bookState;

    public Book toRegisterBook() {
        return Book.builder()
                .managerNumber(registerManagerNumber)
                .bookName(bookName)
                .author(author)
                .publishCompany(publicCompany)
                .content(content)
                .categorizationSymbol(categorizationSymbol)
                .bookState(BookState.BookBefore)
                .build();
    }
}
