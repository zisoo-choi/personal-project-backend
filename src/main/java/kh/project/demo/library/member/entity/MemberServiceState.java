package kh.project.demo.library.member.entity;

// ★ 회원의 도서관 서비스를 이용 상태
public enum MemberServiceState {
    ServiceNormal, // 정상
    ServiceRental, // 대출
    ServiceExtension, // 연장
    ServiceOverdue, // 연체
    ServiceReservation, // 예약
    ServiceReturn // 반납
}
