package kh.project.demo.library.member.controller.form.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberBasicForm {

    private String userId;
    private String userPw;
}
