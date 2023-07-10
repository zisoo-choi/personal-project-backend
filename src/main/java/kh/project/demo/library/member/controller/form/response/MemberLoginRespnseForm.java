package kh.project.demo.library.member.controller.form.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class MemberLoginRespnseForm {

    final private UUID userToken;
}
