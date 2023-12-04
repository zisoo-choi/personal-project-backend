package kh.project.demo.domain.rental.controller;

import kh.project.demo.domain.rental.controller.form.ExtensionBookForm;
import kh.project.demo.domain.rental.controller.form.ReturnedBookForm;
import kh.project.demo.domain.rental.controller.form.RentalBookForm;
import kh.project.demo.domain.rental.entity.Rental;
import kh.project.demo.domain.rental.service.RentalService;
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
public class RentalController {

    final private RentalService rentalService;

    // 도서 대여
    @PostMapping("/rental")
    public boolean bookRental(
            @RequestBody RentalBookForm requestForm,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("도서 대여");

        if (userDetails != null) {
            String userId = userDetails.getUsername();
            // 로그인한 사용자의 정보를 활용한 로직 수행
            return rentalService.rental(requestForm, userId);
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
            return rentalService.extension(requestForm, userId);
        }
        return false;
    }

    // 도서 반납
    @PostMapping("/return")
    public boolean bookReturn(
            @RequestBody ReturnedBookForm requestForm,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("도서 반납");

        if (userDetails != null){
            String userId = userDetails.getUsername();
            return rentalService.returned(requestForm, userId);
        }

        return false;
    }

    // 대여 도서 리스트
    @GetMapping("/rental-book-list")
    public List<Rental> rentalBook() {
        log.info("대여 목록 요청!");

        List<Rental> returnedRentalBookList = rentalService.rentalList();
        return returnedRentalBookList;
    }

    // 개인 사용자의 도서 리스트
    @GetMapping("personal-rent-list")
    public List<Rental> personalRentList(
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("개인 대여 도서 조회");

        if(userDetails != null) {
            String userId = userDetails.getUsername();
            return rentalService.personalRentalList(userId);
        }
        return null;
    }

    // 회원 개인 대출 기록 건수
    @GetMapping("/personal-rental-amount")
    public Integer memberRentalAmount(
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails != null) {
            String userId = userDetails.getUsername();
            return rentalService.personalRentalAmount(userId);
        }
        return null;
    }

    // 전체 회원의 대출 기록 건수
    @GetMapping("/management-rental-amount")
    public Integer managementRentalAmount(
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails != null) {
            String userId = userDetails.getUsername();
            return rentalService.managementRentalAmount(userId);
        }
        return null;
    }
}
