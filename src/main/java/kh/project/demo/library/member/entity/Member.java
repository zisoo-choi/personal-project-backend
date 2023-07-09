package kh.project.demo.library.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String email;

    @Setter
    private String address;

    @Setter
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private MemberState memberState;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;
}
