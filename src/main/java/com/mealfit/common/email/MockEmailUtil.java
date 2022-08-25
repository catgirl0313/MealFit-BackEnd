package com.mealfit.common.email;

import lombok.extern.slf4j.Slf4j;

public class MockEmailUtil implements EmailUtil {

    @Override
    public void sendEmail(String toEmail, SendingEmailStrategy sendingEmailStrategy) {
    }
}
