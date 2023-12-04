package kh.project.demo.domain.rental.repository;

import kh.project.demo.domain.book.entity.Book;
import kh.project.demo.domain.rental.entity.Rental;
import kh.project.demo.domain.rental.entity.RentalState;
import kh.project.demo.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByMember(Member member);

    Optional<Rental> findByMemberAndBookAndRentalState(Member member, Book book, RentalState rentalState);

    Optional<Rental> findByMemberAndBookAndRentalStateIn(Member member, Book book, List<RentalState> asList);

    Optional<Rental> findByRentalNumber(Long rentalNumber);

    List<Rental> findAllByRentalNumber(Long rentalNumber);
}
