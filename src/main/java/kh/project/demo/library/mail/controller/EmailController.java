package kh.project.demo.library.mail.controller;

import kh.project.demo.library.mail.controller.form.request.AuthenticationCodeForm;
import kh.project.demo.library.mail.controller.form.request.EmailMessageToUserForm;
import kh.project.demo.library.mail.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/email-authentication")
public class EmailController {

    final private EmailService emailService;

    @Async
    @PostMapping("/send-email")
    public void sendEmail(@RequestBody EmailMessageToUserForm emailMessageToUserForm) {
        log.info("이메일 코드 보내기");

        emailService.sendMail(emailMessageToUserForm);
    }

    @PostMapping("/authentication-code")
    public boolean authCode(@RequestBody AuthenticationCodeForm authCode) {
        log.info("이메일 코드 검증");

        return emailService.authenticationCodeCheck(authCode);
    }


}
