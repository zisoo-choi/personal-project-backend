package kh.project.demo.library.member.controller;

import kh.project.demo.library.member.controller.form.request.MemberBasicForm;
import kh.project.demo.library.member.controller.form.request.MemberSignUpForm;
import kh.project.demo.library.member.controller.form.request.MemberAccountStopForm;
import kh.project.demo.library.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/library-member")
public class MemberController {

    final private MemberService memberService;

    // 아이디 중복 확인
    @GetMapping("/check-id/{memberId}")
    public Boolean checkId(@PathVariable("memberId") String memberId) {
        log.info("check id duplication: "+ memberId);

        return memberService.checkIdDuplication(memberId);
    }

    // 이메일 중복 확인
    @GetMapping("/check-email/{email}")
    public Boolean checkEmail(@PathVariable("email") String email) {
        log.info("check email duplication: "+ email);

        return memberService.checkEmailDuplication(email);
    }

    // 회원 가입
    @PostMapping("/sign-up")
    public Boolean signUp (@RequestBody MemberSignUpForm memberSignUpForm) {
        log.info("sign Up()");

        return memberService.memberSignUp(memberSignUpForm);
    }

    // 회원 삭제
    @DeleteMapping("/member-delete")
    public boolean memberDelete(@RequestBody MemberBasicForm memberDeleteForm) {
        log.info("member delete()");

        return memberService.memberDelete(memberDeleteForm);
    }

    // 회원 계정 정지
    @PostMapping("/member-account-stop")
    public boolean memberAccountStop(@RequestBody MemberAccountStopForm memberAccountForm) {
        log.info("member account stop");

        return memberService.memberAccountStop(memberAccountForm);
    }

    // 회원 대출 한도 권 수
    @GetMapping("/limits-book")
    public Integer limitsBook(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("limitsBook()");

        if (userDetails != null) {
            String userId = userDetails.getUsername();
            // 로그인한 사용자의 정보를 활용한 로직 수행
            return memberService.inquiryLimitsBook(userId);
        }
        return null;
    }
}
