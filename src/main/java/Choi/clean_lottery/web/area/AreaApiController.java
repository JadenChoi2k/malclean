package Choi.clean_lottery.web.area;

import Choi.clean_lottery.domain.area.Area;
import Choi.clean_lottery.domain.role.Role;
import Choi.clean_lottery.dto.AreaDto;
import Choi.clean_lottery.dto.RoleDto;
import Choi.clean_lottery.dto.TeamDto;
import Choi.clean_lottery.repository.AreaRepository;
import Choi.clean_lottery.repository.RoleRepository;
import Choi.clean_lottery.service.query.TeamQueryService;
import Choi.clean_lottery.web.SessionConst;
import Choi.clean_lottery.web.utils.MalUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/area/api")
@RequiredArgsConstructor
public class AreaApiController {

    private final RoleRepository roleRepository;
    private final AreaRepository areaRepository;
    private final TeamQueryService teamQueryService;
    private final MalUtility malUtility;

    private boolean teamHasArea(TeamDto team, Long areaId) {
        boolean hasAreaInRoles = false;
        for (RoleDto role : team.getRoles()) {
            if (role.isAreaOf(areaId)) {
                hasAreaInRoles = true;
            }
        }
        return hasAreaInRoles;
    }

    @GetMapping("/{areaId}")
    public Map<String, Object> getArea(HttpServletRequest request, @PathVariable Long areaId) {
//        Object memberId = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
//        if (memberId == null) {
//            return malUtility.createErrorResponse("401 unauthorized", "not signed up", 401);
//        }
        Optional<Area> area = areaRepository.findById(areaId);
        if (area.isEmpty()) {
            return malUtility.createErrorResponse("404 not found", "area not exist", 404);
        }
        return malUtility.createResultResponse("ok", "area", new AreaDto(area.get()), 200);
    }

    @RequestMapping("/delete/{areaId}")
    public Map<String, Object> deleteArea(HttpServletRequest request, @PathVariable Long areaId) {
        boolean isManager = malUtility.isManager(request);
        if (!isManager) {
            return malUtility.createErrorResponse("401 unauthorized", "not a manager of team", 401);
        }
        Long memberId = (Long) request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        TeamDto team = teamQueryService.findDtoByMemberId(memberId);
        boolean hasAreaInRoles = teamHasArea(team, areaId);
        if (!hasAreaInRoles) {
            return malUtility.createErrorResponse("404 not found", "area not exist in team", 404);
        }
        areaRepository.deleteAreaCascadeById(areaId);
        return malUtility.createResultResponse("success", null, null, 200);
    }

    @RequestMapping("/add")
    public Map<String, Object> addArea(HttpServletRequest request, @Validated @RequestBody AreaAddForm areaAddForm,
                                       BindingResult bindingResult) {
        boolean isManager = malUtility.isManager(request);
        if (!isManager) {
            return malUtility.createErrorResponse("401 unauthorized", "not a manager of team", 401);
        }
        if (bindingResult.hasErrors()) {
            return malUtility.createErrorResponse("400 bad request", "field error", 400);
        }
        Long memberId = (Long) request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        TeamDto team = teamQueryService.findDtoByMemberId(memberId);
        if (!team.isRoleOf(areaAddForm.getRoleId())) {
            return malUtility.createErrorResponse("404 not found", "team has no role for request", 404);
        }
        Role role = roleRepository.findById(areaAddForm.getRoleId());
        Area addArea = new Area(role, areaAddForm.getName(), areaAddForm.getDifficulty(),
                areaAddForm.getMinimumPeople(), areaAddForm.getChangeable());
        areaRepository.save(addArea);
        return malUtility.createResultResponse("201 createed", "area", new AreaDto(addArea), 201);
    }

    @RequestMapping("/edit/{areaId}")
    public Map<String, Object> editArea(HttpServletRequest request, @Validated @RequestBody AreaEditForm areaEditForm,
                                        BindingResult bindingResult) {
        boolean isManager = malUtility.isManager(request);
        if (!isManager) {
            return malUtility.createErrorResponse("401 unauthorized", "not a manager of team", 401);
        }
        if (bindingResult.hasErrors()) {
            return malUtility.createErrorResponse("400 bad request", "field error", 400);
        }
        Long memberId = (Long) request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        TeamDto team = teamQueryService.findDtoByMemberId(memberId);
        if (!team.isRoleOf(areaEditForm.getRoleId())) {
            return malUtility.createErrorResponse("404 not found", "team has no role for request", 404);
        }
        boolean areaInTeam = teamHasArea(team, areaEditForm.getAreaId());
        if (!areaInTeam) {
            return malUtility.createErrorResponse("404 not found", "area not exist in team", 404);
        }
        Optional<Area> area = areaRepository.findById(areaEditForm.getAreaId());
        if (area.isEmpty()) {
            return malUtility.createErrorResponse("404 not found", "area doest not exist", 404);
        }
        Area _area = area.get();
        _area.setName(areaEditForm.getName());
        _area.setDifficulty(areaEditForm.getDifficulty());
        _area.setMinimumPeople(areaEditForm.getMinimumPeople());
        _area.setChangeable(areaEditForm.getChangeable());
        areaRepository.save(_area);
        return malUtility.createResultResponse("area edited", "area", new AreaDto(_area), 200);
    }
}
