package kh.project.demo.library.libraryService.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import kh.project.demo.library.book.entity.BookState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Rental {

    @Id
    @Column(name = "rentalNumber")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalNumber;

    @JoinColumn(name = "memberNumber")
    private Long memberNumber;

    @JoinColumn(name = "bookNumber")
    private Long bookNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @CreationTimestamp
    private LocalDateTime rentalDate = LocalDateTime.now(); // 대여 일자

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @UpdateTimestamp
    private LocalDateTime estimatedRentalDate; // 예상(정상) 반납 일자

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @UpdateTimestamp
    private LocalDateTime extensionDate; // 연장 일자

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @UpdateTimestamp
    private LocalDateTime returnDate; // 실제 반납 일자

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @UpdateTimestamp
    private LocalDateTime overdueDate; // 연체 일자

    // 회원 대여 상태
    @Enumerated(EnumType.STRING)
    private BookState bookState;
}
