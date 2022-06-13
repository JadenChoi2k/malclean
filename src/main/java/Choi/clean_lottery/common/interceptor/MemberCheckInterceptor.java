package Choi.clean_lottery.common.interceptor;

import Choi.clean_lottery.common.constant.SessionConst;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// 사용자가 이 애플리케이션에 연결된 사용자인지를 확인하는 인터셉터
public class MemberCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession();

        if (session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            response.sendRedirect("/?redirectURI=" + requestURI);
            return false;
        }

        return true;
    }
}
