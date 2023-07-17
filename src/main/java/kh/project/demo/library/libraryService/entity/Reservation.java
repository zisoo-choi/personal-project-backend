package kh.project.demo.library.libraryService.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @Column(name = "reservationNumber")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationNumber;

    @JoinColumn(name = "memberNumber")
    private Long managerNumber; // 예약 회원

    @JoinColumn(name = "bookNumber")
    private Long bookNumber; // 예약 도서

    // 도서 예약 상태
    @Enumerated(EnumType.STRING)
    private ReservationState reservationState;

    // 예약 일시
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @CreationTimestamp
    private LocalDateTime reservationDate = LocalDateTime.now();
}
