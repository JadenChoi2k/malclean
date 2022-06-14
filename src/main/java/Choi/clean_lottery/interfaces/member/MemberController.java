package Choi.clean_lottery.interfaces.member;

import Choi.clean_lottery.application.member.MemberFacade;
import Choi.clean_lottery.common.constant.LogConst;
import Choi.clean_lottery.domain.member.MemberCommand;
import Choi.clean_lottery.domain.member.MemberInfo;
import Choi.clean_lottery.domain.member.query.MemberQueryInfo;
import Choi.clean_lottery.interfaces.social.SocialLoginHandler;
import Choi.clean_lottery.interfaces.social.SocialUserInfo;
import Choi.clean_lottery.interfaces.social.kakao.KakaoAppConst;
import Choi.clean_lottery.common.constant.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    /**
     * 서비스를 탈퇴한다.
     * TODO: 카카오에서도 탈퇴시키는 절차 넣기.
     * @param request
     * @return
     */
    @GetMapping("/delete")
    public String withdraw(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object memberId = session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return "redirect:/";
        }
        memberFacade.deleteMember((Long) memberId);
        session.invalidate();
        return "redirect:/";
    }

    /**
     * 팀에서 나온다.
     * @param request
     * @return
     */
    @GetMapping("/team-out")
    public String outFromTeam(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object memberId = session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return "redirect:/";
        }
        memberFacade.getOutOfTeam((Long) memberId);
        return "redirect:/";
    }

    /**
     * 매니저만 접근 가능한 페이지
     */
    @GetMapping("/manager/team-out/{memberId}")
    public String kickOutMemberFromTeam(HttpServletRequest request, @PathVariable Long memberId) {
        Object managerId = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (managerId == null) {
            return "redirect:/";
         }
        MemberInfo managerInfo = memberFacade.retrieveMemberInfo((Long) managerId);
        if (!managerInfo.getIsManager()) {
            log.info("[{}] 매니저만 접근할 수 있는 페이지에 접근. 메인화면으로 리다이렉트", request.getAttribute(LogConst.LOG_ID));
            return "redirect:/";
        }
        memberFacade.getOutOfTeam(memberId);
        return "redirect:/";
    }

    @GetMapping("/manager/change/{memberId}")
    public String changeManager(HttpServletRequest request, @PathVariable Long memberId) {
        Object managerId = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (managerId == null) {
            return "redirect:/";
        }
        MemberInfo managerInfo = memberFacade.retrieveMemberInfo((Long) managerId);
        if (!managerInfo.getIsManager()) {
            log.info("[{}] 매니저만 접근할 수 있는 페이지에 접근. 메인화면으로 리다이렉트", request.getAttribute(LogConst.LOG_ID));
            return "redirect:/";
        }
        memberFacade.changeManager(MemberCommand.ChangeManagerRequest.builder()
                .prevManagerId((Long) managerId)
                .nextManagerId(memberId)
                .build());
        return "redirect:/";
    }

    /**
     * 내 정보를 조회한다.
     * @param request
     * @param model
     * @return
     */
    @GetMapping("/info")
    public String info(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Object memberId = session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return "redirect:/";
        }
        MemberQueryInfo.WithTeam withTeam = memberFacade.withTeam((Long) memberId);
        model.addAttribute("member", withTeam.getMemberInfo());
        model.addAttribute("team", withTeam.getTeamInfo());
        model.addAttribute("isManager", withTeam.getMemberInfo().getIsManager());
        return "info/memberInfo";
    }
}
