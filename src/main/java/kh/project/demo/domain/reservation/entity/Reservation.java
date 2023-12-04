package kh.project.demo.domain.reservation.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import kh.project.demo.domain.book.entity.Book;
import kh.project.demo.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @Column(name = "reservationNumber")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationNumber;

    @JoinColumn(name = "memberNumber")
    @ManyToOne
    private Member member; // 예약 회원

    @JoinColumn(name = "bookNumber")
    @ManyToOne
    private Book book; // 예약 도서

    // 도서 예약 상태
    @Enumerated(EnumType.STRING)
    private ReservationState reservationState;

    // 예약 일시
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @CreationTimestamp
    @Builder.Default
    private LocalDateTime reservationDate = LocalDateTime.now();
}
