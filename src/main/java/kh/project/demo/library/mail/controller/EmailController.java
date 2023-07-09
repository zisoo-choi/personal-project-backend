package kh.project.demo.library.mail.controller;

import kh.project.demo.library.mail.controller.form.AuthenticationCodeForm;
import kh.project.demo.library.mail.controller.form.EmailMessage;
import kh.project.demo.library.mail.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mail-test")
public class EmailController {

    final private EmailService emailService;

    @PostMapping("/user-send-email")
    public void sendEmail(@RequestBody EmailMessage emailMessage){
        log.info("동작 !");

        emailService.sendMail(emailMessage);
    }

    @PostMapping("/authentication-code")
    public boolean authCode(@RequestBody AuthenticationCodeForm authCode) {
        log.info("이메일 코드 검증 동작");

        return emailService.authenticationCodeCheck(authCode);
    }


}
