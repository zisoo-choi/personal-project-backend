package kh.project.demo.library.book.entity;

public enum BookState {
    BookBefore, // 대출전 (정상)
    BookRental, // 대출 (정상)
    BookDelinquency, // 연체 (비정상)
    BookReservation // 예약
}
