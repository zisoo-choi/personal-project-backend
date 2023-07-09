package kh.project.demo.library.mail.service;

import kh.project.demo.library.mail.controller.form.request.AuthenticationCodeForm;
import kh.project.demo.library.mail.controller.form.request.EmailMessageToUserForm;

public interface EmailService {

    void sendMail(EmailMessageToUserForm emailMessageToUserForm);
    boolean authenticationCodeCheck(AuthenticationCodeForm authCode);
}
