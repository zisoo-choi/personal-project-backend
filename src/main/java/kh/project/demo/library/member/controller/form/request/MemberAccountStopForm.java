package kh.project.demo.library.member.controller.form.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberAccountStopForm {

    private String memberId;
    private String managerId;
}
