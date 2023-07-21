package kh.project.demo.library.libraryService.controller;

import kh.project.demo.library.book.entity.Book;
import kh.project.demo.library.libraryService.controller.form.request.HopeBookForm;
import kh.project.demo.library.libraryService.controller.form.request.RentalBookForm;
import kh.project.demo.library.libraryService.entity.HopeBook;
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

        if (userDetails != null) {
            String userId = userDetails.getUsername();
            // 로그인한 사용자의 정보를 활용한 로직 수행
            return libraryService.rental(requestForm, userId);
        }

        return false;
    }

    // 사용자 희망 도서 신청
    @PostMapping("/hope-book")
    public boolean HopeBook(
            @RequestBody HopeBookForm requestForm,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("HopeBook()");

        if(userDetails != null) {
            String userId = userDetails.getUsername();
            return libraryService.applicationBook(requestForm, userId);
        }
        return false;
    }

    // 희망 도서 목록
    @GetMapping("/hope-book-list")
    public List<HopeBook> hopeBookList() {
        log.info("희망 도서 목록 요청!");
        List<HopeBook> returnedHopeBookList = libraryService.list();
        return returnedHopeBookList;
    }

    // 희망 도서 상세 페이지 읽기
    @GetMapping("/hope-book-read/{bookNumber}")
    public HopeBook readHopeBook (@PathVariable("bookNumber") Long bookNumber) {
        log.info("readHopeBook()");
        return libraryService.read(bookNumber);
    }

}
