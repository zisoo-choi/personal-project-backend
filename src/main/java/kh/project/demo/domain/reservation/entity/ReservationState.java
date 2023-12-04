package kh.project.demo.domain.reservation.entity;

public enum ReservationState {
    Await, // 대기 (예약 후, 대출 전)
    Available, // 대출 가능
    Completion, // 완료 (예약 후 대출 후)
    Cancellation // 취소
}
