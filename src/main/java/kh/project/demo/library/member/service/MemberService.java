package kh.project.demo.library.member.service;

import kh.project.demo.library.member.controller.form.MemberSignUpForm;

public interface MemberService {

    Boolean checkIdDuplication(String id);

    Boolean memberSignUp(MemberSignUpForm memberSignUpForm);
}
