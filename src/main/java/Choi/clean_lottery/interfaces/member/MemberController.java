package Choi.clean_lottery.interfaces.member;

import Choi.clean_lottery.application.member.MemberFacade;
import Choi.clean_lottery.interfaces.social.SocialLoginHandler;
import Choi.clean_lottery.interfaces.social.SocialUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        /**
         * TODO: 세션 주입.
         */
        memberFacade.register(memberDtoMapper.of(socialUserInfo));
        return "redirect:" + (redirectURI == null ? "/" : redirectURI);
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        /**
         * 로그아웃시킨 후 메인화면으로 리다이렉트한다.
         */
        return null;
    }

    @GetMapping("/delete")
    public String withdraw(HttpServletRequest request) {
        /**
         * 서비스를 탈퇴한다.
         * 서비스에서도 탈퇴시키고, 카카오에서도 탈퇴시킨다.
         */
        return null;
    }

    @GetMapping("/team-out")
    public String outFromTeam(HttpServletRequest request) {
        /**
         * 팀에서 나온다.
         * 팀에서 나온 후 메인화면으로 리다이렉트한다.
         */
        return null;
    }

    @GetMapping("/info")
    public String info(HttpServletRequest request, Model model) {
        /**
         * 세션 조회
         * 유효한 세션이 아니면 메인화면으로 리다이렉트시킨다.
         * 유효한 세션이면 쿼리용 서비스를 조회하여 멤버를 꺼내서 모델 객체에 주입한다.
         */
        return null;
    }
}
