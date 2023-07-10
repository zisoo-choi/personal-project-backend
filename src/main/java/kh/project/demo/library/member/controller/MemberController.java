package kh.project.demo.library.member.controller;

import kh.project.demo.library.member.controller.form.request.MemberIdCheckForm;
import kh.project.demo.library.member.controller.form.request.MemberBasicForm;
import kh.project.demo.library.member.controller.form.request.MemberSignUpForm;
import kh.project.demo.library.member.controller.form.response.MemberLoginRespnseForm;
import kh.project.demo.library.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/library-member")
public class MemberController {

    final private MemberService memberService;

    @PostMapping("/cheke-id")
    public Boolean checkId(@RequestBody MemberIdCheckForm memberIdCheckForm) {
        log.info("check id duplication: "+ memberIdCheckForm.getMemberId());

        return memberService.checkIdDuplication(memberIdCheckForm.getMemberId());
    }

    @PostMapping("/sign-up")
    public Boolean signIn(@RequestBody MemberSignUpForm memberSignUpForm) {
        log.info("sign Up()");

        return memberService.memberSignUp(memberSignUpForm);
    }

    @PostMapping("/sign-In")
    public MemberLoginRespnseForm signIn(@RequestBody MemberBasicForm memberSignInForm) {
        log.info("sign In()");

        return memberService.memberSignIn(memberSignInForm);
    }

    @DeleteMapping("/member-delete")
    public boolean MemberDelete(@RequestBody MemberBasicForm memberDeleteForm) {
        log.info("member delete()");

        return memberService.memberDelete(memberDeleteForm);
    }
}
