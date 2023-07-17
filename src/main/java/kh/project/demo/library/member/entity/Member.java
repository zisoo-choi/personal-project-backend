package kh.project.demo.library.member.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @Enumerated(EnumType.STRING)
    private MemberServiceState memberServiceState;
}
