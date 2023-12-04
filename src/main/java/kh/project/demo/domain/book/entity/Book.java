package kh.project.demo.domain.book.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import kh.project.demo.domain.rental.entity.Rental;
import kh.project.demo.domain.reservation.entity.Reservation;
import kh.project.demo.domain.member.entity.Member;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

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
    @ManyToOne
    private Member manager; // 등록 관리자 번호
    // 한 명의 관리자는 한 개의 도서를 관리할 수 있다.

    @Column(name="filePathList" , unique=true)
    private String filePathList; // S3에 업로드된 도서 이미지

    private String bookName;

    private String author;

    private String publishCompany;

    @Setter
    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private KoreanDecimalClassification categorizationSymbol; // 분류 기호 -> 한국 십진 분류표

    @Setter
    private Integer rentalAmount; // 대여 가능 권 수

    @Setter
    private Integer bookAmount; // 전체 도서 권 수

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @CreationTimestamp
    @Builder.Default // 추가: 초기화 식을 기본값으로 사용
    private LocalDateTime registrationDate = LocalDateTime.now();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @UpdateTimestamp
    private LocalDateTime updateDate; // 업데이트 일자

    // 예약 엔티티와의 참조 관계 설정
    @OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Reservation> reservations;

    // 대여 엔티티와의 참조 관계 설정
    @OneToMany(mappedBy = "book", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Rental> rentals;

    public void minusAmount () {
        this.rentalAmount -= 1;
    }

    public void plusAmount () {
        this.rentalAmount += 1;
    }
}
