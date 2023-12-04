package kh.project.demo.domain.member.repository;

import kh.project.demo.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberId(String id);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByMemberNumber(Long registerManagerNumber);

}
