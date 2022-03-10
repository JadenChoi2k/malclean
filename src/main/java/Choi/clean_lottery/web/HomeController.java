package Choi.clean_lottery.web;

import Choi.clean_lottery.domain.Member;
import Choi.clean_lottery.service.MemberService;
import Choi.clean_lottery.web.kakaoapi.KakaoApiHelper;
import Choi.clean_lottery.web.kakaoapi.KakaoTokenInfo;
import Choi.clean_lottery.web.kakaoapi.KakaoUserInfo;
import Choi.clean_lottery.web.member.MemberRequestFinder;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;
    private final MemberRequestFinder memberRequestFinder;

    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Object userId = session.getAttribute(SessionConst.LOGIN_MEMBER);

        if (userId == null) {
            return "index";
        } else {
            Member member = memberService.findOne((Long) userId);
            if (member == null) {
                return "index";
            }
            model.addAttribute(member);
            return "redirect:/team";
        }
    }
}
