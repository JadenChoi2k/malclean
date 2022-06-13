package Choi.clean_lottery.interfaces.team;

import Choi.clean_lottery.application.member.MemberFacade;
import Choi.clean_lottery.application.team.TeamFacade;
import Choi.clean_lottery.common.constant.SessionConst;
import Choi.clean_lottery.domain.member.Member;
import Choi.clean_lottery.domain.member.MemberInfo;
import Choi.clean_lottery.domain.member.query.MemberQueryInfo;
import Choi.clean_lottery.domain.role.RoleInfo;
import Choi.clean_lottery.domain.team.TeamCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {
    private final MemberFacade memberFacade;
    private final TeamFacade teamFacade;

    @GetMapping
    public String teamPage(HttpSession session, Model model) {
        Object memberId = session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return "redirect:/";
        }
        MemberQueryInfo.WithTeam withTeam = memberFacade.withTeam((Long) memberId);
        if (!withTeam.getMemberInfo().hasTeam()) {
            return "team/no-team";
        }
        model.addAttribute("team", withTeam.getTeamInfo());
        model.addAttribute("members", withTeam.getTeamInfo().getMembers());
        return "team/team-main";
    }

    @GetMapping("/edit")
    public String editPage(HttpServletRequest request, Model model) {
        Object memberId = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return "redirect:/";
        }
        MemberQueryInfo.WithTeam withTeam = memberFacade.withTeam((Long) memberId);
        model.addAttribute("isManager", withTeam.getMemberInfo().getIsManager());
        model.addAttribute("members", withTeam.getTeamInfo().getMembers());
        model.addAttribute("team", withTeam.getTeamInfo());
        return "team/team-edit";
    }

    @GetMapping("/add")
    public String addForm(@ModelAttribute("teamForm") TeamForm teamFrom) {
        return "team/add-team";
    }

    @PostMapping("/add")
    public String addTeam(@ModelAttribute @Valid TeamForm teamForm, BindingResult bindingResult,
                          HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "team/add-team";
        }
        Object memberId = session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return "redirect:/";
        }
        MemberInfo memberInfo = memberFacade.retrieveMemberInfo((Long) memberId);
        if (memberInfo.getPosition() != Member.Position.NONE) {
            return "redirect:/";
        }
        teamFacade.registerTeam(TeamCommand.RegisterTeam.builder()
                .managerId((Long) memberId)
                .teamName(teamForm.getTeamName())
                .build());
        return "redirect:/team";
    }

    @GetMapping("/roles/change-roles")
    public String rolesChangeForm(HttpServletRequest request, Model model) {
        Object memberId = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return "redirect:/";
        }
        MemberQueryInfo.WithTeam withTeam = memberFacade.withTeam((Long) memberId);
        if (!withTeam.getMemberInfo().hasTeam()) {
            return "redirect:/";
        }
        if (!withTeam.getMemberInfo().getIsManager()) {
            return "redirect:/team/roles";
        }
        RolesChangeForm changeForm = getRolesChangeForm(withTeam.getTeamInfo().getRoles(), withTeam.getTeamInfo().getCurrentRole());
        model.addAttribute("changeForm", changeForm);
        model.addAttribute("currentRole", withTeam.getTeamInfo().getCurrentRole());
        return "team/roles/roles-change-form";
    }

    private RolesChangeForm getRolesChangeForm(List<RoleInfo> roles, RoleInfo currentRole) {
        RolesChangeForm form = new RolesChangeForm();
        List<Long> roleIds = new ArrayList<>();
        List<String> roleNames = new ArrayList<>();
        for (var role : roles) {
            roleIds.add(role.getRoleId());
            roleNames.add(role.getName());
        }
        form.setRoleIds(roleIds);
        form.setRoleNames(roleNames);
        form.setStartDate(currentRole != null && currentRole.getStartDate() != null ? currentRole.getStartDate() : null);
        form.initializeSequence();
        return form;
    }

    @PostMapping("/roles/change-roles")
    public String changeRoles(HttpServletRequest request,
                              @Valid @ModelAttribute("changeForm") RolesChangeForm form,
                              BindingResult bindingResult) {
        Object memberId = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return "redirect:/";
        }
        MemberQueryInfo.WithTeam withTeam = memberFacade.withTeam((Long) memberId);
        if (!withTeam.getMemberInfo().hasTeam()) {
            return "redirect:/";
        }
        if (!withTeam.getMemberInfo().getIsManager()) {
            return "redirect:/team/roles";
        }
        if (bindingResult.hasErrors()) {
            return "team/roles/roles-change-form";
        }
        teamFacade.changeCurrentRole(TeamCommand.ChangeCurrentRoleRequest.builder()
                .teamId(withTeam.getTeamInfo().getTeamId())
                .roleId(form.getRoleIds().get(0))
                .startDate(form.getStartDate())
                .build());
        return "redirect:/team/roles";
    }
}
