package kh.project.demo.library.libraryService.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import kh.project.demo.library.member.entity.Member;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HopeBook {

    @Id
    @Column(name = "HopeBookNumber")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hopeBookNumber;

    @JoinColumn(name = "memberNumber")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Member member
            ; // 희망도서 신청 회원
    // 한 명의 회원이 여러 개의 희망 도서를 가질 수 있다.

    private String bookName;

    private String author;

    private String publishCompany;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @CreationTimestamp
    @Builder.Default // 추가: 초기화 식을 기본값으로 사용
    private LocalDateTime applicationDate = LocalDateTime.now();
}
