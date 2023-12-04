package kh.project.demo.domain.rental.controller.form;

import kh.project.demo.domain.book.entity.Book;
import kh.project.demo.domain.rental.entity.Rental;
import kh.project.demo.domain.member.entity.Member;
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
