package kh.project.demo.domain.member.service;

import kh.project.demo.domain.member.controller.form.request.MemberAccountStopForm;
import kh.project.demo.domain.member.controller.form.request.MemberBasicForm;
import kh.project.demo.domain.member.controller.form.request.MemberSignUpForm;
import kh.project.demo.domain.member.entity.Member;
import kh.project.demo.domain.member.entity.MemberRole;
import kh.project.demo.domain.member.entity.MemberState;
import kh.project.demo.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        }
        return true;
    }

    @Override
    public Boolean checkEmailDuplication(String email){
        Optional<Member> maybeMember = memberRepository.findByEmail(email);

        if (maybeMember.isPresent()) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean memberSignUp(MemberSignUpForm memberSignUpForm) {
        Optional<Member> maybeMemberId = memberRepository.findByMemberId(memberSignUpForm.getMemberId());
        if ( maybeMemberId.isPresent() ) {
            log.info("존재하는 회원 아이디입니다.");
            return false;
        }

        Optional<Member> maybeMemberEmail = memberRepository.findByEmail(memberSignUpForm.getEmail());
        if ( maybeMemberEmail.isPresent() ) {
            log.info("존재하는 회원 이메일 입니다.");
            return false;
        }

        // 비밀번호 암호화
        String encodePassword = new BCryptPasswordEncoder().encode(memberSignUpForm.getMemberPw());

        memberRepository.save(memberSignUpForm.toMember(encodePassword));
        return true;
    }

    @Override
    public boolean memberDelete(MemberBasicForm memberDeleteForm){
        final Optional<Member> mayMember = memberRepository.findByMemberId(memberDeleteForm.getMemberId());

        if (mayMember.isPresent()) {
            memberRepository.delete(mayMember.get());
            log.info("계정 삭제 성공");
            return true;
        }

        log.info("존재하지 않는 계정입니다.");
        return false;
    }

    @Override
    public boolean memberAccountStop(MemberAccountStopForm memberAccountForm){
        final Optional<Member> maybeNormal = memberRepository.findByMemberId(memberAccountForm.getMemberId());
        final Optional<Member> maybeManager = memberRepository.findByMemberId(memberAccountForm.getManagerId());

        if (maybeNormal.isEmpty()) {
            log.info("존재하지 않는 회원 계정입니다.");
            return false;
        }

        if (maybeManager.isEmpty()) {
            log.info("존재하지 않는 관리자 계정입니다.");
            return false;
        }

        Member normal = maybeNormal.get();
        Member manager = maybeManager.get();

        if (normal.getMemberState().equals(MemberState.OK)){
            if (manager.getMemberRole().equals(MemberRole.MANAGER)) {

                normal.setMemberState(MemberState.STOP);
                memberRepository.save(normal);
                log.info("계정이 정지 되었습니다.");
                return true;
            }
            log.info("관리자가 아닌 사용자는 계정을 정지할 수 없습니다.");
            return false;
        }

        log.info("계정 삭제 실패");
        return false;
    }

    @Override
    public Integer inquiryLimitsBook(String userId){
        Optional<Member> maybeMember = memberRepository.findByMemberId(userId);

        if(maybeMember.isEmpty()) {
            log.info("존재하지 않는 회원입니다.");
            return null;
        }
        Member member = maybeMember.get();
        return member.getAvailableAmount();
    }

    @Override
    public List<Member> list(){
        return memberRepository.findAll(Sort.by(Sort.Direction.DESC, "memberNumber"));
    }
}
