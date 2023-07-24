package kh.project.demo.library.libraryService.service;

import kh.project.demo.library.book.entity.Book;
import kh.project.demo.library.libraryService.controller.form.request.HopeBookForm;
import kh.project.demo.library.libraryService.entity.HopeBook;
import kh.project.demo.library.libraryService.entity.RentalState;
import kh.project.demo.library.book.repository.BookRepository;
import kh.project.demo.library.libraryService.controller.form.request.RentalBookForm;
import kh.project.demo.library.libraryService.entity.Rental;
import kh.project.demo.library.libraryService.repository.HopeBookRepository;
import kh.project.demo.library.libraryService.repository.LibraryRepository;
import kh.project.demo.library.libraryService.repository.RentalBookRepository;
import kh.project.demo.library.member.entity.Member;
import kh.project.demo.library.member.entity.MemberServiceState;
import kh.project.demo.library.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {

    final private LibraryRepository libraryRepository;
    final private MemberRepository memberRepository;
    final private BookRepository bookRepository;
    final private HopeBookRepository hopeBookRepository;
    final private RentalBookRepository rentalBookRepository;

    @Override
    public boolean rental(RentalBookForm requestForm, String userId) {
        Optional<Book> maybeBook = bookRepository.findByBookNumber(requestForm.getBookNumber());
        Optional<Member> maybeMember = memberRepository.findByMemberId(userId);

        if (maybeBook.isEmpty()) {
            log.info("존재하지 않는 도서 입니다.");
            return false;
        }

        Book book = maybeBook.get();
        Member member = maybeMember.get();

        if (book.getBookAmount() > 0) {
//            Rental rental = requestForm.toRentalBook(member.getMemberNumber());
            Rental rental = requestForm.toRentalBook(book, member);
            rental.setRentalState(RentalState.BookRental);
            member.setMemberServiceState(MemberServiceState.ServiceRental);

            book.minusAmount(); // 도서 대여 수량 1 감소
            member.minusAmount(); // 회원의 대여 수량 1 감소
            memberRepository.save(member);
            bookRepository.save(book);
            libraryRepository.save(rental);

            log.info("대출 되었습니다.");
            return true;
        }
        log.info("대여 수량이 0 입니다.");
        return false;
    }

    @Override
    public HopeBook applicationBook(HopeBookForm requestForm, String userId) {
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
                return hopeBookRepository.save(requestForm.toHopeBook(member));
            }
        }

        // -> 아래 코드를 안 넣어주면 경고가 있는 듯 함
        if(maybeMember.isEmpty()) {
            log.info("존재하지 않는 회원입니다.");
            return null;
        }

        // 2. 도서 이름이 다른 경우
        log.info("존재하지 않는 도서이므로 희망 도서 신청 되었습니다.");
        return hopeBookRepository.save(requestForm.toHopeBook(maybeMember.get()));
    }

    @Override
    public List<HopeBook> hopeList() {
        return hopeBookRepository.findAll(Sort.by(Sort.Direction.DESC, "hopeBookNumber"));
    }

    @Override
    public HopeBook read(Long bookNumber) {
        Optional<HopeBook> maybeHopeBook = hopeBookRepository.findByHopeBookNumber(bookNumber);

        if(maybeHopeBook.isEmpty()){
            log.info("존재하지 않는 도서 입니다.");
            return null;
        }
        return maybeHopeBook.get();
    }

    @Override
    public List<Rental> rentalList() {
        return rentalBookRepository.findAll(Sort.by(Sort.Direction.DESC, "rentalNumber"));
    }
}
