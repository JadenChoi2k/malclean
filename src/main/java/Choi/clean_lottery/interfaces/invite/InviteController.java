package Choi.clean_lottery.interfaces.invite;

import Choi.clean_lottery.application.invite.InviteFacade;
import Choi.clean_lottery.common.response.CommonResponse;
import Choi.clean_lottery.common.response.ErrorCode;
import Choi.clean_lottery.domain.invite.InviteCommand;
import Choi.clean_lottery.domain.invite.InviteInfo;
import Choi.clean_lottery.web.SessionConst;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequestMapping("/invite")
@Controller
@RequiredArgsConstructor
public class InviteController {
    private final InviteFacade inviteFacade;
    private final InviteDtoMapper inviteDtoMapper;

    @ResponseBody
    @GetMapping("/{teamId}/create")
    public CommonResponse create(HttpServletRequest request, @PathVariable Long teamId) {
        HttpSession session = request.getSession();
        Object memberId = session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return CommonResponse.fail("로그인", ErrorCode.COMMON_INVALID_ACCESS.getErrorMsg());
        }
        InviteInfo inviteInfo = inviteFacade.createInvite(InviteCommand.CreateInviteRequest.builder()
                .senderId((Long) memberId)
                .teamId(teamId)
                .build());
        return CommonResponse.success(inviteDtoMapper.of(inviteInfo));
    }

    @GetMapping("/{uuid}")
    public String inviteHome(@PathVariable String uuid, Model model) {
        InviteInfo inviteInfo = inviteFacade.retrieveInvite(uuid);
        if (inviteInfo == null) {
            return "invite/not-found";
        }
        if (inviteInfo.alreadyProcessed()) {
            model.addAttribute("status", inviteInfo.getStatus().getDescription());
            return "invite/already-processed";
        }
        model.addAttribute("invite", inviteInfo);
        return "invite/invite-home";
    }

    @GetMapping("/{uuid}/accept")
    public String accept(HttpServletRequest request, @PathVariable String uuid) {
        HttpSession session = request.getSession();
        Object memberId = session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return "redirect:/";
        }
        inviteFacade.acceptInvite(InviteCommand.AcceptInviteRequest.builder()
                .uuid(uuid)
                .receiverId((Long) memberId)
                .build());
        return "redirect:/";
    }

    @GetMapping("/{uuid}/reject")
    public String reject(HttpServletRequest request, @PathVariable String uuid) {
        HttpSession session = request.getSession();
        Object memberId = session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return "redirect:/invite/" + uuid;
        }
        inviteFacade.rejectInvite(InviteCommand.RejectInviteRequest.builder()
                .uuid(uuid)
                .receiverId((Long) memberId)
                .build());
        return "redirect:/invite/" + uuid;
    }
}
