package kh.project.demo.domain.requestedBook.controller.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import kh.project.demo.domain.book.entity.KoreanDecimalClassification;
import kh.project.demo.domain.requestedBook.entity.RequestedBook;
import kh.project.demo.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HopeBookForm {

    private String bookName;
    private String author;
    private String publishCompany;
    private KoreanDecimalClassification categorizationSymbol;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime applicationDate = LocalDateTime.now(); // 신청 일자

    public RequestedBook toHopeBook(Member member) {
        return RequestedBook.builder()
                .bookName(bookName)
                .author(author)
                .publishCompany(publishCompany)
                .member(member)
                .applicationDate(LocalDateTime.now())
                .build();
    }
}
