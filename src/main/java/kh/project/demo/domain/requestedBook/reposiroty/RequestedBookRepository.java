package kh.project.demo.domain.requestedBook.reposiroty;

import kh.project.demo.domain.requestedBook.entity.RequestedBook;
import kh.project.demo.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RequestedBookRepository extends JpaRepository<RequestedBook, Long> {
    Optional<RequestedBook> findByHopeBookNumber(Long bookNumber);

    List<RequestedBook> findByMemberMemberId(String userId);

    List<RequestedBook> findByMember(Member member);
}
