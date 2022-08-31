package com.mealfit.config.security.OAuth.handler;

import com.mealfit.authentication.application.JwtTokenService;
import com.mealfit.config.security.details.UserDetailsImpl;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenService jwtTokenService;

    @Value("${common.redirect-url}")
    private String redirectUrl;

    public OAuth2SuccessHandler(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
          Authentication authentication) throws IOException, ServletException {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String accessToken = jwtTokenService.createAccessToken(userDetails.getUsername())
              .getToken();
        String refreshToken = jwtTokenService.createRefreshToken(userDetails.getUsername())
              .getToken();

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(redirectUrl)
              .queryParam("accessToken", accessToken)
              .queryParam("refreshToken", refreshToken);

        if (isFirstSocialLogin(userDetails)) {
            uriComponentsBuilder
                  .queryParam("firstSocialLogin", true);
        } else {
            uriComponentsBuilder
                  .queryParam("firstSocialLogin", false);
        }

        String url = uriComponentsBuilder.build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, url);
    }

    private static boolean isFirstSocialLogin(UserDetailsImpl userDetails) {
        return userDetails.getUser().isSocialUser() && userDetails.getUser().isFirstLogin();
    }
}
