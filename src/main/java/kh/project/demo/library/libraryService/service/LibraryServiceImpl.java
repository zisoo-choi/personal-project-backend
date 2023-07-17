package kh.project.demo.library.libraryService.service;

import kh.project.demo.library.book.entity.Book;
import kh.project.demo.library.book.entity.BookState;
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
        Optional<Member> maybeMember =
                memberRepository.findByMemberNumber(requestForm.getMemberNumber());
        Optional<Book> maybeBook =
                bookRepository.findByBookNumber(requestForm.getBookNumber());

        if(maybeMember.isEmpty() || maybeBook.isEmpty()) {
            log.info("대여 불가능 합니다. (회원 및 도서 다시 확인 요망)");
            return false;
        }

        // 회원과 도서 모두 존재
        Member member = maybeMember.get();
        Book book = maybeBook.get();
        if (member.getMemberServiceState() != null &&
                member.getMemberServiceState().equals(MemberServiceState.ServiceNormal)) {
            // 사용자가 빌릴 수 있는 상태라면
            // 사용자의 상태를 대출 중으로 만들고
            member.setMemberServiceState(MemberServiceState.ServiceRental);
            // 상태 저장 (변경된 값 저장과 같음)
            memberRepository.save(member);

            // 대여 서비스 저장
            Rental rental = requestForm.toRentalBook();
            rental.setBookState(BookState.BookRental);
            book.setBookState(BookState.BookRental);
            bookRepository.save(book);
            libraryRepository.save(rental);

            log.info("대출이 되었습니다.");
            return true;
        }

        return false;
    }
}
