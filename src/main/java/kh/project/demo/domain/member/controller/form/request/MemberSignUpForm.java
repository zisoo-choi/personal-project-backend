package kh.project.demo.domain.member.controller.form.request;

import kh.project.demo.domain.member.entity.Member;
import kh.project.demo.domain.member.entity.MemberRole;
import kh.project.demo.domain.member.entity.MemberServiceState;
import kh.project.demo.domain.member.entity.MemberState;
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
    private MemberServiceState memberServiceState;

    public Member toMember(String encodePassword) {
        return Member.builder()
                .memberId(memberId)
                .memberPw(encodePassword)
                .email(email)
                .address(address)
                .phoneNumber(phoneNumber)
                .memberRole(memberRole)
                .memberState(MemberState.OK)
                .memberServiceState(MemberServiceState.ServiceNormal)
                .availableAmount(5)
                .build();
    }
}
