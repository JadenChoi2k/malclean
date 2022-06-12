package Choi.clean_lottery.interfaces;

import Choi.clean_lottery.domain.member.Member;
import Choi.clean_lottery.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;

    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session == null) {
            return "index";
        }

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
