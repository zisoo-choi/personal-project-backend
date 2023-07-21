package kh.project.demo.library.libraryService.repository;

import kh.project.demo.library.libraryService.entity.HopeBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HopeBookRepository extends JpaRepository<HopeBook, Long> {
    Optional<HopeBook> findByHopeBookNumber(Long bookNumber);
}
