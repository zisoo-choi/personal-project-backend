package kh.project.demo.library.member.entity;

public enum MemberRole {
    NORMAL("NORMAL"),
    MANAGER("MANAGER"),
    ;

    String cdNm;

    MemberRole(String cdNm) {
        this.cdNm = cdNm;
    }
}
