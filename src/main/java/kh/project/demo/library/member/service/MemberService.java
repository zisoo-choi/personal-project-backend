package kh.project.demo.library.member.service;

import kh.project.demo.library.member.controller.form.request.MemberAccountStopForm;
import kh.project.demo.library.member.controller.form.request.MemberBasicForm;
import kh.project.demo.library.member.controller.form.request.MemberSignUpForm;

public interface MemberService {

    Boolean checkIdDuplication(String id);

    Boolean memberSignUp(MemberSignUpForm memberSignUpForm);

    boolean memberDelete(MemberBasicForm memberDeleteForm);

    boolean memberAccountStop(MemberAccountStopForm memberAccountForm);

    Boolean checkEmailDuplication(String email);

    Integer inquiryLimitsBook(String userId);
}
