package kh.project.demo.domain.reservation.service;

import kh.project.demo.domain.reservation.controller.form.ReservationForm;
import kh.project.demo.domain.reservation.entity.Reservation;

import java.util.List;

public interface ReservationService {
    boolean reservation(ReservationForm requestForm, String userId);

    List<Reservation> reservationList();

    List<Reservation> personalReservationList(String userId);

    Integer personalReservationAmount(String userId);

    Integer managementReservationAmount(String userId);
}
