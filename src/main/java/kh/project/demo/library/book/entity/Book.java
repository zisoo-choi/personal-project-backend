package kh.project.demo.library.book.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import kh.project.demo.library.member.entity.Member;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @Column(name = "BookNumber")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookNumber; // 도서 번호

    @Column(name="isbn" , unique=true)
    private String isbn; // 국제 표준 도서 번호

    private String bookName;

    private String author;

    private String publishCompany;

    @Setter
    private String content;

//    private String categorizationSymbol;

    @Enumerated(EnumType.STRING)
    private KoreanDecimalClassification categorizationSymbol; // 분류 기호 -> 한국 십진 분류표

    @Setter
    private Integer bookAmount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @CreationTimestamp
    private LocalDate registrationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @UpdateTimestamp
    private LocalDateTime updateDate; // 업데이트 일자

    public Book(String isbn, String bookName, String author, String publishCompany, String content, KoreanDecimalClassification categorizationSymbol, Integer bookAmount) {
        this.isbn = "BOOK_"+isbn;
        this.bookName = bookName;
        this.author = author;
        this.publishCompany = publishCompany;
        this.content = content;
        this.categorizationSymbol = categorizationSymbol;
        this.bookAmount = bookAmount;
    }

    public void bookAddAmount (Integer plusBook) {
        this.bookAmount += plusBook;
    }
}
