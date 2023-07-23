package kh.project.demo.library.book.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import kh.project.demo.library.member.entity.Member;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @Column(name = "bookNumber")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookNumber; // 도서 번호

    @Setter
    @JoinColumn(name = "memberNumber")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Member manager; // 등록 관리자 번호
    // 한 명의 관리자는 한 개의 도서를 관리할 수 있다.

//    @Column(name="isbn" , unique=true)
//    private String isbn; // 국제 표준 도서 번호
    // --> 이거 대신에 그냥 도서 번호를 사용해도 좋을 것 같다는 생각 중

    private String bookName;

    private String author;

    private String publishCompany;

    @Setter
    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private KoreanDecimalClassification categorizationSymbol; // 분류 기호 -> 한국 십진 분류표

    @Setter
    private Integer bookAmount; // 전체 도서 권 수

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @CreationTimestamp
    @Builder.Default // 추가: 초기화 식을 기본값으로 사용
    private LocalDateTime registrationDate = LocalDateTime.now();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @UpdateTimestamp
    private LocalDateTime updateDate; // 업데이트 일자

//    public Book(Long managerNumber, String bookName, String author,
//                String publishCompany, String content,
//                KoreanDecimalClassification categorizationSymbol, Integer bookAmount) {
//        this.managerNumber = managerNumber;
////        this.isbn = "BOOK_"+isbn;
//        this.bookName = bookName;
//        this.author = author;
//        this.publishCompany = publishCompany;
//        this.content = content;
//        this.categorizationSymbol = categorizationSymbol;
//        this.bookAmount = bookAmount;
//    }

    public void minusAmount () {
        this.bookAmount -= 1;
    }
}
