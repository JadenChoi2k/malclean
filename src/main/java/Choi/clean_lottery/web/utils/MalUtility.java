package Choi.clean_lottery.web.utils;

import Choi.clean_lottery.domain.Member;
import Choi.clean_lottery.dto.TeamDto;
import Choi.clean_lottery.service.MemberService;
import Choi.clean_lottery.service.TeamService;
import Choi.clean_lottery.service.query.TeamQueryService;
import Choi.clean_lottery.web.SessionConst;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class MalUtility {

    private final MemberService memberService;
    private final TeamService teamService;
    private final TeamQueryService teamQueryService;

    public boolean isManager(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session == null) return false;
        Object memberId = session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) return false;
        Member member = memberService.findOne((Long) memberId);
        TeamDto team = teamQueryService.findDtoByMemberId((Long) memberId);
        if (team == null) return false;
        return Objects.equals(team.getManager().getId(), member.getId());
    }

    public Map<String, Object> createErrorResponse(String error, String msg, Integer code) {
        Map<String, Object> ret = new HashMap<>();
        ret.put("error", error);
        ret.put("msg", msg);
        ret.put("code", code);
        return ret;
    }

    public Map<String, Object> createResultResponse(String msg, String name, Object object, Integer code) {
        Map<String, Object> ret = new HashMap<>();
        if (name != null) {
            ret.put(name, object);
        }
        ret.put("msg", msg);
        ret.put("code", code);
        return ret;
    }
}
