package kh.project.demo.domain.mail.controller.form.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationCodeForm {

    private String authCode;
}
