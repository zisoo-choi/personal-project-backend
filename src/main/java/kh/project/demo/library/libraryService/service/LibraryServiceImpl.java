package kh.project.demo.library.libraryService.service;

import kh.project.demo.library.book.entity.Book;
import kh.project.demo.library.libraryService.controller.form.request.ExtensionBookForm;
import kh.project.demo.library.libraryService.controller.form.request.HopeBookForm;
import kh.project.demo.library.libraryService.controller.form.request.ReservationBookForm;
import kh.project.demo.library.libraryService.entity.*;
import kh.project.demo.library.book.repository.BookRepository;
import kh.project.demo.library.libraryService.controller.form.request.RentalBookForm;
import kh.project.demo.library.libraryService.repository.HopeBookRepository;
import kh.project.demo.library.libraryService.repository.LibraryRepository;
import kh.project.demo.library.libraryService.repository.RentalBookRepository;
import kh.project.demo.library.libraryService.repository.ReservationRepository;
import kh.project.demo.library.member.entity.Member;
import kh.project.demo.library.member.entity.MemberServiceState;
import kh.project.demo.library.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    final private ReservationRepository reservationRepository;

    @Override
    public boolean rental(RentalBookForm requestForm, String userId) {
        Optional<Book> maybeBook = bookRepository.findByBookNumber(requestForm.getBookNumber());
        Optional<Member> maybeMember = memberRepository.findByMemberId(userId);

        if (maybeBook.isEmpty()) {
            log.info("존재하지 않는 도서입니다.");
            return false;
        }

        if (maybeMember.isEmpty()) {
            log.info("존재하지 않는 회원입니다.");
            return false;
        }

        Book book = maybeBook.get();
        Member member = maybeMember.get();

        // 먼저 연체 기록이 존재하지 않는 지 확인해야 합니다.
        if (member.getMemberServiceState().equals(MemberServiceState.ServiceOverdue)) {
            log.info("연체 기일이 존재하는 회원은 연체 기간 동안 대여 불가입니다.");
            return false;
        }

        // 이미 대여된 도서인지 확인합니다.
        List<Rental> existingRentals = rentalBookRepository.findByMemberAndBookAndRentalState(member, book, RentalState.BookRental);
        if (!existingRentals.isEmpty()) {
            log.info("이미 대여된 도서입니다.");
            return false;
        }

        if (book.getBookAmount() > 0 && member.getAvailableAmount() > 0) {
            Rental rental = Rental.builder()
                    .member(member)
                    .book(book)
                    .rentalState(RentalState.BookRental)
                    .build();

            rental.setEstimatedRentalDate(rental.getRentalDate().plusDays(15));

            member.setMemberServiceState(MemberServiceState.ServiceRental);

            book.minusAmount(); // 도서 대여 수량 1 감소
            member.minusAmount(); // 회원의 대여 수량 1 감소

            memberRepository.save(member);
            bookRepository.save(book);
            rentalBookRepository.save(rental);

            log.info("대출되었습니다.");
            return true;
        }

        log.info("대여 수량이 0입니다.");
        return false;
    }

    @Override
    public boolean extension(ExtensionBookForm requestForm, String userId) {
        Optional<Book> maybeBook = bookRepository.findByBookNumber(requestForm.getBookNumber());
        Optional<Member> maybeMember = memberRepository.findByMemberId(userId);

        if (maybeBook.isEmpty()) {
            log.info("도서가 존재하지 않습니다.");
            return false;
        }

        if (maybeMember.isEmpty()) {
            log.info("회원이 존재하지 않습니다.");
            return false;
        }

        Book book = maybeBook.get();
        Member member = maybeMember.get();

        // 예약자가 존재하지 않는지 확인
        Optional<Reservation> maybeReservation = reservationRepository.findByBook(book);

        if (maybeReservation.isPresent()) {
            log.info("예약자가 존재하므로 도서 연장이 불가합니다.");
            return false;
        }

        // 회원이 이미 대여한 도서인 지 확인한다. (대여해야만 연장 가능)
        Optional<Rental> maybeRental = rentalBookRepository.findByMemberAndBook(member, book);

        if (maybeRental.isEmpty()) {
            log.info("존재하지 않는 회원과 도서입니다.");
            return false;
        }

        Rental rental = maybeRental.get();
        LocalDateTime now = LocalDateTime.now();

        if (rental.getEstimatedRentalDate() != null && rental.getEstimatedRentalDate().isBefore(now)) {
            // 반납 예정일이 현재 날짜보다 이전이면, 반납 예정일이 지난 것입니다.
            rental.setOverdueDate(now); // 연체일자를 현재 날짜로 설정
            rental.setRentalState(RentalState.BookDelinquency);
            member.setMemberServiceState(MemberServiceState.ServiceOverdue);

            memberRepository.save(member);
            bookRepository.save(book);
            rentalBookRepository.save(rental);

            log.info("연체된 회원입니다.");
            return false;
        }

        // 예약자가 존재하지 않고, 연체 일자가 존재하지 않는다면 연장이 가능합니다.
        if (rental.getEstimatedRentalDate() != null) {
            rental.setExtensionDate(rental.getEstimatedRentalDate().plusDays(7)); // 마감 반납 날짜 + 7일 연장한다 !
            rentalBookRepository.save(rental);

            member.setMemberServiceState(MemberServiceState.ServiceExtension); // 회원 연장했다 ! (-> 이거 좀 이상하다.)
            memberRepository.save(member);
            return true;
        } else {
            log.info("반납 예정일이 없습니다."); // 혹시라도 예상 반납일이 null이면 에러 메시지 출력
            return false;
        }
    }

    @Override
    public boolean reservation(ReservationBookForm requestForm, String userId) {
        Optional<Book> maybeBook = bookRepository.findByBookNumber(requestForm.getBookNumber());
        Optional<Member> maybeMember = memberRepository.findByMemberId(userId);

        if (maybeBook.isEmpty()) {
            log.info("도서가 존재하지 않습니다.");
            return false;
        }

        if (maybeMember.isEmpty()) {
            log.info("회원이 존재하지 않습니다.");
            return false;
        }

        Book book = maybeBook.get();
        Member member = maybeMember.get();

        // 해당 도서와 회원이 기존에 대여를 한 도서라면 예약이 불가능 합니다.
        Optional<Rental> maybeRental = rentalBookRepository.findByMemberAndBook(member, book);

        if(maybeRental.isPresent()) {
            log.info("해당 도서를 대여한 회원은 예약할 수 없습니다.");
            return false;
        }

        // 해당 도서와 회원이 기존에 예약을 했다면 중복 예약이 불가능 합니다.
        Optional<Reservation> maybeReservation = reservationRepository.findByBookAndMember(book, member);

        if(maybeReservation.isPresent()){
            log.info("중복 예약은 불가능 합니다.");
            return false;
        }

        // 중복된 예약이 아니라면 예약 생성
        Reservation reservation = Reservation.builder()
                .member(member)
                .book(book)
                .reservationState(ReservationState.Await) // 대기 (예약 후, 대출 전)
                .build();

        reservationRepository.save(reservation);
        return true;
    }

    @Override
    public List<Reservation> reservationList(){
        return reservationRepository.findAll(Sort.by(Sort.Direction.DESC, "reservationNumber"));
    }

//    @Override
//    public boolean returned(ReturnedBookForm requestForm, String userId) {
//        Optional<Book> maybeBook = bookRepository.findByBookNumber(requestForm.getBookNumber());
//        Optional<Member> maybeMember = memberRepository.findByMemberId(userId);
//
//        if(maybeBook.isPresent() && maybeMember.isPresent()){
//            Member member = maybeMember.get();
//            Book book = maybeBook.get();
//
//            // 대여 기록 조회
//            Rental rental = rentalBookRepository.findByMemberAndBook(member, book);
//
//            if(rental.getRentalState().equals(RentalState.BookRental)){
//
//                // 사용자가 연체 인지 아닌 지 여부 확인
//
//
//                // 연체 아니고 대여 상태라면
//                rental.setRentalState(RentalState.BookBefore); // 대출 전 상태로 만들어주기(=사실상 반납)
//                book.plusAmount(); // 도서 대여 수량 1 증가
//
//                // 사용자가 연체가 아니라면 사용자의 대여 수량 1 증가
//
//            }
//        }
//
//        // 반납을 해보자고
//        // 1. memberNumber 와 bookNumber 모두 일치하면 반납 완료
//        // 1 - 1) 일치해서 반납 (정상)
//        // 1 - 2) 일치하나 연체된 반납 (비정상)
//
//
//        return false;
//    }

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

    @Override
    public List<Rental> personalRentalList(String userId) {
        Optional<Member> maybeMember = memberRepository.findByMemberId(userId);

        if(maybeMember.isEmpty()){
            log.info("존재하지 않는 사용자 입니다.");
            return null;
        }

        Member member = maybeMember.get();

        // 해당 사용자의 Rental 기록들을 반환해줘야 함
        List<Rental> rentalList = rentalBookRepository.findByMember(member);

        return rentalList;
    }

    @Override
    public List<Reservation> personalReservationList(String userId) {
        Optional<Member> maybeMember = memberRepository.findByMemberId(userId);

        if(maybeMember.isEmpty()){
            log.info("존재하지 않는 사용자 입니다.");
            return null;
        }
        Member member = maybeMember.get();

        List<Reservation> reservationList = reservationRepository.findByMember(member);
        return reservationList;
    }
}
