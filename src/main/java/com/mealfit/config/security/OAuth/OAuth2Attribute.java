package com.mealfit.config.security.OAuth;

import com.mealfit.user.domain.User;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class OAuth2Attribute {

    private String clientName;
    private Map<String, Object> attributes;
    private String attributeKey;
    private String email;
    private String name;
    private String nickname;
    private String picture;

    static OAuth2Attribute of(String provider, String attributeKey,
          Map<String, Object> attributes) {
        switch (provider) {
            case "GOOGLE":
                return ofGoogle(attributeKey, attributes);
            case "KAKAO":
                return ofKakao("email", attributes);
            case "NAVER":
                return ofNaver("id", attributes);
            default:
                throw new RuntimeException();
        }
    }

    private static OAuth2Attribute ofGoogle(String attributeKey,
          Map<String, Object> attributes) {
        return OAuth2Attribute.builder()
              .clientName("GOOGLE")
              .name((String) attributes.get("name"))
              .email((String) attributes.get("email"))
              .picture((String) attributes.get("picture"))
              .nickname((String) attributes.get("nickname"))
              .attributes(attributes)
              .attributeKey(attributeKey)
              .build();
    }

    private static OAuth2Attribute ofKakao(String attributeKey,
          Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuth2Attribute.builder()
              .clientName("KAKAO")
              .name((String) kakaoProfile.get("nickname"))
              .email((String) kakaoAccount.get("account_email"))
              .picture((String) kakaoProfile.get("profile_image_url"))
              .nickname((String) attributes.get("profile_nickname"))
              .attributes(kakaoAccount)
              .attributeKey(attributeKey)
              .build();
    }

    private static OAuth2Attribute ofNaver(String attributeKey,
          Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuth2Attribute.builder()
              .clientName("NAVER")
              .name((String) response.get("name"))
              .email((String) response.get("email"))
              .picture((String) response.get("profile_image"))
              .nickname((String) attributes.get("nickname"))
              .attributes(response)
              .attributeKey(attributeKey)
              .build();
    }

    Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", attributeKey);
        map.put("key", attributeKey);
        map.put("name", name);
        map.put("nickname", nickname);
        map.put("email", email);
        map.put("picture", picture);

        return map;
    }

    public User toEntity() {
        return new User(name, "SOCIAL_LOGIN", nickname, email, clientName);
    }
}