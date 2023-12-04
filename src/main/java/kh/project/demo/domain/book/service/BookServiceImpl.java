package kh.project.demo.domain.book.service;

import kh.project.demo.domain.book.controller.form.request.RegisterBookForm;
import kh.project.demo.domain.book.controller.form.request.ModifyBookForm;
import kh.project.demo.domain.book.entity.Book;
import kh.project.demo.domain.book.entity.KoreanDecimalClassification;
import kh.project.demo.domain.book.repository.BookRepository;
import kh.project.demo.domain.member.entity.Member;
import kh.project.demo.domain.member.entity.MemberRole;
import kh.project.demo.domain.member.repository.MemberRepository;
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
    public Book register(RegisterBookForm requestForm, String userId) {
        Optional<Member> maybeMember = memberRepository.findByMemberId(userId);
        Optional<Book> maybeBook = bookRepository.findByBookName(requestForm.getBookName());
        // 존재하지 않는 도서라면 도서 등록
        if (maybeBook.isEmpty()) {
            if (maybeMember.isPresent()){
                Member member = maybeMember.get();
                if(member.getMemberRole().equals(MemberRole.NORMAL)){
                    log.info("권한이 없는 사용자는 도서 등록이 불가합니다.");
                    return null;
                }
                Book book = requestForm.toRegisterBook(member);
                book.setManager(member);
                log.info("도서 등록이 완료 되었습니다.");
                return bookRepository.save(book);
            }
        }
        return null;
    }

    // 도서 수정
    @Override
    public Book modify(Long bookNumber, ModifyBookForm modifyBookForm, String userId){
        Optional<Member> maybeMember = memberRepository.findByMemberId(userId);
        Optional<Book> maybeBook = bookRepository.findByBookNumber(bookNumber);
        if(maybeBook.isEmpty()) {
            return null;
        }

        if(maybeBook.isPresent()) {
            Member member = maybeMember.get();
            if(member.getMemberRole().equals(MemberRole.NORMAL)) {
                log.info("권한이 없는 사용자는 도서 수정이 불가합니다.");
                return null;
            }
            Book book = maybeBook.get();

            // 수정 부분
            book.setBookAmount(modifyBookForm.getBookAmount());
            book.setContent(modifyBookForm.getContent());
            modifyBookForm.setUpdateDate(LocalDateTime.now());

            log.info("도서 수정 완료 !");
            return bookRepository.save(book);
        }
        return null;
    }

     //도서 삭제
    @Override
    public boolean delete(Long bookNumber){
        bookRepository.deleteById(bookNumber);
        return true;
    }

    // 도서 삭제 (수량 받는)
//    @Override
//    public boolean delete(Long bookNumber, int deleteCount) {
//        Optional<Book> maybeBook = bookRepository.findByBookNumber(bookNumber);
//
//        if (maybeBook.isPresent()) {
//            Book book = maybeBook.get();
//
//            // 기존 도서의 수량과 삭제하려는 권수를 비교하여 삭제 가능한 권수를 계산합니다.
//            int availableDeleteCount = Math.min(book.getBookAmount(), deleteCount);
//
//            if (availableDeleteCount > 0) {
//                // 삭제 가능한 권수만큼 도서를 삭제합니다.
//                book.setBookAmount(book.getBookAmount() - availableDeleteCount);
//                bookRepository.save(book);
//
//                // 만약 도서 수량이 0이 되었을 경우에는 도서를 데이터베이스에서 완전히 삭제합니다.
//                if (book.getBookAmount() == 0) {
//                    bookRepository.delete(book);
//                }
//
//                return true;
//            }
//        }
//        return false;
//    }


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

    // 전체 도서 목록
    @Override
    public List<Book> list(){
        return bookRepository.findAll(Sort.by(Sort.Direction.DESC, "bookNumber"));
    }
}
