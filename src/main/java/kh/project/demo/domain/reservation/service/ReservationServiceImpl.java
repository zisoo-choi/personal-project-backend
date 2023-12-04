package kh.project.demo.domain.reservation.service;

import kh.project.demo.domain.book.entity.Book;
import kh.project.demo.domain.book.repository.BookRepository;
import kh.project.demo.domain.member.entity.Member;
import kh.project.demo.domain.member.repository.MemberRepository;
import kh.project.demo.domain.rental.entity.Rental;
import kh.project.demo.domain.rental.entity.RentalState;
import kh.project.demo.domain.rental.repository.RentalRepository;
import kh.project.demo.domain.reservation.controller.form.ReservationForm;
import kh.project.demo.domain.reservation.entity.Reservation;
import kh.project.demo.domain.reservation.entity.ReservationState;
import kh.project.demo.domain.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService{

    final private MemberRepository memberRepository;
    final private BookRepository bookRepository;
    final private ReservationRepository reservationRepository;
    final private RentalRepository rentalRepository;

    @Override
    public boolean reservation(ReservationForm requestForm, String userId) {
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
        Optional<Rental> maybeRental = rentalRepository.findByMemberAndBookAndRentalState(member, book, RentalState.BookRental);

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
    public Integer personalReservationAmount(String userId) {
        Optional<Member> maybeMember = memberRepository.findByMemberId(userId);

        if(maybeMember.isEmpty()){
            log.info("존재하지 않는 사용자 입니다.");
            return null;
        }

        Member member = maybeMember.get();

        List<Reservation> reservationsBooks = reservationRepository.findByMember(member);
        return reservationsBooks.size();
    }

    @Override
    public Integer managementReservationAmount(String userId) {
        List<Reservation> reservationList = reservationRepository.findAll();
        return reservationList.size();
    }
}
