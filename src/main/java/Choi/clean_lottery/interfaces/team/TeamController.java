package Choi.clean_lottery.interfaces.team;

import Choi.clean_lottery.application.member.MemberFacade;
import Choi.clean_lottery.application.team.TeamFacade;
import Choi.clean_lottery.domain.member.Member;
import Choi.clean_lottery.domain.member.MemberInfo;
import Choi.clean_lottery.domain.member.query.MemberQueryInfo;
import Choi.clean_lottery.domain.team.TeamCommand;
import Choi.clean_lottery.web.SessionConst;
import Choi.clean_lottery.web.team.TeamForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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
}
