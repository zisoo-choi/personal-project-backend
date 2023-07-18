package kh.project.demo.library.book.service;

import kh.project.demo.library.book.controller.form.request.RegisterBookForm;
import kh.project.demo.library.book.controller.form.request.ModifyBookForm;
import kh.project.demo.library.book.entity.Book;
import kh.project.demo.library.book.entity.KoreanDecimalClassification;
import kh.project.demo.library.book.repository.BookRepository;
import kh.project.demo.library.member.entity.Member;
import kh.project.demo.library.member.entity.MemberRole;
import kh.project.demo.library.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService{

    final private BookRepository bookRepository;
    final private MemberRepository memberRepository;

    // 도서 등록
    @Override
    public Book register(RegisterBookForm requestForm) {
        Optional<Book> maybeBook = bookRepository.findByBookName(requestForm.getBookName());
        // 존재하지 않는 도서라면 도서 등록
        if (maybeBook.isEmpty()) {
            Book book = requestForm.toRegisterBook();
            log.info("도서 등록이 완료 되었습니다.");
            return bookRepository.save(book);
        }
        return null;
    }

    // 도서 수정
    @Override
    public Book modify(Long bookNumber, ModifyBookForm modifyBookForm){
        // -> 예외 처리로 정리

        // 회원 역할 조회
        Member member = memberRepository.findByMemberNumber(modifyBookForm.getManagerNumber()).orElseThrow(() -> {
            throw new RuntimeException("존재 하지 않는 사용자");
        });

        if (member.getMemberRole().equals(MemberRole.NORMAL)) {
            throw new RuntimeException("권한이 없는 사용자");
        }

        Book book = bookRepository.findByBookNumber(bookNumber).orElseThrow(() -> {
            throw new RuntimeException("존재 하지 않는 사용자");
        });

        // 수정 부분
        book.setBookAmount(modifyBookForm.getBookAmount());
        book.setContent(modifyBookForm.getContent());
        modifyBookForm.setUpdateDate(LocalDateTime.now());

        log.info("도서 수정 완료 !");
        return bookRepository.save(book);
    }

    // 도서 삭제
    @Override
    public boolean delete(Long bookNumber){
        bookRepository.deleteById(bookNumber);
        return true;
    }

    // 도서 상세 페이지 읽기
    @Override
    public Book read(Long bookNumber) {
        Optional<Book> maybeBook = bookRepository.findByBookNumber(bookNumber);

        if(maybeBook.isEmpty()) {
            log.info("존재하지 않는 도서 입니다.");
            return null;
        }
        return maybeBook.get();
    }

    // 신간 도서 리스트
    @Override
    public List<Book> registerationDateSort(){
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "registrationDate");
        Page<Book> bookPage = bookRepository.findAll(pageable);
        return bookPage.getContent();
    }

    // 목록 별 리스트
    @Override
    public List<Book> listByfield(KoreanDecimalClassification categorizationSymbol) {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "categorizationSymbol");
        Page<Book> bookPage = bookRepository.findByCategorizationSymbol(categorizationSymbol, pageable);
        return bookPage.getContent();
    }
}
