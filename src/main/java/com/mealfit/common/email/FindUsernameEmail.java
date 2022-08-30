package com.mealfit.common.email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class FindUsernameEmail implements SendingEmailStrategy {

    private String maskedUsername;

    public FindUsernameEmail(String maskedUsername) {
        this.maskedUsername = maskedUsername;
    }

    @Override
    public void fillMessage(MimeMessage message) {
        try {
            String charSet = "utf-8";
            message.setSubject("아이디 찾기 - MealFit", charSet);

            message.setText(new StringBuffer()
                  .append("<h1>[아이디 찾기 서비스]</h1>")
                  .append("<p>해당 이메일로 회원가입되어 있는 아이디는 다음과 같습니다.</p>")
                  .append(maskedUsername)
                  .toString(), charSet, "html");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
