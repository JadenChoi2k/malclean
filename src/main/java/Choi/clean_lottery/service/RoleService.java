package Choi.clean_lottery.service;

import Choi.clean_lottery.domain.Area;
import Choi.clean_lottery.domain.Role;
import Choi.clean_lottery.domain.Team;
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
                           List<Integer> minimumPeoples,Integer changeDate) {
        Team team = teamRepository.findOne(teamId);
        Role role = new Role(name, team);

        for (int i = 0; i < areas.size(); i++) {
            if (difficulties.get(i) < 0 || difficulties.get(i) == null) {
                difficulties.set(i, 0);
            }
            if (minimumPeoples.get(i) < 0 || minimumPeoples.get(i) == null) {
                minimumPeoples.set(i, 0);
            }
            Area area = new Area(role, areas.get(i), difficulties.get(i), minimumPeoples.get(i));
            role.addArea(area);
        }

        role.setDuration(changeDate);

        List<Role> roles = team.getRolesBySequence();
        if (!roles.isEmpty()) {
            Role prev = roles.get(roles.size() - 1);
            Role next = roles.get(0);
            role.setPrev(prev);
            prev.setNext(role);
            role.setNext(next);
            next.setPrev(role);
        }

        roleRepository.save(role);
        return role;
    }

    // 각 원소가 null이면 적용하지 않는다.
    public void editRole(Long roleId, String name, Integer changeDate) {
        Role role = roleRepository.findById(roleId);

        if (role == null) return;
        if (name != null) role.setName(name);
        if (changeDate != null && changeDate != 0) role.setDuration(changeDate);
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

        // 만약 currentRole이 없으면 삽입해준다.
        // TODO 이는 더 고민해봐야 할 문제다. 맨 처음 lottery를 돌릴 때 currentRole이 없으므로 rolesChangePage로 오게 하나?
        // 일단 그렇게 하자.
        team.setCurrentRole(roles.get(0));

        // 순서를 삽입해준다.
        for (int i = 0; i < roles.size(); i++) {
            roles.get(i).setNext(roles.get((i + 1) % roles.size()));
            roles.get(i).setPrev(roles.get((i - 1 + roles.size()) % roles.size()));
        }
    }

    public void delete(Role role) {
        roleRepository.delete(role);
    }
}
