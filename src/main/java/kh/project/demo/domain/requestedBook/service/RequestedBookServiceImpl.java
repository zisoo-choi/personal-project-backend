package kh.project.demo.domain.requestedBook.service;

import kh.project.demo.domain.book.entity.Book;
import kh.project.demo.domain.book.repository.BookRepository;
import kh.project.demo.domain.member.entity.Member;
import kh.project.demo.domain.member.repository.MemberRepository;
import kh.project.demo.domain.requestedBook.controller.form.HopeBookForm;
import kh.project.demo.domain.requestedBook.entity.RequestedBook;
import kh.project.demo.domain.requestedBook.reposiroty.RequestedBookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestedBookServiceImpl implements RequestedBookService{

    final private MemberRepository memberRepository;
    final private BookRepository bookRepository;
    final private RequestedBookRepository requestedBookRepository;

    @Override
    public RequestedBook applicationBook(HopeBookForm requestForm, String userId) {
        Optional<Book> maybeBook = bookRepository.findByBookName(requestForm.getBookName());
        Optional<Member> maybeMember = memberRepository.findByMemberId(userId);

        // 1. 도서 이름이 같은 경우
        if(maybeBook.isPresent()) {
            // 도서 작가 확인
            Book book = maybeBook.get();
            if(book.getAuthor().equals(requestForm.getAuthor())){
                log.info("존재하는 도서이므로 희망 도서 신청이 불가능 합니다.");
                return null;
            }

            // 어떤 회원이 신청했는 지 확인
            if(maybeMember.isPresent()) {
                Member member = maybeMember.get();
                log.info("존재하지 않는 도서이므로 희망 도서 신청 되었습니다.");
                return requestedBookRepository.save(requestForm.toHopeBook(member));
            }
        }

        // -> 아래 코드를 안 넣어주면 경고가 있는 듯 함
        if(maybeMember.isEmpty()) {
            log.info("존재하지 않는 회원입니다.");
            return null;
        }

        // 2. 도서 이름이 다른 경우
        log.info("존재하지 않는 도서이므로 희망 도서 신청 되었습니다.");
        return requestedBookRepository.save(requestForm.toHopeBook(maybeMember.get()));
    }

    @Override
    public List<RequestedBook> hopeList() {
        return requestedBookRepository.findAll(Sort.by(Sort.Direction.DESC, "hopeBookNumber"));
    }

    @Override
    public RequestedBook read(Long bookNumber) {
        Optional<RequestedBook> maybeHopeBook = requestedBookRepository.findByHopeBookNumber(bookNumber);

        if(maybeHopeBook.isEmpty()){
            log.info("존재하지 않는 도서 입니다.");
            return null;
        }
        return maybeHopeBook.get();
    }

    @Override
    public List<RequestedBook> personalHopeList(String userId){
        return requestedBookRepository.findByMemberMemberId(userId);
    }

    @Override
    public Integer personalHopeAmount(String userId) {
        Optional<Member> maybeMember = memberRepository.findByMemberId(userId);

        if(maybeMember.isEmpty()){
            log.info("존재하지 않는 사용자 입니다.");
            return null;
        }

        Member member = maybeMember.get();

        List<RequestedBook> requestedBooks = requestedBookRepository.findByMember(member);
        return requestedBooks.size();
    }

    @Override
    public Integer managementHopeAmount(String userId) {
        List<RequestedBook> requestedBookList = requestedBookRepository.findAll();
        return requestedBookList.size();
    }
}
