package Choi.clean_lottery.web.interceptor;

import Choi.clean_lottery.web.SessionConst;
import Choi.clean_lottery.web.kakaoapi.KakaoApiHelper;
import Choi.clean_lottery.web.member.MemberRequestFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Optional;

// 카카오톡에 로그인하고 연결해서 유효한 토큰을 받았는지 검증하는 인터셉터.
// TODO 이 인터셉터 삭제하기.
public class LoginCheckInterceptor implements HandlerInterceptor {

    private static final MemberRequestFinder memberFinder = new MemberRequestFinder(new KakaoApiHelper());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        Long userId = memberFinder.getUserId(request);

        if (userId != null) {
            return true;
        } else {
            response.sendRedirect("/?redirectURL=" + requestURI);
            return false;
        }
    }
}
