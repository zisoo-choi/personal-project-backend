package kh.project.demo.library.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kh.project.demo.library.mail.controller.form.request.AuthenticationCodeForm;
import kh.project.demo.library.mail.controller.form.request.EmailMessageToUserForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender javaMailSender;
    private String authNumber = createKey();

    public void sendMail(EmailMessageToUserForm emailMessageToUserForm) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            mimeMessageHelper.setTo(emailMessageToUserForm.getTo()); // 메일 수신자
            mimeMessageHelper.setSubject("Cloud Library 회원 가입 인증 이메일 입니다."); // 메일 제목
            mimeMessageHelper.setText("Cloud Library 회원 가입 인증 코드는 "+authNumber+" 입니다."); // 메일 본문 내용, HTML 여부

            javaMailSender.send(mimeMessage);
            log.info("Success");

        } catch (MessagingException e) {
            log.info("fail");

            throw new RuntimeException(e);
        }
    }

    public boolean authenticationCodeCheck(AuthenticationCodeForm authCode) {
        if (authCode.getAuthCode().equals(authNumber)) {
            log.info("이메일 인증 성공");
            log.info("authNumber: "+authNumber+", authCode: "+authCode.getAuthCode());
            return true;
        } else {
            log.info("이메일 인증 실패");
            log.info("authNumber: "+authNumber+", authCode: "+authCode.getAuthCode());
            return false;
        }
    }

    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    //  A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
        }
        return key.toString();
    }
}
