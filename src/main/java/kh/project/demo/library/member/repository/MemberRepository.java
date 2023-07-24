package kh.project.demo.library.member.repository;

import kh.project.demo.library.member.entity.Member;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberId(String id);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByMemberNumber(Long registerManagerNumber);

}
