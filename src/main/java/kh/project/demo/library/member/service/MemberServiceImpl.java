package kh.project.demo.library.member.service;

import kh.project.demo.library.member.controller.form.request.MemberSignInForm;
import kh.project.demo.library.member.controller.form.request.MemberSignUpForm;
import kh.project.demo.library.member.controller.form.response.MemberLoginRespnseForm;
import kh.project.demo.library.member.entity.Member;
import kh.project.demo.library.member.entity.MemberState;
import kh.project.demo.library.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    final private MemberRepository memberRepository;

    @Override
    public Boolean checkIdDuplication(String id){
        Optional<Member> maybeMember = memberRepository.findByMemberId(id);

        if (maybeMember.isPresent()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Boolean memberSignUp(MemberSignUpForm memberSignUpForm) {
        memberRepository.save(memberSignUpForm.toMember());
        return true;
    }

    @Override
    public MemberLoginRespnseForm memberSignIn(MemberSignInForm memberSignInForm){
        final Optional<Member> maybeMember = memberRepository.findByMemberId(memberSignInForm.getUserId());

        if (maybeMember.isEmpty()) {
            log.info("로그인 실패!");
            return new MemberLoginRespnseForm(null);
        }

        Member member = maybeMember.get();

        if (member.getMemberState().equals(MemberState.OK)) {
            if (member.getMemberPw().equals(memberSignInForm.getUserPw())) {
                log.info("로그인 성공!");
                return new MemberLoginRespnseForm(UUID.randomUUID());
            }
        } else {
            log.info("계정이 정지된 회원입니다.");
            return new MemberLoginRespnseForm(null);
        }

        log.info("로그인 실패!");
        return new MemberLoginRespnseForm(null);
    }

}
