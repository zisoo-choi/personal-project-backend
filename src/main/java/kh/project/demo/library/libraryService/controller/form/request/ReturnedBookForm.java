package kh.project.demo.library.libraryService.controller.form.request;

import kh.project.demo.library.book.entity.Book;
import kh.project.demo.library.libraryService.entity.Rental;
import kh.project.demo.library.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReturnedBookForm {

//    private Long bookNumber;

    private Long rentalNumber;

    public Rental toReturnedBook(Book book, Member member){
        return Rental.builder()
                .book(book)
                .member(member)
                .build();
    }
}
