package com.mealfit.common.email;

public interface EmailUtil {

    void sendEmail(String toEmail, SendingEmailStrategy sendingEmailStrategy);
}
