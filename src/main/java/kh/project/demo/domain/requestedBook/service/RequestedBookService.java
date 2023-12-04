package kh.project.demo.domain.requestedBook.service;

import kh.project.demo.domain.requestedBook.controller.form.HopeBookForm;
import kh.project.demo.domain.requestedBook.entity.RequestedBook;

import java.util.List;

public interface RequestedBookService {
    List<RequestedBook> hopeList();

    RequestedBook read(Long bookNumber);

    RequestedBook applicationBook(HopeBookForm requestForm, String userId);

    List<RequestedBook> personalHopeList(String userId);

    Integer personalHopeAmount(String userId);

    Integer managementHopeAmount(String userId);
}
