package kh.project.demo.library.libraryService.repository;

import kh.project.demo.library.libraryService.entity.HopeBook;
import kh.project.demo.library.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HopeBookRepository extends JpaRepository<HopeBook, Long> {
    Optional<HopeBook> findByHopeBookNumber(Long bookNumber);

    List<HopeBook> findByMemberMemberId(String userId);

    List<HopeBook> findByMember(Member member);
}
