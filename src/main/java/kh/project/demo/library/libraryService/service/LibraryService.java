package kh.project.demo.library.libraryService.service;

import jakarta.servlet.http.HttpServletRequest;
import kh.project.demo.library.libraryService.controller.form.request.*;
import kh.project.demo.library.libraryService.entity.HopeBook;
import kh.project.demo.library.libraryService.entity.Rental;
import kh.project.demo.library.libraryService.entity.Reservation;

import java.util.List;

public interface LibraryService {
    boolean rental(RentalBookForm requestForm, String userId);

    List<HopeBook> hopeList();

    HopeBook read(Long bookNumber);

    HopeBook applicationBook(HopeBookForm requestForm, String userId);

    List<Rental> rentalList();

    boolean extension(ExtensionBookForm requestForm, String userId);

    List<Rental> personalRentalList(String userId);

    boolean reservation(ReservationBookForm requestForm, String userId);

    List<Reservation> reservationList();

    List<Reservation> personalReservationList(String userId);

//    boolean returned(ReturnedBookForm requestForm, String userId);
}
