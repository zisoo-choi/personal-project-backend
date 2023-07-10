package kh.project.demo.library.member.controller.form.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
public class MemberSignInForm {

    private String userId;
    private String userPw;
}
