package kh.project.demo.domain.book.repository;

import kh.project.demo.domain.book.entity.Book;
import kh.project.demo.domain.book.entity.KoreanDecimalClassification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByBookName(String bookName);

    Page<Book> findByCategorizationSymbol(KoreanDecimalClassification categorizationSymbol, Pageable pageable);

    Optional<Book> findByBookNumber(Long bookNumber);


}
