package kh.project.demo.library.member.service;

import kh.project.demo.library.member.entity.Member;
import kh.project.demo.library.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        } else {
            return true;
        }
    }

}
