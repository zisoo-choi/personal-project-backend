package kh.project.demo.library.libraryService.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import kh.project.demo.library.book.entity.Book;
import kh.project.demo.library.member.entity.Member;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rental {

    @Id
    @Column(name = "rentalNumber")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalNumber;

    @JoinColumn(name = "memberNumber")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Member member;
    // 한 명의 회원은 여러 권의 책을 대여할 수 있다.

    @JoinColumn(name = "bookNumber")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Book book;
    // 한 권은 책은 대여가 하나이다. ?

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @CreationTimestamp
    private final LocalDateTime rentalDate = LocalDateTime.now(); // 대여 일자

    @Setter
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime estimatedRentalDate; // 예상(정상) 반납 일자

    @Setter
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime extensionDate; // 연장 일자

    @Setter
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime returnDate; // 실제 반납 일자

    @Setter
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime overdueDate; // 연체 일자

    // 회원 대여 상태
    @Setter
    @Enumerated(EnumType.STRING)
    private RentalState rentalState;
}
