package kh.project.demo.library.libraryService.service;

import jakarta.transaction.Transactional;
import kh.project.demo.library.book.entity.Book;
import kh.project.demo.library.libraryService.controller.form.request.*;
import kh.project.demo.library.libraryService.entity.*;
import kh.project.demo.library.book.repository.BookRepository;
import kh.project.demo.library.libraryService.repository.HopeBookRepository;
import kh.project.demo.library.libraryService.repository.RentalBookRepository;
import kh.project.demo.library.libraryService.repository.ReservationRepository;
import kh.project.demo.library.member.entity.Member;
import kh.project.demo.library.member.entity.MemberServiceState;
import kh.project.demo.library.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {

    final private MemberRepository memberRepository;
    final private BookRepository bookRepository;
    final private HopeBookRepository hopeBookRepository;
    final private RentalBookRepository rentalBookRepository;
    final private ReservationRepository reservationRepository;

    @Override
    public boolean rental(RentalBookForm requestForm, String userId) {
        // 1. 회원과 도서가 존재하는 지 확인합니다.
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

        // 2. 존재하는 회원의 연체 기록 여부 검사
        if (member.getMemberServiceState().equals(MemberServiceState.ServiceOverdue)) {
            // 회원의 상태가 연체 상태와 같다면 대여 불가
            log.info("연체 기일이 존재하는 회원은 연체 기간 동안 대여 불가입니다.");
            return false;
        }

        // 3. 대여하려는 도서가 이미 대여된 도서인지 확인합니다.
        // 회원, 도서      => 과거 여러 권도 나오기 때문에 Optional 이 아닌 List 로 받아야 한다.
        // 회원, 도서, (반납완료)상태 => 이 또한 여러 번 빌렸다가 반납하면 같은 결과겠는데? 그러면 이것도 List 로 받아야 하고 ..

        // ※ 즉, 해당 도서를 여러 번 빌리고 볼 수 있으나, 현재 대여 중인 것을 중복 대여는 안되게 하고 싶은데
        // 그러면 현재 빌리려는 도서의 대여 상태가 "대여중"과 "연장" + "연체"상태만 아니면 빌릴 수 있다.

        Optional<Rental> existingRentals = rentalBookRepository.findByMemberAndBookAndRentalStateIn(member, book,
                Arrays.asList(RentalState.BookRental, RentalState.ServiceExtension, RentalState.BookDelinquency));

        if(existingRentals.isPresent()){
            log.info("현재 '대여', '연체' 상태이므로 대여가 불가능 합니다.");
            return false;
        }

        // 현재 여기는 대여 및 연체된 도서가 아닐 때

        // 회원의 대여 가능 수량이 0 이상이고, 도서 수량이 0 이상 일 때
        if (book.getBookAmount() > 0 && member.getAvailableAmount() > 0) {
            Rental rental = Rental.builder()
                    .member(member)
                    .book(book)
                    .rentalState(RentalState.BookRental)
                    .build();

            rental.setEstimatedRentalDate(rental.getRentalDate().plusDays(15)); // 대여 기간 15일(반납 예상 일자)
            member.setMemberServiceState(MemberServiceState.ServiceRental); // 회원 서비스 상태는 "대여 중"

            book.minusAmount(); // 도서 대여 수량 1 감소
            member.minusAmount(); // 회원의 대여 가능 수량 1 감소

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
        // 1. 해당 도서와 회원이 빌린 대여가 맞는 지 확인
        Optional<Rental> maybeRental = rentalBookRepository.findByRentalNumber(requestForm.getRentalNumber());
        Optional<Member> maybeMember = memberRepository.findByMemberId(userId);

        if(maybeMember.isEmpty()) {
            log.info("존재하지 않는 회원 입니다.");
            return false;
        }

        if(maybeRental.isEmpty()) {
            log.info("존재하지 않는 대여 기록 입니다.");
            return false;
        }

        Rental rental = maybeRental.get();
        Member member = maybeMember.get();

        // ★ 회원이 "대여 중" or "연장 중" 인 경우만 반납이 가능하다.
        if(rental.getRentalState().equals(RentalState.BookRental) || rental.getRentalState().equals(RentalState.BookDelinquency)) {

            // 2. 해당 도서가 "예약"되어 있는 도서이면 연장 불가능
            // (예약 인원이 한 명이 아닌 여러 명일 수도 있으니 List 로 받는다.)
            List<Reservation> maybeReservation = reservationRepository.findByBook(rental.getBook());

            if (!maybeReservation.isEmpty()) {
                log.info("예약 인원이 존재하므로 연장 불가 입니다.");
                return false;
            }

            //         // 3. 대여기록이 확인 된 회원은 대여 상태가 "연체" 상태이면 연장 불가능
            //        if(rental.getRentalState().equals(RentalState.BookDelinquency)) {
            //            log.info("연체 상태이므로 연장이 불가 합니다. 빠른 시일 내로 반납 해주세요");
            //            return false;
            //        }
            //
            //        // 3-1. (수정 버전) 회원의 서비스 상태가 "연체" 상태이면 연장 불가능
            //        if(member.getMemberServiceState().equals(MemberServiceState.ServiceOverdue)) {
            //            log.info("회원의 상태가 연체이므로 연장이 불가합니다. 빠른 시일 내로 반납 해주세요 !");
            //            return false;
            //        }

            // 3-2. (최종 수정 버전) 회원의 대여 기록들을 List 로 불러와서 연체 기록이 하나라도 존재하면 연체로 간주한다 !
            List<Rental> rentalList = rentalBookRepository.findAllByRentalNumber(requestForm.getRentalNumber());  // ★
            for (Rental rentalStateCheck : rentalList) {
                if (rentalStateCheck.getRentalState() == RentalState.BookDelinquency) {
                    // 하나라도 연체가 되었다면
                    if (!member.getMemberServiceState().equals(MemberServiceState.ServiceOverdue)) {
                        // 연체된 회원이 연체로 되어 있지 않다면 연체 상태로 업데이트 해줍니다.
                        member.setMemberServiceState(MemberServiceState.ServiceOverdue);
                        memberRepository.save(member);
                    }
                    log.info("연체된 도서가 존재하므로 연장이 불가합니다.");
                    return false;
                }
            }

            // 4. 대여기록이 확인 된 회원은 대여 상태가 "연장" 상태이면 연장 불가능
            if (rental.getRentalState().equals(RentalState.ServiceExtension)) {
                // 연장 상태인데 반납 기한을 넘긴 상태라면 연체로 만들어 준다.
                if (rental.getExtensionEstimatedDate().isBefore(LocalDateTime.now())) {
                    // 연장 후 반납 예정일이 "현재"보다 넘긴 후라면 연체 상태로 업데이트 해준다.
                    rental.setRentalState(RentalState.BookDelinquency);
                    member.setMemberServiceState(MemberServiceState.ServiceOverdue);
                    memberRepository.save(member);
                    rentalBookRepository.save(rental);
                    log.info("연장 후 반납 예정일이 지났습니다. 빠른 시일 내로 반납 해주세요");
                } else {
                    log.info("이미 연장한 회원은 재 연장이 불가 합니다.");
                }
                return false;
            }

            // 5. 대여기록이 확인 되었고, 대여 상태가 "연장"과 "연체"가 아닌 회원은 연장이 가능합니다.
            rental.setRentalState(RentalState.ServiceExtension); // 연장
            rental.setExtensionEstimatedDate(rental.getEstimatedRentalDate().plusDays(15));
            rentalBookRepository.save(rental);

            member.setMemberServiceState(MemberServiceState.ServiceExtension);
            memberRepository.save(member);

            log.info("도서 연장되었습니다!");
            return true;
        }
        log.info("도서를 대여하지 않은 회원입니다.");
        return false;
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

    @Override
    @Transactional
    public boolean returned(ReturnedBookForm requestForm, String userId) {

        Optional<Rental> maybeRental = rentalBookRepository.findByRentalNumber(requestForm.getRentalNumber());
        Optional<Member> maybeMember = memberRepository.findByMemberId(userId);

        if(maybeRental.isEmpty()){
            log.info("존재하지 않는 대여입니다.");
            return false;
        }

        if(maybeMember.isEmpty()){
            log.info("존재하지 않는 회원입니다.");
            return false;
        }

        Rental rental = maybeRental.get();
        Member member = maybeMember.get();

        Optional<Book> maybeBook = bookRepository.findByBookNumber(rental.getBook().getBookNumber());

        if(maybeBook.isEmpty()) {
            log.info("대여된 도서가 없습니다. 이거 로직이 이상합니다?");
            return false;
        }

        Book book = maybeBook.get();
        // 0. 회원이 "대여 중" or "연장 중" 인 경우만 반납이 가능하다.
        if(rental.getRentalState().equals(RentalState.BookRental) || rental.getRentalState().equals(RentalState.ServiceExtension)) {
            // 1. 연장을 한 회원인지 확인하기
            if(rental.getExtensionEstimatedDate() != null) {
                // 연장한 회원

                // 1-1. 연장 후 대여 상태가 연체 라면 연체일자를 업데이트 해주고, 반납 해줍니다.
                // (해당 대여에 대한 연체 일자만 업데이트 해주고 그 기간 동안 대여 정지를 주면 됨)
                if (rental.getExtensionEstimatedDate().isBefore(LocalDateTime.now())) {
                    // 연체 상태거나 연장 후 반납 예정일이 현재보다 지났다면
                    rental.setRentalState(RentalState.BookDelinquency);
                    member.setMemberServiceState(MemberServiceState.ServiceOverdue);

                    // 연장 후 반납 예정일 - 현재 날자 = 대여 금지 일자
                    // 23.07.25 - 23.07.28 = 3일 ((=> 23.07.31))

                    int delinquencyDays =  Period.between(rental.getExtensionEstimatedDate().toLocalDate(), LocalDate.now()).getDays();
                    LocalDateTime overdueDateTime = LocalDateTime.now().plusDays(delinquencyDays);

                    rental.setOverdueDate(overdueDateTime); // 연체 일자 업데이트
                    rental.setRentalState(RentalState.ServiceReturn);
                    rental.setReturnDate(LocalDateTime.now());

                    book.plusAmount(); // 도서 대여 수량 1 증가
                    member.plusAmount(); // 회원의 대여 가능 수량 1 증가
                    member.setMemberServiceState(MemberServiceState.ServiceOverdue);

                    rentalBookRepository.save(rental);
                    memberRepository.save(member);
                    bookRepository.save(book);

                    log.info("연장 후 연체 회원의 반납이 완료되었습니다.");
                    return true;
                } else {
                    // 1-2. 연장 후 연체가 아닌 상황이라면 반납 해줍니다.
                    member.setMemberServiceState(MemberServiceState.ServiceNormal);
                    rental.setRentalState(RentalState.ServiceReturn);
                    rental.setReturnDate(LocalDateTime.now());

                    book.plusAmount(); // 도서 대여 수량 1 증가
                    member.plusAmount(); // 회원의 대여 가능 수량 1 증가

                    rentalBookRepository.save(rental);
                    memberRepository.save(member);
                    bookRepository.save(book);

                    log.info("연장 후 반납이 완료되었습니다.");
                    return true;
                }
            }
            ///--> 연장 임


            ///--> 연장 아님
            // 2. 대여 상태가 연체가 아니라면 현재 연체 상황인지 확인합니다.

            // 2-1. 연체 상황이라면 연체 일자를 계산해서 연체 일자를 업데이트 해주고 반납합니다.
            if(rental.getEstimatedRentalDate().isBefore(LocalDateTime.now())) {
                // 예상 반납일이 지났다면 연체이다.
                rental.setRentalState(RentalState.BookDelinquency);
                member.setMemberServiceState(MemberServiceState.ServiceOverdue);

                // 연장 후 반납 예정일 - 현재 날자 = 대여 금지 일자
                // 23.07.25 - 23.07.28 = 3일 ((=> 23.07.31))

                int delinquencyDays =  Period.between(rental.getEstimatedRentalDate().toLocalDate(), LocalDate.now()).getDays();
                LocalDateTime overdueDateTime = LocalDateTime.now().plusDays(delinquencyDays);

                rental.setOverdueDate(overdueDateTime); // 연체 일자 업데이트
                rental.setRentalState(RentalState.ServiceReturn);
                rental.setReturnDate(LocalDateTime.now());

                book.plusAmount(); // 도서 대여 수량 1 증가
                member.plusAmount(); // 회원의 대여 가능 수량 1 증가
                member.setMemberServiceState(MemberServiceState.ServiceOverdue);

                rentalBookRepository.save(rental);
                memberRepository.save(member);
                bookRepository.save(book);

                log.info("연장 후 연체 회원의 반납이 완료되었습니다.");
                return true;
            } else {
                // 2-2. 연체 상황이 아니라면 반납합니다.
                member.setMemberServiceState(MemberServiceState.ServiceNormal);
                rental.setRentalState(RentalState.ServiceReturn);
                rental.setReturnDate(LocalDateTime.now());

                book.plusAmount(); // 도서 대여 수량 1 증가
                member.plusAmount(); // 회원의 대여 가능 수량 1 증가
                log.info(String.valueOf(rental.getRentalState()));
                rentalBookRepository.save(rental);
                memberRepository.save(member);
                bookRepository.save(book);
                log.info(String.valueOf(rental.getRentalState()));
                log.info(String.valueOf(rental.getRentalNumber()));
                log.info("반납이 완료되었습니다.");
                return true;
            }
        }

        log.info("해당 도서를 대여하지 않은 회원입니다.");
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

    @Override
    public List<HopeBook> personalHopeList(String userId){
        return hopeBookRepository.findByMemberMemberId(userId);
    }
}
