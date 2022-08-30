package com.mealfit.common.email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class FindPasswordEmail implements SendingEmailStrategy {

    private final String url;
    private final String username;
    private final String authKey;

    public FindPasswordEmail(String url, String username, String authKey) {
        this.url = url;
        this.username = username;
        this.authKey = authKey;
    }

    @Override
    public void fillMessage(MimeMessage message) {
        try {
            String charSet = "utf-8";
            message.setSubject("비밀번호 찾기 - MealFit", charSet);

            message.setText(new StringBuffer()
                  .append("<h1>[이메일 인증]</h1>")
                  .append("<p>아래 링크를 클릭하시면 비밀번호 변경 페이지로 이동합니다.</p>")
                  .append("<a href='")
                  .append(url)
                  .append("/user/validate?username=")
                  .append(username)
                  .append("&authKey=")
                  .append(authKey)
                  .append("' target='_blenk'>비밀번호 변경하기</a>")
                  .toString(), charSet, "html");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
