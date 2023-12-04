package kh.project.demo.domain.reservation.controller;

import kh.project.demo.domain.reservation.controller.form.ReservationForm;
import kh.project.demo.domain.reservation.entity.Reservation;
import kh.project.demo.domain.reservation.service.ReservationService;
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
public class ReservationController {
    final private ReservationService reservationService;

    // 도서 예약
    @PostMapping("/reservation")
    public boolean bookReservation(
            @RequestBody ReservationForm requestForm,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("도서 예약");

        if(userDetails != null) {
            String userId = userDetails.getUsername();
            return reservationService.reservation(requestForm, userId);
        }
        return false;
    }

    // 도서 예약 목록 조회
    @GetMapping("/reservation-list")
    public List<Reservation> reservationList() {
        log.info("도서 예약 목록 요청!");

        List<Reservation> returnedReservationBookList = reservationService.reservationList();
        return returnedReservationBookList;
    }

    // 도서 예약 개인 목록 조회
    @GetMapping("/personal-reservation-list")
    public List<Reservation> personalReservationList(
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("personal reservation list()");

        if(userDetails != null) {
            String userId = userDetails.getUsername();
            return reservationService.personalReservationList(userId);
        }
        return null;
    }

    // 회원 개인 예약 기록 건수
    @GetMapping("/personal-reservation-amount")
    public Integer memberReservationAmount(
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails != null) {
            String userId = userDetails.getUsername();
            return reservationService.personalReservationAmount(userId);
        }
        return null;
    }


    // 전체 회원의 예약 기록 건수
    @GetMapping("/management-reservation-amount")
    public Integer managementReservationAmount(
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails != null) {
            String userId = userDetails.getUsername();
            return reservationService.managementReservationAmount(userId);
        }
        return null;
    }
}
