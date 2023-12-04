package kh.project.demo.domain.book.controller.form.request;

import kh.project.demo.domain.book.entity.Book;
import kh.project.demo.domain.book.entity.KoreanDecimalClassification;
import kh.project.demo.domain.member.entity.Member;
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
    private String filePathList;


    public Book toRegisterBook(Member member) {
        return Book.builder()
                .manager(member)
                .bookName(bookName)
                .author(author)
                .publishCompany(publishCompany)
                .content(content)
                .categorizationSymbol(categorizationSymbol)
                .bookAmount(bookAmount)
                .rentalAmount(bookAmount)
                .filePathList(filePathList)
                .build();
    }
}
