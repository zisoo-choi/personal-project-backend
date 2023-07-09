package kh.project.demo.library.member.controller;

import kh.project.demo.library.member.controller.form.MemberIdCheckForm;
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
}
