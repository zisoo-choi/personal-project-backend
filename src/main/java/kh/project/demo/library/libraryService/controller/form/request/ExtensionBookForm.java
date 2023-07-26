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
public class ExtensionBookForm {
    private Long bookNumber;

}
