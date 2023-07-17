package kh.project.demo.library.book.service;

import kh.project.demo.library.book.controller.form.request.RegisterBookForm;
import kh.project.demo.library.book.controller.form.request.RequestBookBoardForm;
import kh.project.demo.library.book.entity.Book;
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
        // 등록하는 사람의 역할 검증
        Optional<Member> mayMember = memberRepository.findByMemberNumber(requestForm.getRegisterManagerNumber());

        if (mayMember.isPresent()) {
            Member member = mayMember.get();
            if (member.getMemberRole().equals(MemberRole.NORMAL)) {
                log.info("관리자가 아니므로 권한이 없습니다.");
                return null;
            }
        }

        // 도서 존재 여부 확인 후, 존재 시 수량 증가 (현재 원하는 대로 구동 안되고 있음)
        Optional<Book> maybeBook = bookRepository.findByBookName(requestForm.getBookName());

        if (maybeBook.isPresent()) {
            // 받아온 도서의 새 객체를 만들어 줌
            Book book = maybeBook.get();

            // 기존 책과 동일한 책인 지 비교하기
            if (book.getBookName().equals(maybeBook.get().getBookName())
                    && book.getAuthor().equals(maybeBook.get().getAuthor())
            ) {
                book.bookAddAmount();
                bookRepository.save(book); // 수정된 도서 정보 저장
                log.info("존재하는 도서 이므로 수량이 증가합니다.");
                return null;
            }
        }

        // 존재하지 않는 도서라면
        if (maybeBook.isEmpty()) {
            // 저장해준다.
            log.info("도서 등록이 완료 되었습니다.");
            return bookRepository.save(requestForm.registerBook());
        }
        return null;
    }

    // 도서 수정
    @Override
    public Book modify(Long bookNumber, RequestBookBoardForm requestBookBoardForm){
        // 회원 역할 조회
        Optional<Member> maybeMember = memberRepository.findByMemberNumber(requestBookBoardForm.getManagerNumber());

        if(maybeMember.isPresent()) {
            Member member = maybeMember.get();
            if(member.getMemberRole().equals(MemberRole.NORMAL)) {
                log.info("권한이 없는 사용자 입니다.");
                return null;
            }
        }

        // 도서 존재 여부 확인
        Optional<Book> maybeBook = bookRepository.findByBookNumber(bookNumber);

        if(maybeBook.isEmpty()) {
            log.info("해당 도서 정보가 없습니다.");
            return null;
        }

        if(maybeBook.isPresent()) {
            // 수정 부분
            Book book = maybeBook.get();
            book.setBookAmount(requestBookBoardForm.getBookAmount());
            book.setContent(requestBookBoardForm.getContent());
            requestBookBoardForm.setUpdateDate(LocalDateTime.now());
            log.info("도서 수정 완료 !");
            return bookRepository.save(book);
        }
        log.info("해당 도서 정보가 없습니다.");
        return null;
    }

    // 도서 삭제
    @Override
    public boolean delete(Long bookNumber){
        bookRepository.deleteById(bookNumber);
        return true;
    }

    // 신간 도서 리스트
    @Override
    public List<Book> registerationDateSort(){
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "registrationDate");
        Page<Book> bookPage = bookRepository.findAll(pageable);
        return bookPage.getContent();
    }
}
