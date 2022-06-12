package Choi.clean_lottery.interfaces.role_changing;

import Choi.clean_lottery.application.member.MemberFacade;
import Choi.clean_lottery.application.role_change.ChangeRoleTableFacade;
import Choi.clean_lottery.domain.member.query.MemberQueryInfo;
import Choi.clean_lottery.domain.role_change.ChangeRoleTableCommand;
import Choi.clean_lottery.domain.role_change.ChangeRoleTableInfo;
import Choi.clean_lottery.web.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequestMapping("/team/role-changing")
@RequiredArgsConstructor
public class RoleChangingController {
    private final ChangeRoleTableFacade changeRoleTableFacade;
    private final MemberFacade memberFacade;

    public String roleChangingHome(HttpServletRequest request) {
        Object memberId = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return "redirect:/";
        }
        MemberQueryInfo.WithTeam withTeam = memberFacade.withTeam((Long) memberId);
        if (withTeam.getMemberInfo() == null || withTeam.getTeamInfo() == null ||
        !withTeam.getTeamInfo().isChangingRole()) {
            return "redirect:/";
        }
        Long tableId = changeRoleTableFacade.findTableIdByTeamId(withTeam.getTeamInfo().getTeamId());
        return "redirect:/team/role-changing/" + tableId;
    }

    @GetMapping("/role-changing/start/{roleId}")
    public String rolesChangingStart(HttpServletRequest request, @PathVariable Long roleId) {
        Object memberId = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return "redirect:/";
        }
        MemberQueryInfo.WithTeam withTeam = memberFacade.withTeam((Long) memberId);
        if (withTeam.getMemberInfo().getIsManager()) {
            Long currentRoleId = withTeam.getTeamInfo().getCurrentRole().getRoleId();
            if (currentRoleId.equals(roleId)) {
                return "redirect:/team";
            }
            if (!withTeam.getTeamInfo().isRoleOf(roleId)) {
                return "redirect:/team";
            }
            changeRoleTableFacade.register(ChangeRoleTableCommand.RegisterTableRequest.builder()
                    .teamId(withTeam.getTeamInfo().getTeamId())
                    .giveRoleId(currentRoleId)
                    .receiveRoleId(roleId)
                    .build());
            return "redirect:/team/role-changing";
        }
        return "redirect:/team";
    }

    @GetMapping("/{tableId}")
    public String roleChangingPage(HttpServletRequest request, @PathVariable Long tableId, Model model) {
        Object memberId = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return "redirect:/";
        }
        ChangeRoleTableInfo.Main tableInfo = changeRoleTableFacade.retrieve(tableId);
        MemberQueryInfo.WithTeam withTeam = memberFacade.withTeam((Long) memberId);
        if (tableInfo == null || withTeam.getTeamInfo() == null ||
                !tableInfo.getTeamId().equals(withTeam.getTeamInfo().getTeamId())) {
            return "redirect:/";
        }
        model.addAttribute("isManager", withTeam.getMemberInfo().getIsManager());
        model.addAttribute("memberId", withTeam.getMemberInfo().getMemberId());
        model.addAttribute("changeTable", tableInfo);
        model.addAttribute("receivingRole", tableInfo.getReceiveRoleName());
        model.addAttribute("givingRole", tableInfo.getGiveRoleName());
        return "team/roles/roles-changing-page";
    }
}
