package kh.project.demo.library.libraryService.repository;

import kh.project.demo.library.book.entity.Book;
import kh.project.demo.library.libraryService.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findByBook(Book book);
}
