package com.mealfit.common.email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class SignUpEmail implements SendingEmailStrategy {

    private final String url;
    private final String username;
    private final String authKey;

    public SignUpEmail(String url, String username, String authKey) {
        this.url = url;
        this.username = username;
        this.authKey = authKey;
    }

    @Override
    public void fillMessage(MimeMessage message) {
        try {
            String charSet = "utf-8";
            message.setSubject("회원가입 이메일 인증 - MealFit", charSet);

            message.setText(new StringBuffer()
                  .append("<h1>[이메일 인증]</h1>")
                  .append("<p>아래 링크를 클릭하시면 이메일 인증이 완료됩니다.</p>")
                  .append("<a href='")
                  .append(url)
                  .append("/user/validate?username=")
                  .append(username)
                  .append("&authKey=")
                  .append(authKey)
                  .append("' target='_blenk'>이메일 인증 확인</a>")

                  .toString(), charSet, "html");
        } catch (MessagingException e) {
            throw new IllegalArgumentException("잘못된 전송 형식입니다. 이메일 전송에 실패했습니다.");
        }
    }
}
