package Choi.clean_lottery.web.member;

import Choi.clean_lottery.domain.member.Member;
import Choi.clean_lottery.dto.TeamDto;
import Choi.clean_lottery.service.MemberService;
import Choi.clean_lottery.service.query.TeamQueryService;
import Choi.clean_lottery.web.SessionConst;
import Choi.clean_lottery.web.WebConfig;
import Choi.clean_lottery.web.kakaoapi.KakaoApiHelper;
import Choi.clean_lottery.web.kakaoapi.KakaoTokenInfo;
import Choi.clean_lottery.web.kakaoapi.KakaoUserInfo;
import Choi.clean_lottery.web.utils.MalUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final TeamQueryService teamQueryService;
    private final KakaoApiHelper kakaoApiHelper;
    private final MemberRequestFinder memberRequestFinder;
    private final MalUtility malUtility;

    // 카카오톡 로그인 후 리다이렉트 되는 곳.
    // 사용자를 판단한 후, 유저 DB에 삽입하거나 업데이트(이름 등)한다.
    // 이후 redirectURI가 null이 아니면 redirect해주고, null이면 "/"으로 리다이렉트한다.
    // 세션 주입도 여기서 한다...
    @GetMapping("/join-pass")
    public String joinOrPass(HttpServletRequest request, HttpServletResponse response,
                             @CookieValue(name = "redirectURI", required = false) String redirectURI,
                             @RequestParam String code)
            throws JsonProcessingException {
        KakaoTokenInfo tokenInfo;
        try {
            String tokenRedirectUrl = WebConfig.APP_DOMAIN + request.getRequestURI();
            tokenInfo = kakaoApiHelper.getTokenByCode(code, tokenRedirectUrl);
        } catch (Exception e) {
            log.info("tokenInfo를 받아오던 중 오류가 생겼습니다 {}", e.toString());
            tokenInfo = kakaoApiHelper.getTokenByCode(code);
        }
        if (tokenInfo.hasError()) {
            log.info("tokenInfo에 에러가 있습니다."); // <--
            log.info("에러 메시지 -> {}", tokenInfo.errorToString());
            return "redirect:/";
        }
        KakaoUserInfo kakaoUserInfo = memberRequestFinder.getKakaoUserInfo(tokenInfo.getAccess_token());
        if (kakaoUserInfo == null || kakaoUserInfo.hasError()) {
            log.info("kakoUserInfo가 null이거나 kakaoUserInfo에 에러가 있습니다.");
            return "redirect:/";
        }

        // kakao_account로부터 json 파싱을 통해 유저의 정보를 가져온다.
        JSONObject kakao_account = kakaoUserInfo.getKakao_account();
        JSONObject profile = kakao_account.getJSONObject("profile");
        String nickname = profile.getString("nickname");
        String profile_image_url = profile.getString("profile_image_url");
        // 멤버가 등록되어 있지 않으면 조인시키고, 등록되어 있으면 업데이트한다.
        Member findResult = memberService.findOne(kakaoUserInfo.getId());
        if (findResult == null) {
            Member joinMember = new Member(kakaoUserInfo.getId(), nickname, profile_image_url,
                    LocalDateTime.now(), LocalDateTime.now());
            findResult = memberService.join(joinMember);
        } else {
            findResult = memberService.merge(findResult);
        }
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(tokenInfo.getExpires_in());
        session.setAttribute(SessionConst.LOGIN_MEMBER, findResult.getId());
        Cookie token_cookie = new Cookie("authorize-access-token", tokenInfo.getAccess_token());
        token_cookie.setMaxAge(tokenInfo.getExpires_in());
        token_cookie.setPath("/");
        response.addCookie(token_cookie);

        if (redirectURI == null) {
            return "redirect:/";
        } else {
            return "redirect:" + redirectURI;
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        Cookie token_cookie = new Cookie("authorize-access-token", "expired");
        token_cookie.setPath("/");
        token_cookie.setMaxAge(0);
        response.addCookie(token_cookie);
        return "redirect:/";
    }

    @GetMapping("/delete")
    public String withdraw(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object memberId = session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return "redirect:/";
        }
        memberService.deleteMember((Long) memberId);
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/team-out")
    public String outFromTeam(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object memberId = session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return "redirect:/";
        }
        memberService.getOutOfTeam((Long) memberId);
        return "redirect:/";
    }

    @GetMapping("/manager/change/{memberId}")
    public String changeManager(HttpServletRequest request, @PathVariable Long memberId) {
        Object managerId = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (managerId == null) {
            return "redirect:/";
        }
        boolean result = memberService.changeManager((Long) managerId, memberId);
        return "redirect:/";
    }

    // 매니저가 강퇴시키는 경로
    @RequestMapping("/team-out/{memberId}")
    public String kickOutMember(HttpServletRequest request, @PathVariable Long memberId) {
        Object managerId = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (managerId == null) {
            return "redirect:/";
        }
        boolean result = memberService.kickOutMember((Long) managerId, memberId);
        return "redirect:/";
    }

    @GetMapping("/info")
    public String info(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Object memberId = session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return "redirect:/";
        }
        Member member = memberService.findOne((Long) memberId);
        TeamDto teamDto = teamQueryService.findDtoByMemberId((Long) memberId);
        model.addAttribute("member", member);
        model.addAttribute("team", teamDto);
        model.addAttribute("isManager", malUtility.isManager(request));
        return "info/memberInfo";
    }
}
