package kh.project.demo.library.book.repository;

import kh.project.demo.library.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByBookName(String bookName);

    Optional<Book> findByBookNumber(Long bookNumber);

}
