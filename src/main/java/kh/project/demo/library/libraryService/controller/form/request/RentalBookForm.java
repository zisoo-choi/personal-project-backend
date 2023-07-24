package kh.project.demo.library.libraryService.controller.form.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import kh.project.demo.library.book.entity.Book;
import kh.project.demo.library.libraryService.entity.Rental;
import kh.project.demo.library.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RentalBookForm {

    private Long bookNumber; // 대여 책
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime rentalDate = LocalDateTime.now(); // 대여 일자

    public Rental toRentalBook(Book book, Member member) {
        return Rental.builder()
                .book(book)
                .member(member)
                .estimatedRentalDate(rentalDate.plusDays(15))
                .extensionDate(null)
                .returnDate(null)
                .overdueDate(null)
                .build();
    }

}
