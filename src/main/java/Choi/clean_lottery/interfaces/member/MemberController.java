package Choi.clean_lottery.interfaces.member;

import Choi.clean_lottery.application.member.MemberFacade;
import Choi.clean_lottery.domain.member.MemberInfo;
import Choi.clean_lottery.interfaces.social.SocialLoginHandler;
import Choi.clean_lottery.interfaces.social.SocialUserInfo;
import Choi.clean_lottery.interfaces.social.kakao.KakaoAppConst;
import Choi.clean_lottery.web.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final SocialLoginHandler socialLoginHandler;
    private final MemberFacade memberFacade;
    private final MemberDtoMapper memberDtoMapper;

    @GetMapping("/join-pass")
    public String joinOrPass(HttpServletRequest request, HttpServletResponse response,
                             @CookieValue(name = "redirectURI", required = false) String redirectURI) {
        SocialUserInfo socialUserInfo = socialLoginHandler.handle(request, response);
        memberFacade.register(memberDtoMapper.of(socialUserInfo));
        return "redirect:" + (redirectURI == null ? "/" : redirectURI);
    }

    // 로그아웃시킨 후 메인화면으로 리다이렉트 시킨다.
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        Cookie logoutCookie = new Cookie(KakaoAppConst.TOKEN_COOKIE_NAME, "logout");
        logoutCookie.setPath("/");
        logoutCookie.setMaxAge(0);
        response.addCookie(logoutCookie);
        return "redirect:/";
    }

    @GetMapping("/delete")
    public String withdraw(HttpServletRequest request) {
        /**
         * 서비스를 탈퇴한다.
         * 서비스에서도 탈퇴시키고, 카카오에서도 탈퇴시킨다.
         */
        HttpSession session = request.getSession();
        Object memberId = session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return "redirect:/";
        }
        memberFacade.deleteMember((Long) memberId);
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/team-out")
    public String outFromTeam(HttpServletRequest request) {
        /**
         * 팀에서 나온다.
         * 팀에서 나온 후 메인화면으로 리다이렉트한다.
         */
        HttpSession session = request.getSession();
        Object memberId = session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return "redirect:/";
        }
        memberFacade.getOutOfTeam((Long) memberId);
        return "redirect:/";
    }

    @GetMapping("/info")
    public String info(HttpServletRequest request, Model model) {
        /**
         * 세션 조회
         * 유효한 세션이 아니면 메인화면으로 리다이렉트시킨다.
         * 유효한 세션이면 쿼리용 서비스를 조회하여 멤버를 꺼내서 모델 객체에 주입한다.
         */
        HttpSession session = request.getSession();
        Object memberId = session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return "redirect:/";
        }
        /**
         * 쿼리 전용 서비스 구현 후 퍼사드에서 호출
         */
        return "info/memberInfo";
    }
}
