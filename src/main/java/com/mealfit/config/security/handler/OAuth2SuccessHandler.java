package com.mealfit.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealfit.config.security.dto.LoginResponseDto;
import com.mealfit.config.security.jwt.JwtUtils;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;

    @Value("${common.redirect-url}")
    private String redirectUrl;

    public OAuth2SuccessHandler(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
          Authentication authentication) throws IOException, ServletException {

        ObjectMapper objectMapper = new ObjectMapper();

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        Map<String, Object> attributes = oAuth2User.getAttributes();

        String accessToken = jwtUtils.issueAccessToken((String) attributes.get("username"));
        String refreshToken = jwtUtils.issueRefreshToken((String) attributes.get("username"));

        LoginResponseDto loginResponseDto = new LoginResponseDto(accessToken, refreshToken,
              (String) attributes.get("nickname"),
              (String) attributes.get("picture"));

        String url = UriComponentsBuilder.fromUriString(redirectUrl)
              .queryParam("accessToken", accessToken)
              .queryParam("refreshToken", refreshToken)
              .build().toUriString();

        response.getOutputStream().write(objectMapper.writeValueAsBytes(loginResponseDto));
        getRedirectStrategy().sendRedirect(request, response, url);
    }
}
