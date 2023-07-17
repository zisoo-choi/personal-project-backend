package kh.project.demo.library.libraryService.controller;

import kh.project.demo.library.libraryService.controller.form.request.RentalBookForm;
import kh.project.demo.library.libraryService.service.LibraryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/library-service")
public class LibraryController {

    final private LibraryService libraryService;

    // 도서 대여
    @PostMapping("/rental")
    public boolean bookRental(@RequestBody RentalBookForm requestForm) {
        return libraryService.rental(requestForm);
    }
}
