package kh.project.demo.library.libraryService.repository;

import kh.project.demo.library.libraryService.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Rental, Long> {

}
