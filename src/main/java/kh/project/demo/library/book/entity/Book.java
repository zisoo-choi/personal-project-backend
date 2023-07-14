package kh.project.demo.library.book.entity;

import jakarta.persistence.*;
import kh.project.demo.library.member.entity.Member;
import lombok.*;

import java.time.LocalDate;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @Column(name = "BookNumber")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberNumber")
    private Member memberNumber;

    @Column(name="isbn" , unique=true)
    private String isbn;

    @Column(name="bookName" , unique=true)
    private String bookName;

    private String author;

    private String publishCompany;

    @Setter
    private String content;

    private String categorizationSymbol;

    @Enumerated(EnumType.STRING)
    private KoreanDecimalClassification classificationCode;

    @Setter
    private Integer bookAmount;

    private LocalDate registrationDate;
}
