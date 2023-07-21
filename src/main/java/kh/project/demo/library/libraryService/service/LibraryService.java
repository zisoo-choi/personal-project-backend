package kh.project.demo.library.libraryService.service;

import jakarta.servlet.http.HttpServletRequest;
import kh.project.demo.library.libraryService.controller.form.request.HopeBookForm;
import kh.project.demo.library.libraryService.controller.form.request.RentalBookForm;
import kh.project.demo.library.libraryService.entity.HopeBook;

import java.util.List;

public interface LibraryService {
    boolean rental(RentalBookForm requestForm, String userId);

    boolean applicationBook(HopeBookForm requestForm, String userId);

    List<HopeBook> list();
}
