package Choi.clean_lottery.interfaces.social;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 소셜 로그인 가입을 시킨다.
 * 지금은 카카오톡만 쓰도록 한다.
 */
public interface SocialLoginHandler {
    SocialUserInfo handle(HttpServletRequest request, HttpServletResponse response);
}
