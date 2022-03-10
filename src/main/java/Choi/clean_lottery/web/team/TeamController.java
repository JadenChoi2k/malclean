package Choi.clean_lottery.web.team;

import Choi.clean_lottery.domain.Member;
import Choi.clean_lottery.dto.MemberDto;
import Choi.clean_lottery.dto.TeamDto;
import Choi.clean_lottery.service.MemberService;
import Choi.clean_lottery.service.TeamService;
import Choi.clean_lottery.service.query.TeamQueryService;
import Choi.clean_lottery.web.SessionConst;
import Choi.clean_lottery.web.utils.MalUtility;
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
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {

    private final MemberService memberService;
    private final TeamService teamService;
    private final TeamQueryService teamQueryService;
    private final MalUtility malUtility;

    @GetMapping
    public String teamPage(HttpSession session, Model model) {
        Long memberId = (Long) session.getAttribute(SessionConst.LOGIN_MEMBER);
        Member member = memberService.findOne(memberId);

        if (member == null) {
            return "redirect:/";
        }

        if (member.getTeam() == null) {
            return "team/no-team";
        }

        TeamDto team = teamQueryService.findDtoByMemberId(member.getId());
        List<MemberDto> members = team.getMembers();
        model.addAttribute("team", team);
        model.addAttribute("members", members);
        return "team/team-main";
    }

    @GetMapping("/edit")
    public String editPage(HttpServletRequest request, Model model) {
        boolean isManager = malUtility.isManager(request);
        Long memberId = (Long) request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        TeamDto teamDto = teamQueryService.findDtoByMemberId(memberId);
        List<MemberDto> members = teamDto.getMembers();
        model.addAttribute("isManager", isManager);
        model.addAttribute("members", members);
        model.addAttribute("team", teamDto);
        return "team/team-edit";
    }

    @GetMapping("/add")
    public String addForm(@ModelAttribute("teamForm") TeamForm teamForm) {
        return "team/add-team";
    }

    @PostMapping("/add")
    public String addTeam(@Valid @ModelAttribute TeamForm teamForm, BindingResult bindingResult,
                          HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "team/add-team";
        }

        Long memberId = (Long) session.getAttribute(SessionConst.LOGIN_MEMBER);
        teamService.createTeam(memberId, teamForm.getTeamName());

        return "redirect:/team";
    }
}
