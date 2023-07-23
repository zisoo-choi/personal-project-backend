package kh.project.demo.library.libraryService.controller.form.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import kh.project.demo.library.book.entity.KoreanDecimalClassification;
import kh.project.demo.library.libraryService.entity.HopeBook;
import kh.project.demo.library.member.entity.Member;
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

    public HopeBook toHopeBook(Member member) {
        return HopeBook.builder()
                .bookName(bookName)
                .author(author)
                .publishCompany(publishCompany)
                .member(member)
                .applicationDate(LocalDateTime.now())
                .build();
    }
}
