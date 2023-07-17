package kh.project.demo.library.libraryService.service;

import kh.project.demo.library.libraryService.controller.form.request.RentalBookForm;

public interface LibraryService {

    boolean rental(RentalBookForm requestForm);
}
