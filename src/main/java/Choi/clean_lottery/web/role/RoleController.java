package Choi.clean_lottery.web.role;

import Choi.clean_lottery.domain.Role;
import Choi.clean_lottery.dto.RoleDto;
import Choi.clean_lottery.dto.TeamDto;
import Choi.clean_lottery.service.RoleService;
import Choi.clean_lottery.service.query.RoleQueryService;
import Choi.clean_lottery.service.query.TeamQueryService;
import Choi.clean_lottery.web.SessionConst;
import Choi.clean_lottery.web.utils.MalUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

@RequestMapping("/team/roles")
@Slf4j
@Controller
@RequiredArgsConstructor
public class RoleController {

    private final TeamQueryService teamQueryService;
    private final RoleService roleService;
    private final RoleQueryService roleQueryService;
    private final MalUtility malUtility;
    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    @GetMapping
    public String roleHome(HttpServletRequest request, Model model) {
        // 매니저일 경우 수정/추가 버튼 있다.
        boolean isManager = malUtility.isManager(request);
        TeamDto team = teamQueryService.findDtoByMemberId(
                (Long) request.getSession().getAttribute(SessionConst.LOGIN_MEMBER));
        if (team == null)
            return "redirect:/";
        List<RoleDto> roles = team.getRoles();
        if(!roles.isEmpty()) model.addAttribute("currentRole", team.getCurrentRole());
        model.addAttribute("roles", roles);
        model.addAttribute("isManager", isManager);

        return "team/roles/role-home";
    }

    // RoleApiController로 옮겨질 예정
    @GetMapping("/{roleId}")
    @ResponseBody
    public Map<String, Object> getRole(@PathVariable Long roleId) {
        Role role = roleService.findWithAreas(roleId);
        Map<String, Object> json = new HashMap<>();
        if (role != null) {
            json.put("id", role.getId());
            json.put("name", role.getName());
            json.put("area", role.getAreas().toArray());
            json.put("startDate", role.getStartDate());
        } else {
            json.put("error", 404);
            json.put("msg", "not found");
        }
        return json;
    }

    // 전체 역할의 순서나 시작 날짜(첫 번째 역할) 등을 수정해서 제출한다.
    @GetMapping("/change-roles")
    public String rolesChangeForm(HttpServletRequest request, Model model) {
        boolean isManager = malUtility.isManager(request);
        if (!isManager) {
            return "redirect:/team/roles";
        }
        TeamDto team = teamQueryService.findDtoByMemberId((Long) request.getSession().getAttribute(SessionConst.LOGIN_MEMBER));
        List<RoleDto> roles = team.getRoles();
        RolesChangeForm changeForm = getRolesChangeForm(roles, team.getCurrentRole());
        model.addAttribute("changeForm", changeForm);
        model.addAttribute("currentRole", team.getCurrentRole());
        return "team/roles/roles-change-form";
    }

    private RolesChangeForm getRolesChangeForm(List<RoleDto> roles, RoleDto currentRole) {
        RolesChangeForm form = new RolesChangeForm();
        List<Long> roleIds = new ArrayList<>();
        List<String> roleNames = new ArrayList<>();
        for (RoleDto role : roles) {
            roleIds.add(role.getId());
            roleNames.add(role.getName());
        }
        form.setRoleIds(roleIds);
        form.setRoleNames(roleNames);
        form.setStartDate(currentRole.getStartDate());
        form.initializeSequence();
        return form;
    }

    // TODO : changeSequence 없애고, role의 startDate랑 team의 currentRole만 바꾸도록 한다.
    @PostMapping("/change-roles")
    public String changeRoles(HttpServletRequest request,
                              @Validated @ModelAttribute("changeForm") RolesChangeForm form,
                              BindingResult bindingResult) {
        boolean isManager = malUtility.isManager(request);
        if (!isManager) {
            bindingResult.reject("unauthorized.manager");
            return "redirect:/";
        }
        TeamDto teamDto = teamQueryService.findDtoByMemberId((Long) request.getSession().getAttribute(SessionConst.LOGIN_MEMBER));
        List<RoleDto> roles = teamDto.getRoles();
        if (roles.isEmpty()) {
            bindingResult.reject("WrongRequest");
        }
        List<Long> roleIds = form.getRoleIds();
        for (RoleDto role : roles) {
            if (!roleIds.contains(role.getId()))
                bindingResult.reject("WrongRequest.wrongId.role");
        }
        if (bindingResult.hasErrors()) {
            return "team/roles/roles-change-form";
        }
        // 순서 반영
        roleService.changeSequence(teamDto.getId(), roleIds);
        // 시작 날짜 반영 (null이면 하지 않는다)
        if (form.getStartDate() != null)
            roleService.changeStartDate(roleIds.get(0), form.getStartDate());
        return "redirect:/team/roles";
    }

    @GetMapping("/{roleId}/edit")
    public String roleEditForm(HttpServletRequest request, @PathVariable Long roleId,
                               Model model) {
        boolean isManager = malUtility.isManager(request);
        if (!isManager) {
            // 참고로 말하지만 예외 처리 다 해줘야 한다.
            return "redirect:/team/roles";
        }
        RoleDto role = roleQueryService.findDto(roleId);
        if (role == null) {
            return "redirect:/team/roles";
        }
        RoleEditForm editForm = new RoleEditForm(role);
        model.addAttribute("areas", role.getAreas());
        model.addAttribute("editForm", editForm);
        return "team/roles/edit-role";
    }

    @PostMapping("/{roleId}/edit")
    public String edit(HttpServletRequest request, @Validated @ModelAttribute("editForm") RoleEditForm editForm,
                       BindingResult bindingResult, @PathVariable Long roleId) {
        Role role = roleService.findOne(roleId);
        if (!malUtility.isManager(request)) {
            bindingResult.reject("unauthorized.manager");
        }
        if (role == null) {
            bindingResult.reject("WrongRequest");
        }
        if (role!=null && !role.getId().equals(editForm.getId())) {
            bindingResult.reject("WrongRequest.wrongId.role");
        }
        if (bindingResult.hasErrors()) {
            return "team/roles/edit-role";
        }
        roleService.editRole(editForm.getId(), editForm.getName());
        return "redirect:/team/roles";
    }

    @GetMapping("/add")
    public String roleAddForm(HttpServletRequest request, Model model) {
        RoleAddForm addForm = new RoleAddForm();
        model.addAttribute("addForm", addForm);
        return "team/roles/add-role";
    }

    @PostMapping("/add")
    public String addRole(HttpServletRequest request,
                          @Validated @ModelAttribute("addForm") RoleAddForm form,
                          BindingResult bindingResult) {
        boolean isManager = malUtility.isManager(request);
        log.info("roleAddForm = {}", form.toString());
        if (bindingResult.hasErrors()) {
            return "team/roles/add-role";
        }
        if (!isManager) {
            bindingResult.reject("unauthorized.manager");
        }
        if (form.getAreaNames().size() != form.getDifficulties().size() ||
            form.getAreaNames().size() != form.getMinimumPeoples().size() ||
            form.getDifficulties().size() != form.getMinimumPeoples().size()) {
            bindingResult.reject("WrongRequest.wrongInput");
        }
        // TODO : validate
        validateList(form, bindingResult);
        if (bindingResult.hasErrors()) {
            return "team/roles/add-role";
        }
        TeamDto team = teamQueryService.findDtoByMemberId((Long) request.getSession().getAttribute(SessionConst.LOGIN_MEMBER));
        roleService.createRole(form.getName(), team.getId(), form.getAreaNames(),
                form.getDifficulties(), form.getMinimumPeoples(), form.getChangeable());
        return "redirect:/team/roles/change-roles";
    }

    private void validateList(RoleAddForm form, BindingResult bindingResult) {
        Validator validator = validatorFactory.getValidator();
        form.getAreaNames().forEach(name -> {
            if (!validator.validate(name).isEmpty()) {
                bindingResult.reject("areaName", "구역 이름을 확인해주세요.");
//                bindingResult.addError(new ObjectError("areaName", "구역 이름을 확인해주세요."));
            }
        });
        form.getDifficulties().forEach(diff -> {
            if (!validator.validate(diff).isEmpty()) {
                bindingResult.reject("difficulty", "난이도를 확인해주세요");
//                bindingResult.addError(new ObjectError("difficulty", "난이도를 확인해주세요"));
            }
        });
        form.getMinimumPeoples().forEach(min -> {
            if (!validator.validate(min).isEmpty()) {
                bindingResult.reject("minimumPeople", "최소인원을 확인해주세요");
//                bindingResult.addError(new ObjectError("minimumPeople", "최소인원을 확인해주세요"));
            }
        });
    }

    @RequestMapping("/{roleId}/remove")
    public String removeRole(HttpServletRequest request, @PathVariable Long roleId,
                             @RequestParam(required = false) String redirectURI) {
        boolean isManager = malUtility.isManager(request);
        if (isManager) {
            Role role = roleService.findOne(roleId);
            if (role != null) {
                roleService.delete(role);
            }
        }
        return "redirect:/team/roles" + (redirectURI == null ? "" : redirectURI);
    }
}
