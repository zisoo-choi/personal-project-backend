package kh.project.demo.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import kh.project.demo.domain.requestedBook.entity.RequestedBook;
import kh.project.demo.domain.rental.entity.Rental;
import kh.project.demo.domain.reservation.entity.Reservation;
import lombok.*;

import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @Column(name = "memberNumber")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberNumber;

    @Column(name="memberId" , unique=true)
    private String memberId;

    private String memberPw;

    @Column(name="email" , unique=true)
    private String email;

    @Setter
    private String address;

    @Setter
    private String phoneNumber;

    @Setter
    @Enumerated(EnumType.STRING)
    private MemberState memberState;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    @Setter
    @Enumerated(EnumType.STRING)
    private MemberServiceState memberServiceState;

    // 예약 엔티티와의 참조 관계 설정
    @JsonIgnore
    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER)
    private List<Reservation> reservations;

    // 희망 도서 엔티티와의 참조 관계 설정
    @JsonIgnore
    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER)
    private List<RequestedBook> requestedBooks;

    // 대여 엔티티와의 참조 관계 설정
    @JsonIgnore
    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER)
    private List<Rental> rentals;

    @Setter
    private Integer availableAmount; // 회원이 동시에 대여할 수 있는 한도 수

    public void minusAmount () {
        this.availableAmount -= 1;
    }

    public void plusAmount () {
        this.availableAmount += 1;
    }
}
