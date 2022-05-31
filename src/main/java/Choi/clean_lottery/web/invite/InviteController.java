package Choi.clean_lottery.web.invite;

import Choi.clean_lottery.domain.invite.Invite;
import Choi.clean_lottery.domain.member.Member;
import Choi.clean_lottery.dto.InviteDto;
import Choi.clean_lottery.dto.TeamDto;
import Choi.clean_lottery.service.InviteService;
import Choi.clean_lottery.service.MemberService;
import Choi.clean_lottery.service.TeamService;
import Choi.clean_lottery.service.query.TeamQueryService;
import Choi.clean_lottery.web.SessionConst;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/invite")
@Controller
@RequiredArgsConstructor
public class InviteController {

    private final MemberService memberService;
    private final TeamService teamService;
    private final TeamQueryService teamQueryService;
    private final InviteService inviteService;
    private Map<Invite.Status, String> statusStringMap = new HashMap<>() {{
        put(Invite.Status.WAITING, "대기 중");
        put(Invite.Status.REJECTED, "거절");
        put(Invite.Status.ACCEPTED, "수락");
    }};

    @ResponseBody
    @GetMapping("/create")
    public Map<String, Object> create(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        HttpSession session = request.getSession();
        Object memberId = session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            response.put("msg", "has no session");
            response.put("code", -401);
            return response;
        }
        Member sender = memberService.findOne((Long) memberId);
        TeamDto teamDto = teamQueryService.findDtoByMemberId(sender.getId());
        Invite invite = inviteService.createInvite(sender, null, sender.getTeam());
        // json 객체에 말아서 전달된다.
        response.put("uuid", invite.getUuid());
        response.put("senderName", sender.getName());
        response.put("teamName", teamDto.getName());
        return response;
    }

    @GetMapping("/{uuid}")
    public String inviteHome(@PathVariable String uuid, Model model) {
        InviteDto invite = inviteService.findOneDto(uuid);
        if (invite == null) {
            return "invite/not-found";
        }
        if (invite.getStatus() == Invite.Status.ACCEPTED ||
            invite.getStatus() == Invite.Status.REJECTED) {
            model.addAttribute("status", statusStringMap.get(invite.getStatus()));
            return "invite/already-processed";
        }
        model.addAttribute("invite", invite);
        return "invite/invite-home";
    }

    @GetMapping("/{uuid}/accept")
    public String accept(HttpServletRequest request, @PathVariable String uuid) {
        HttpSession session = request.getSession();
        Object memberId = session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return "redirect:/";
        }
        Member receiver = memberService.findOne((Long) memberId);
        // result : true => 수락 완료 / false => 수락할 수 없다.
        boolean result = inviteService.acceptInvite(uuid, receiver);
        return "redirect:/";
    }

    @GetMapping("/{uuid}/reject")
    public String reject(HttpServletRequest request, @PathVariable String uuid) {
        HttpSession session = request.getSession();
        Object memberId = session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return "redirect:/invite/" + uuid;
        }
        Member receiver = memberService.findOne((Long) memberId);
        boolean result = inviteService.rejectInvite(uuid, receiver);
        return "redirect:/invite/" + uuid;
    }
}
