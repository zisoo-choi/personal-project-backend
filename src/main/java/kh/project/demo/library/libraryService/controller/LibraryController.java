package kh.project.demo.library.libraryService.controller;

import kh.project.demo.library.libraryService.controller.form.request.*;
import kh.project.demo.library.libraryService.entity.HopeBook;
import kh.project.demo.library.libraryService.entity.Rental;
import kh.project.demo.library.libraryService.entity.Reservation;
import kh.project.demo.library.libraryService.service.LibraryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/library-service")
public class LibraryController {

    final private LibraryService libraryService;

    // 도서 대여
    @PostMapping("/rental")
    public boolean bookRental(
            @RequestBody RentalBookForm requestForm,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("도서 대여");

        if (userDetails != null) {
            String userId = userDetails.getUsername();
            // 로그인한 사용자의 정보를 활용한 로직 수행
            return libraryService.rental(requestForm, userId);
        }
        return false;
    }

    // 도서 연장
    @PostMapping("/extension")
    public boolean bookExtension(
            @RequestBody ExtensionBookForm requestForm,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("도서 연장");

        if(userDetails != null) {
            String userId = userDetails.getUsername();
            return libraryService.extension(requestForm, userId);
        }
        return false;
    }

    // 도서 예약
    @PostMapping("/reservation")
    public boolean bookReservation(
            @RequestBody ReservationBookForm requestForm,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("도서 예약");

        if(userDetails != null) {
            String userId = userDetails.getUsername();
            return libraryService.reservation(requestForm, userId);
        }
        return false;
    }

    // 도서 예약 목록 조회
    @GetMapping("/reservation-list")
    public List<Reservation> reservationList() {
        log.info("도서 예약 목록 요청!");

        List<Reservation> returnedReservationBookList = libraryService.reservationList();
        return returnedReservationBookList;
    }

    // 도서 예약 개인 목록 조회
    @GetMapping("/personal-reservation-list")
    public List<Reservation> personalReservationList(
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("personal reservation list()");

        if(userDetails != null) {
            String userId = userDetails.getUsername();
            return libraryService.personalReservationList(userId);
        }
        return null;
    }

    // 도서 반납
    @PostMapping("/return")
    public boolean bookReturn(
            @RequestBody ReturnedBookForm requestForm,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("도서 반납");

        if (userDetails != null){
            String userId = userDetails.getUsername();
            return libraryService.returned(requestForm, userId);
        }

        return false;
    }

    // 대여 도서 리스트
    @GetMapping("/rental-book-list")
    public List<Rental> rentalBook() {
        log.info("대여 목록 요청!");

        List<Rental> returnedRentalBookList = libraryService.rentalList();
        return returnedRentalBookList;
    }

    // 개인 사용자의 도서 리스트
    @GetMapping("personal-rent-list")
    public List<Rental> personalRentList(
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("개인 대여 도서 조회");

        if(userDetails != null) {
            String userId = userDetails.getUsername();
            return libraryService.personalRentalList(userId);
        }
        return null;
    }

    // 사용자 희망 도서 신청
    @PostMapping("/hope-book")
    public HopeBook HopeBook(
            @RequestBody HopeBookForm requestForm,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("HopeBook()");

        if(userDetails != null) {
            String userId = userDetails.getUsername();
            return libraryService.applicationBook(requestForm, userId);
        }
        return null;
    }

    // 희망 도서 목록
    @GetMapping("/hope-book-list")
    public List<HopeBook> hopeBookList() {
        log.info("희망 도서 목록 요청!");
        List<HopeBook> returnedHopeBookList = libraryService.hopeList();
        return returnedHopeBookList;
    }

    // 희망 도서 상세 페이지 읽기
    @GetMapping("/hope-book-read/{bookNumber}")
    public HopeBook readHopeBook (@PathVariable("bookNumber") Long bookNumber) {
        log.info("readHopeBook()");
        return libraryService.read(bookNumber);
    }

}
