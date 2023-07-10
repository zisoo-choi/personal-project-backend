package kh.project.demo.library.member.service;

import kh.project.demo.library.member.controller.form.request.MemberBasicForm;
import kh.project.demo.library.member.controller.form.request.MemberSignUpForm;
import kh.project.demo.library.member.controller.form.response.MemberLoginRespnseForm;

public interface MemberService {

    Boolean checkIdDuplication(String id);

    Boolean memberSignUp(MemberSignUpForm memberSignUpForm);

    MemberLoginRespnseForm memberSignIn(MemberBasicForm memberSignInForm);

    boolean memberDelete(MemberBasicForm memberDeleteForm);
}
