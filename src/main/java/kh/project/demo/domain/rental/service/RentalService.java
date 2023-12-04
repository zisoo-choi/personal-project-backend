package kh.project.demo.domain.rental.service;

import kh.project.demo.domain.rental.controller.form.ExtensionBookForm;
import kh.project.demo.domain.rental.controller.form.ReturnedBookForm;
import kh.project.demo.domain.rental.controller.form.RentalBookForm;
import kh.project.demo.domain.rental.entity.Rental;

import java.util.List;

public interface RentalService {
    boolean rental(RentalBookForm requestForm, String userId);
    boolean extension(ExtensionBookForm requestForm, String userId);
    boolean returned(ReturnedBookForm requestForm, String userId);
    List<Rental> personalRentalList(String userId);
    Integer personalRentalAmount(String userId);
    Integer managementRentalAmount(String userId);
    List<Rental> rentalList();
}
