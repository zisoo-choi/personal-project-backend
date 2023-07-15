package kh.project.demo.library.member.controller.form.request;

import kh.project.demo.library.member.entity.Member;
import kh.project.demo.library.member.entity.MemberRole;
import kh.project.demo.library.member.entity.MemberState;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MemberSignUpForm {

    private String memberId;
    private String memberPw;
    private String email;
    private String address;
    private String phoneNumber;
    private MemberRole memberRole;

    public Member toMember(String encodePassword) {
        return Member.builder()
                .memberId(memberId)
                .memberPw(encodePassword)
                .email(email)
                .address(address)
                .phoneNumber(phoneNumber)
                .memberRole(memberRole)
                .memberState(MemberState.OK)
                .build();
    }
}
