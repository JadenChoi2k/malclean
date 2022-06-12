package Choi.clean_lottery.web.role_changing;

import Choi.clean_lottery.domain.role_change.ChangeRoleTable;
import Choi.clean_lottery.domain.member.Member;
import Choi.clean_lottery.domain.team.Team;
import Choi.clean_lottery.dto.ChangeRoleTableDto;
import Choi.clean_lottery.repository.ChangeRoleTableRepository;
import Choi.clean_lottery.service.MemberService;
import Choi.clean_lottery.service.TeamService;
import Choi.clean_lottery.interfaces.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

//@Controller
@RequestMapping("/team/role-changing")
@RequiredArgsConstructor
@Slf4j
public class RoleChangingController {

    private final MemberService memberService;
    private final TeamService teamService;
    private final ChangeRoleTableRepository tableRepository;

    @GetMapping
    public String roleChangingHome(HttpServletRequest request) {
        Object memberId = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return "redirect:/";
        }
        Optional<Member> member = memberService.findWithTeam((Long) memberId);
        if (member.isEmpty() || member.get().getTeam() == null ||
                member.get().getTeam().getState() != Team.Status.CHANGING_ROLE) {
            return "redirect:/";
        }
        ChangeRoleTable table = tableRepository.findFirstByTeamOrderByCreateDateDesc(member.get().getTeam());
        if (table == null) {
            return "redirect:/";
        }
        member.get().getTeam().setState(Team.Status.CHANGING_ROLE);
        teamService.merge(member.get().getTeam());
        return "redirect:/team/role-changing/" + table.getId();
    }

    @GetMapping("/{tableId}")
    public String roleChangingPage(HttpServletRequest request, @PathVariable Long tableId, Model model) {
        Object memberId = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return "redirect:/";
        }
        Optional<Member> member = memberService.findWithTeam((Long) memberId);
        ChangeRoleTable table = tableRepository.findFirstByTeamOrderByCreateDateDesc(member.get().getTeam());
        ChangeRoleTableDto tableDto = tableRepository.getTableDtoById(tableId);
        if (tableDto == null || !table.getId().equals(tableDto.getId())) {
            return "redirect:/";
        }
        model.addAttribute("isManager", member.get() == member.get().getTeam().getManager());
        model.addAttribute("memberId", memberId);
        model.addAttribute("changeTable", tableDto);
        model.addAttribute("givingRole", table.getGiveRole());
        model.addAttribute("receivingRole", table.getReceiveRole());
        return "team/roles/roles-changing-page";
    }
}
