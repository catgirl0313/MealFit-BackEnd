package com.mealfit.user.application;

import com.mealfit.common.email.EmailUtil;
import com.mealfit.common.email.FindPasswordEmail;
import com.mealfit.common.email.FindUsernameEmail;
import com.mealfit.common.email.SignUpEmail;
import com.mealfit.user.application.dto.request.EmailAuthRequestDto;
import com.mealfit.user.application.dto.request.SendEmailRequestDto;
import com.mealfit.user.domain.email.EmailCertificationRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmailService {

    private final EmailUtil emailUtil;
    private final EmailCertificationRepository emailCertificationRepository;

    public EmailService(EmailUtil emailUtil,
          EmailCertificationRepository emailCertificationRepository) {
        this.emailUtil = emailUtil;
        this.emailCertificationRepository = emailCertificationRepository;
    }

    public void sendEmail(SendEmailRequestDto dto) {
        String authKey = UUID.randomUUID().toString();

        switch (dto.getEmailType()) {
            case VALID_NEW_ACCOUNT:
                emailUtil.sendEmail(dto.getSendingEmail(),
                      new SignUpEmail(dto.getRedirectUrl(), dto.getUsername(), authKey));
                return;
            case FIND_USERNAME:
                emailUtil.sendEmail(dto.getSendingEmail(),
                      new FindUsernameEmail(dto.getUsername()));
                return;
            case FIND_PASSWORD:
                emailUtil.sendEmail(dto.getSendingEmail(),
                      new FindPasswordEmail(dto.getRedirectUrl(), dto.getUsername(), authKey));
                return;
            default:
                throw new IllegalArgumentException("잘못된 이메일 양식입니다.");
        }
    }

    @Transactional
    public void authEmail(EmailAuthRequestDto dto) {
//        User user = findByUsername(dto.getUsername());
//
//        if (!user.checkNotValid()) {
//            throw new IllegalStateException("이미 인증을 완료했습니다.");
//        }
//
//        if (!emailCertificationRepository.existsByUsernameAndAuthKey(dto.getUsername(),
//              dto.getAuthKey())) {
//            throw new IllegalArgumentException("잘못된 요청입니다. 이메일 전송을 다시 한번 요청하세요.");
//        }
//
//        user.changeUserStatus(UserStatus.FIRST_LOGIN);
    }
}
