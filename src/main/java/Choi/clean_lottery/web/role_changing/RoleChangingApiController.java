package Choi.clean_lottery.web.role_changing;

import Choi.clean_lottery.domain.member.Member;
import Choi.clean_lottery.domain.role_change.ChangeAreaCard;
import Choi.clean_lottery.domain.role_change.ChangeRoleTable;
import Choi.clean_lottery.domain.team.Team;
import Choi.clean_lottery.dto.ChangeRoleTableDto;
import Choi.clean_lottery.repository.ChangeRoleTableRepository;
import Choi.clean_lottery.service.MemberService;
import Choi.clean_lottery.service.TeamService;
import Choi.clean_lottery.web.SessionConst;
import Choi.clean_lottery.web.utils.MalUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/team/role-changing/api")
@RequiredArgsConstructor
public class RoleChangingApiController {

    private final MemberService memberService;
    private final TeamService teamService;
    private final ChangeRoleTableRepository tableRepository;
    private final MalUtility malUtility;

    @GetMapping("/{tableId}")
    public Map<String, Object> fetchById(@PathVariable Long tableId) {
        ChangeRoleTableDto tableDtoById = tableRepository.getTableDtoById(tableId);
        if (tableDtoById == null) {
            return malUtility.createErrorResponse("404 not found", "not found", 404);
        }
        return malUtility.createResultResponse("ok", "data", tableDtoById, 200);
    }

    @GetMapping("/{tableId}/{areaCardId}/get")
    public Map<String, Object> getInChargeOf(HttpServletRequest request, @PathVariable Long tableId,
                                             @PathVariable Long areaCardId) {
        Object memberId = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return malUtility.createErrorResponse("401 unauthorized", "you must be signed up", 401);
        }
        Optional<Member> member = memberService.findWithTeam((Long) memberId);
        if (member.isEmpty()) {
            return malUtility.createErrorResponse("404 not found", "user not found", 404);
        }
        ChangeRoleTable table = tableRepository.getTableWithTeamAndCards(tableId);
        if (table == null) {
            return malUtility.createErrorResponse("404 not found", "table not found", 404);
        }
        if (!table.getTeam().getId().equals(member.get().getTeam().getId())) {
            return malUtility.createErrorResponse("401 unauthorized", "not a member of team", 401);
        }
        ChangeAreaCard areaCard = getAreaCardFromTable(table, areaCardId);
        if (areaCard == null) {
            return malUtility.createErrorResponse("404 not found", "area card not found", 404);
        }
        areaCard.setChanger(member.get());
        tableRepository.save(table);
        return malUtility.createResultResponse("ok", "response", "ok", 200);
    }

    @GetMapping("/{tableId}/{areaCardId}/cancel")
    public Map<String, Object> detach(HttpServletRequest request, @PathVariable Long tableId,
                                      @PathVariable Long areaCardId) {
        Object memberId = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return malUtility.createErrorResponse("401 unauthorized", "you must be signed up", 401);
        }
        Optional<Member> member = memberService.findWithTeam((Long) memberId);
        if (member.isEmpty()) {
            return malUtility.createErrorResponse("404 not found", "user not found", 404);
        }
        ChangeRoleTable table = tableRepository.getTableWithTeamAndCards(tableId);
        if (table == null) {
            return malUtility.createErrorResponse("404 not found", "table not found", 404);
        }
        if (!table.getTeam().getId().equals(member.get().getTeam().getId())) {
            return malUtility.createErrorResponse("401 unauthorized", "not a member of team", 401);
        }
        ChangeAreaCard areaCard = getAreaCardFromTable(table, areaCardId);
        if (areaCard == null) {
            return malUtility.createErrorResponse("404 not found", "area card not found", 404);
        }
        if (areaCard.getChanger() == member.get()) {
            areaCard.detachChanger();
        }
        tableRepository.save(table);
        return malUtility.createResultResponse("ok", "response", "ok", 200);
    }

    @GetMapping("/{tableId}/{areaCardId}/done")
    public Map<String, Object> done(HttpServletRequest request, @PathVariable Long tableId,
                                    @PathVariable Long areaCardId) {
        Object memberId = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return malUtility.createErrorResponse("401 unauthorized", "you must be signed up", 401);
        }
        Optional<Member> member = memberService.findWithTeam((Long) memberId);
        if (member.isEmpty()) {
            return malUtility.createErrorResponse("404 not found", "user not found", 404);
        }
        ChangeRoleTable table = tableRepository.getTableWithTeamAndCards(tableId);
        if (table == null) {
            return malUtility.createErrorResponse("404 not found", "table not found", 404);
        }
        if (!table.getTeam().getId().equals(member.get().getTeam().getId())) {
            return malUtility.createErrorResponse("401 unauthorized", "not a member of team", 401);
        }
        ChangeAreaCard areaCard = getAreaCardFromTable(table, areaCardId);
        if (areaCard == null) {
            return malUtility.createErrorResponse("404 not found", "area card not found", 404);
        }
        if (areaCard.getChanger() == member.get()) {
            areaCard.completeChanging();
            tableRepository.save(table);
        }
        return malUtility.createResultResponse("ok", "response", "ok", 200);
    }

    @GetMapping("/{tableId}/complete")
    public Map<String, Object> complete(HttpServletRequest request, @PathVariable Long tableId) {
        if (!malUtility.isManager(request)) {
            return malUtility.createErrorResponse("401 unauthorized", "you must be a manager", 401);
        }
        Optional<Member> member = memberService.findWithTeam((Long) request.getSession().getAttribute(SessionConst.LOGIN_MEMBER));
        Optional<ChangeRoleTable> table = tableRepository.findById(tableId);
        if (table.isEmpty() || member.isEmpty()) {
            return malUtility.createErrorResponse("404 not found", "table not found", 404);
        }
        Team team = member.get().getTeam();
        if (table.get().getTeam() != team) {
            return malUtility.createErrorResponse("400 bad request", "table must be in team", 400);
        }
        if (table.get().getState() != ChangeRoleTable.Status.DONE) {
            return malUtility.createErrorResponse("400 bad request", "state of table must be done", 400);
        }
        team.setState(Team.Status.ON);
        team.setCurrentRole(table.get().getReceiveRole());
        team.getCurrentRole().setStartDate(LocalDate.now());
        teamService.save(team);
        return malUtility.createResultResponse("ok", "msg", "200 ok", 200);
    }

    private ChangeAreaCard getAreaCardFromTable(ChangeRoleTable table, Long areaCardId) {
        ChangeAreaCard areaCard = null;
        Optional<ChangeAreaCard> receive = table.getReceiveAreaCardList().
                stream().filter(a -> a.getId().equals(areaCardId)).findFirst();
        Optional<ChangeAreaCard> give = table.getGiveAreaCardList().
                stream().filter(a -> a.getId().equals(areaCardId)).findFirst();
        if (receive.isEmpty() && give.isEmpty()) {
            return null;
        }
        if (receive.isPresent()) {
            areaCard = receive.get();
        }
        if (give.isPresent()) {
            areaCard = give.get();
        }
        return areaCard;
    }
}
