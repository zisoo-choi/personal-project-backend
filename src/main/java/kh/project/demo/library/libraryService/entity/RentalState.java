package kh.project.demo.library.libraryService.entity;

public enum RentalState {
    BookBefore, // 대출전 (정상)
    BookRental, // 대출 (정상)
    BookDelinquency, // 연체 (비정상)
    BookReservation, // 예약
    ServiceReturn, // 반납
    ServiceExtension, // 연장
}
