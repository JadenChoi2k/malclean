package Choi.clean_lottery.interfaces.role;

import Choi.clean_lottery.application.member.MemberFacade;
import Choi.clean_lottery.application.role.RoleFacade;
import Choi.clean_lottery.domain.member.MemberInfo;
import Choi.clean_lottery.domain.member.query.MemberQueryInfo;
import Choi.clean_lottery.domain.role.RoleCommand;
import Choi.clean_lottery.domain.role.RoleInfo;
import Choi.clean_lottery.domain.role.area.AreaCommand;
import Choi.clean_lottery.interfaces.role.form.RoleAddForm;
import Choi.clean_lottery.interfaces.role.form.RoleEditForm;
import Choi.clean_lottery.common.constant.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping("/team/roles")
@Controller
@RequiredArgsConstructor
public class RoleController {
    private final RoleFacade roleFacade;
    private final MemberFacade memberFacade;

    @GetMapping
    public String roleHome(HttpServletRequest request, Model model) {
        Object memberId = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return "redirect:/";
        }
        MemberQueryInfo.WithTeam withTeam = memberFacade.withTeam((Long) memberId);
        if (withTeam.getTeamInfo() == null) {
            return "redirect:/";
        }
        List<RoleInfo> roles = withTeam.getTeamInfo().getRoles();
        if (!roles.isEmpty()) model.addAttribute("currentRole", withTeam.getTeamInfo().getCurrentRole());
        model.addAttribute("roles", roles);
        model.addAttribute("isManager", withTeam.getMemberInfo().getIsManager());

        return "team/roles/role-home";
    }

    @GetMapping("/{roleId}/edit")
    public String roleEditForm(HttpServletRequest request, @PathVariable Long roleId,
                               Model model) {
        Object memberId = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return roleHome(request, model);
        }
        MemberInfo memberInfo = memberFacade.retrieveMemberInfo((Long) memberId);
        if (!memberInfo.getIsManager()) {
            return roleHome(request, model);
        }
        RoleInfo roleInfo = roleFacade.retrieve(roleId);
        model.addAttribute("areas", roleInfo.getAreaList());
        model.addAttribute("editForm", RoleEditForm.builder()
                .id(roleInfo.getRoleId())
                .name(roleInfo.getName())
                .build());
        return "team/roles/edit-role";
    }

    @PostMapping("/{roleId}/edit")
    public String edit(HttpServletRequest request, @ModelAttribute("editForm") @Valid RoleEditForm editForm,
                       BindingResult bindingResult, @PathVariable Long roleId, Model model) {
        Object memberId = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return "redirect:/";
        }
        MemberInfo memberInfo = memberFacade.retrieveMemberInfo((Long) memberId);
        if (!memberInfo.getIsManager()) {
            bindingResult.reject("unauthorized.manager");
        }
        RoleInfo roleInfo = roleFacade.retrieve(roleId);
        if (roleInfo == null) {
            bindingResult.reject("WrongRequest");
        }
        if (roleInfo != null && !roleInfo.getRoleId().equals(editForm.getId())) {
            bindingResult.reject("WrongRequest.wrongId.role");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("areas", roleInfo.getAreaList());
            return "team/roles/edit-role";
        }
        roleFacade.edit(RoleCommand.EditRoleRequest.builder()
                .id(roleId)
                .name(editForm.getName())
                .build());
        return "redirect:/team/roles";
    }

    @GetMapping("/add")
    public String roleAddForm(HttpServletRequest request, Model model) {
        Object memberId = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return "redirect:/";
        }
        MemberInfo memberInfo = memberFacade.retrieveMemberInfo((Long) memberId);
        if (!memberInfo.getIsManager()) {
            return roleHome(request, model);
        }
        RoleAddForm addForm = new RoleAddForm();
        model.addAttribute("addForm", addForm);
        return "team/roles/add-role";
    }

    @PostMapping("/add")
    public String addRole(HttpServletRequest request,
                          @ModelAttribute("addForm") RoleAddForm form,
                          BindingResult bindingResult) {
        Object memberId = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return "redirect:/";
        }
        MemberQueryInfo.WithTeam withTeam = memberFacade.withTeam((Long) memberId);
        if (!withTeam.getMemberInfo().getIsManager()) {
            bindingResult.reject("unauthorized.manager");
        }
        if (form.getAreaNames().size() != form.getDifficulties().size() ||
                form.getAreaNames().size() != form.getMinimumPeoples().size() ||
                form.getDifficulties().size() != form.getMinimumPeoples().size()) {
            bindingResult.reject("WrongRequest.wrongInput");
        }
        if (bindingResult.hasErrors()) {
            return "team/roles/add-role";
        }
        roleFacade.register(RoleCommand.RegisterRoleRequest.builder()
                .teamId(withTeam.getTeamInfo().getTeamId())
                .name(form.getName())
                .areaRequestList(extractAreaRegisterRequestList(form))
                .build());
        return "redirect:/team/roles";
    }

    private List<AreaCommand.RegisterAreaRequest> extractAreaRegisterRequestList(RoleAddForm form) {
        List<AreaCommand.RegisterAreaRequest> ret = new ArrayList<>();
        for (int i = 0; i < form.getAreaNames().size(); i++) {
            ret.add(AreaCommand.RegisterAreaRequest.builder()
                            .areaName(form.getAreaNames().get(i))
                            .difficulty(form.getDifficulties().get(i))
                            .minimumPeople(form.getMinimumPeoples().get(i))
                            .changeable(form.getChangeable().get(i) != null && form.getChangeable().get(i))
                    .build());
        }
        return ret;
    }

    @RequestMapping("/{roleId}/remove")
    public String removeRole(HttpServletRequest request, @PathVariable Long roleId,
                             @RequestParam(required = false) String redirectURI) {
        Object memberId = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return "redirect:/";
        }
        if (memberFacade.retrieveMemberInfo((Long) memberId).getIsManager()) {
            roleFacade.detachFromTeam(roleId);
        }
        return "redirect:/team/roles" + Optional.of(redirectURI).orElse("");
    }
}
