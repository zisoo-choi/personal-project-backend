package kh.project.demo.domain.mail.service;

import kh.project.demo.domain.mail.controller.form.request.AuthenticationCodeForm;
import kh.project.demo.domain.mail.controller.form.request.EmailMessageToUserForm;

public interface EmailService {

    boolean sendMail(EmailMessageToUserForm emailMessageToUserForm);
    boolean authenticationCodeCheck(AuthenticationCodeForm authCode);
}
