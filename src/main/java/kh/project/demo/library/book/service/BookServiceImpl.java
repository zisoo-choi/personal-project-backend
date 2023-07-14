package kh.project.demo.library.book.service;

import kh.project.demo.library.book.entity.Book;
import kh.project.demo.library.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService{

    final private BookRepository bookRepository;

    @Override
    public List<Book> registerationDateSort(){
        return null; // 코드 작성해야 함
    }
}
