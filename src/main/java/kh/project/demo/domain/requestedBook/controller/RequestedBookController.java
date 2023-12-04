package kh.project.demo.domain.requestedBook.controller;

import kh.project.demo.domain.requestedBook.controller.form.HopeBookForm;
import kh.project.demo.domain.requestedBook.entity.RequestedBook;
import kh.project.demo.domain.requestedBook.service.RequestedBookService;
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
public class RequestedBookController {

    final private RequestedBookService requestedBookService;

    // 사용자 희망 도서 신청
    @PostMapping("/hope-book")
    public RequestedBook HopeBook(
            @RequestBody HopeBookForm requestForm,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("HopeBook()");

        if(userDetails != null) {
            String userId = userDetails.getUsername();
            return requestedBookService.applicationBook(requestForm, userId);
        }
        return null;
    }

    // 희망 도서 목록
    @GetMapping("/hope-book-list")
    public List<RequestedBook> hopeBookList() {
        log.info("희망 도서 목록 요청!");
        List<RequestedBook> returnedRequestedBookList = requestedBookService.hopeList();
        return returnedRequestedBookList;
    }

    // 희망 도서 상세 페이지 읽기
    @GetMapping("/hope-book-read/{bookNumber}")
    public RequestedBook readHopeBook (@PathVariable("bookNumber") Long bookNumber) {
        log.info("readHopeBook()");
        return requestedBookService.read(bookNumber);
    }

    // 개인 사용자의 희망 도서 목록
    @GetMapping("personal-hope-list")
    public List<RequestedBook> personalHopeList(
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("personal hope list !");

        if(userDetails != null) {
            String userId = userDetails.getUsername();
            return requestedBookService.personalHopeList(userId);
        }
        return null;
    }

    // 회원 희망 도서 기록 건수
    @GetMapping("/personal-hope-amount")
    public Integer memberHopeAmount(
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails != null) {
            String userId = userDetails.getUsername();
            return requestedBookService.personalHopeAmount(userId);
        }
        return null;
    }

    // 전체 회원의 희망 도서 기록 건수
    @GetMapping("/management-hope-amount")
    public Integer managementHopeAmount(
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails != null) {
            String userId = userDetails.getUsername();
            return requestedBookService.managementHopeAmount(userId);
        }
        return null;
    }
}
