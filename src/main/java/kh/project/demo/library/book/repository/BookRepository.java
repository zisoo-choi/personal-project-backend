package kh.project.demo.library.book.repository;

import kh.project.demo.library.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
