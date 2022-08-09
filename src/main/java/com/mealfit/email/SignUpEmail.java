package com.mealfit.email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class SignUpEmail implements SendingEmailStrategy {

    private final String charSet = "utf-8";
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
            message.setSubject("회원가입 이메일 인증 - 혜림스 포토존", charSet);

            message.setText(new StringBuffer().append("<h1>[이메일 인증]</h1>")
                  .append("<p>아래 링크를 클릭하시면 이메일 인증이 완료됩니다.</p>")
                  .append("<a href='")
                  .append(url)
                  .append("/user/certification?username=")
                  .append(username)
                  .append("&authKey=")
                  .append(authKey)
                  .append("' target='_blenk'>이메일 인증 확인</a>")

                  .toString(), charSet, "html");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
