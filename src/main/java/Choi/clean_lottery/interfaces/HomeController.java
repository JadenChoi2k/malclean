package Choi.clean_lottery.interfaces;

import Choi.clean_lottery.application.member.MemberFacade;
import Choi.clean_lottery.common.constant.SessionConst;
import Choi.clean_lottery.domain.member.Member;
import Choi.clean_lottery.domain.member.MemberInfo;
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

    private final MemberFacade memberFacade;

    @GetMapping("/")
    public String home(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session == null) {
            return "index";
        }

        Object memberId = session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return "index";
        } else {
            if (memberFacade.exists((Long) memberId)) {
                return "redirect:/team";
            } else {
                return "index";
            }
        }
    }
}
