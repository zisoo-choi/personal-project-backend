package kh.project.demo.library.book.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @Column(name = "BookNumber")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookNumber; // 도서 번호

    @JoinColumn(name = "memberNumber")
    private Long managerNumber; // 등록 관리자 번호

//    @Column(name="isbn" , unique=true)
//    private String isbn; // 국제 표준 도서 번호
    // --> 이거 대신에 그냥 도서 번호를 사용해도 좋을 것 같다는 생각 중

    private String bookName;

    private String author;

    private String publishCompany;

    @Setter
    private String content;

    @Enumerated(EnumType.STRING)
    private KoreanDecimalClassification categorizationSymbol; // 분류 기호 -> 한국 십진 분류표

    @Setter
    private Integer bookAmount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @CreationTimestamp
    private LocalDateTime registrationDate = LocalDateTime.now();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @UpdateTimestamp
    private LocalDateTime updateDate; // 업데이트 일자

    public Book(Long managerNumber, String bookName, String author, String publishCompany, String content, KoreanDecimalClassification categorizationSymbol, Integer bookAmount) {
        this.managerNumber = managerNumber;
//        this.isbn = "BOOK_"+isbn;
        this.bookName = bookName;
        this.author = author;
        this.publishCompany = publishCompany;
        this.content = content;
        this.categorizationSymbol = categorizationSymbol;
        this.bookAmount = bookAmount;
    }

    public void bookAddAmount () {
        this.bookAmount += 1;
    }
}
