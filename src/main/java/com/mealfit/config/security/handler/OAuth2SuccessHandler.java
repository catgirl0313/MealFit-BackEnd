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
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;

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

        response.getOutputStream().write(objectMapper.writeValueAsBytes(loginResponseDto));
        getRedirectStrategy().sendRedirect(request, response, "https://localhost:3000/oauth2/redirect");
    }
}
