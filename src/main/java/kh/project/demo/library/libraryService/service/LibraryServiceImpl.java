package kh.project.demo.library.libraryService.service;

import kh.project.demo.library.book.entity.Book;
import kh.project.demo.library.libraryService.entity.RentalState;
import kh.project.demo.library.book.repository.BookRepository;
import kh.project.demo.library.libraryService.controller.form.request.RentalBookForm;
import kh.project.demo.library.libraryService.entity.Rental;
import kh.project.demo.library.libraryService.repository.LibraryRepository;
import kh.project.demo.library.member.entity.Member;
import kh.project.demo.library.member.entity.MemberServiceState;
import kh.project.demo.library.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {

    final private LibraryRepository libraryRepository;
    final private MemberRepository memberRepository;
    final private BookRepository bookRepository;

    @Override
    public boolean rental(RentalBookForm requestForm) {
        Optional<Book> maybeBook = bookRepository.findByBookNumber(requestForm.getBookNumber());
        Optional<Member> maybeMember = memberRepository.findByMemberNumber(requestForm.getMemberNumber());

        if(maybeBook.isEmpty()) {
            log.info("존재하지 않는 도서 입니다.");
            return false;
        }

        Book book = maybeBook.get();
        Member member = maybeMember.get();

        if(book.getBookAmount() != 0) {
            Rental rental = requestForm.toRentalBook();
            rental.setRentalState(RentalState.BookRental);
            member.setMemberServiceState(MemberServiceState.ServiceRental);

            book.minusAmount(); // 대여 수량 1 빼기
            memberRepository.save(member);
            bookRepository.save(book);
            libraryRepository.save(rental);

            log.info("대출 되었습니다.");
            return true;
        }
        log.info("대여 수량이 0 입니다.");
        return false;
    }
}
