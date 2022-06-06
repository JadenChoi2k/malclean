package Choi.clean_lottery.service;

import Choi.clean_lottery.domain.role.area.Area;
import Choi.clean_lottery.domain.role.Role;
import Choi.clean_lottery.domain.team.Team;
import Choi.clean_lottery.ex.NotRoleOfTeam;
import Choi.clean_lottery.repository.RoleRepository;
import Choi.clean_lottery.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;
    private final TeamRepository teamRepository;

    public Role findOne(Long roleId) {
        return roleRepository.findById(roleId);
    }

    public Role findWithAreas(Long roleId) {
        return roleRepository.findWithAreas(roleId);
    }

    // 참고 : 팀에서 getRoles를 호출하면 현재 시간 기점으로 맡고 있는 역할부터 순서대로 반환해준다.
    public List<Role> findRolesByTeam(Team team) {
        return roleRepository.findRolesByTeam(team);
    }

    public Role createRole(String name, Long teamId, List<String> areas, List<Integer> difficulties,
                           List<Integer> minimumPeoples, List<Boolean> changeable) {
        Team team = teamRepository.findOne(teamId);
        Role role = new Role(name, team);

        for (int i = 0; i < areas.size(); i++) {
            if (difficulties.get(i) < 0 || difficulties.get(i) == null) {
                difficulties.set(i, 0);
            }
            if (minimumPeoples.get(i) < 0 || minimumPeoples.get(i) == null) {
                minimumPeoples.set(i, 0);
            }
            if (changeable.get(i) == null) {
                changeable.set(i, false);
            }
            Area area = new Area(role, areas.get(i), difficulties.get(i), minimumPeoples.get(i), changeable.get(i));
            role.addArea(area);
        }

        if (team.getCurrentRole() == null) {
            team.setCurrentRole(role);
        }
        roleRepository.save(role);
        return role;
    }

    // 각 원소가 null이면 적용하지 않는다.
    public void editRole(Long roleId, String name) {
        Role role = roleRepository.findById(roleId);

        if (role == null) return;
        if (name != null) role.setName(name);
        roleRepository.merge(role);
    }

    public void changeStartDate(Long roleId, LocalDate startDate) {
        Role role = roleRepository.findById(roleId);
        role.setStartDate(startDate);
    }

    /**
     * 팀의 역할의 순서를 수정할 수 있는 페이지를 제공해줄 것이다.
     * @param teamId 역할의 순서를 수정하는 팀의 아이디
     * @param roleIds 순서대로 배정된 역할의 아이디
     */
    public void changeSequence(Long teamId, List<Long> roleIds) {
        Team team = teamRepository.findOne(teamId);

        List<Role> roles = new ArrayList<>();
        for (Long roleId : roleIds) {
            Role role = roleRepository.findById(roleId);

            if (!team.isRoleOf(role))
                throw new NotRoleOfTeam("팀에 없는 역할입니다.");

            roles.add(role);
        }

        team.setCurrentRole(roles.get(0));
    }

    public void delete(Role role) {
        roleRepository.delete(role);
    }
}
