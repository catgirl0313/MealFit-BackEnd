package com.mealfit.config.security.OAuth;

import com.mealfit.user.domain.User;
import com.mealfit.user.repository.UserRepository;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
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

        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 소셜 로그인 종류 구분
        String clientName = userRequest.getClientRegistration().getClientName();

        String userNameAttributeNameKey = userRequest
              .getClientRegistration()
              .getProviderDetails()
              .getUserInfoEndpoint()
              .getUserNameAttributeName();

        OAuth2Attribute oAuth2Attribute =
              OAuth2Attribute.of(clientName, userNameAttributeNameKey, oAuth2User.getAttributes());

        var memberAttribute = oAuth2Attribute.convertToMap();

        User user = loginOrSignUp(oAuth2Attribute);

        return new DefaultOAuth2User(
              Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
              memberAttribute, userNameAttributeNameKey);
    }

    private User loginOrSignUp(OAuth2Attribute attributes) {
        User user = userRepository.findByUsername(attributes.getName())
              .map(userEntity -> userEntity.update(attributes.getNickname(),
                    attributes.getPicture()))
              .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}
