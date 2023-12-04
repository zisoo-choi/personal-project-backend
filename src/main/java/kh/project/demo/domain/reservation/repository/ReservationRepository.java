package kh.project.demo.domain.reservation.repository;

import kh.project.demo.domain.book.entity.Book;
import kh.project.demo.domain.reservation.entity.Reservation;
import kh.project.demo.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

//    Optional<Reservation> findByBook(Book book);

    Optional<Reservation> findByBookAndMember(Book book, Member member);

    List<Reservation> findByMember(Member member);

    List<Reservation> findByBook(Book book);
}
