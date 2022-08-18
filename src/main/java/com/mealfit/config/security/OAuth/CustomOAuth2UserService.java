package com.mealfit.config.security.OAuth;

import com.mealfit.config.security.OAuth.OAuth2UserInfo.OAuth2UserInfo;
import com.mealfit.config.security.details.UserDetailsImpl;
import com.mealfit.user.domain.ProviderType;
import com.mealfit.user.domain.User;
import com.mealfit.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User user = super.loadUser(userRequest);

        try {
            return this.process(userRequest, user);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {
        ProviderType providerType = ProviderType.valueOf(
              userRequest.getClientRegistration().getRegistrationId().toUpperCase());

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType,
              user.getAttributes());

        User savedUser = loginOrSignUp(userInfo);
        return UserDetailsImpl.create(savedUser, user.getAttributes());
    }

    private User loginOrSignUp(OAuth2UserInfo userInfo) {
        User user = userRepository.findByUsername(userInfo.getId())
              .map(userEntity -> userEntity.update(userInfo.getNickname(),
                    userInfo.getImageUrl()))
              .orElse(userInfo.toEntity());

        return userRepository.save(user);
    }
}
