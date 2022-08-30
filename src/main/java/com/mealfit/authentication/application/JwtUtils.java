package com.mealfit.authentication.application;

import com.mealfit.authentication.domain.JwtToken;
import com.mealfit.authentication.domain.JwtTokenVerifyResult;

/**
 * JWT 전용 유틸리티 클래스
 *    - JWT 생성 및 해독이 너무 많은 클래스 퍼져 있었다.
 *    - 이를 한 클래스로 기능을 모아서 쓸 수 있게 리팩토링
 */
public interface JwtUtils {

    JwtToken issueAccessJwtToken(String username);

    JwtToken issueRefreshJwtToken(String username);

    JwtTokenVerifyResult verifyToken(String token);

    JwtToken issueBlackListToken(String token);
}