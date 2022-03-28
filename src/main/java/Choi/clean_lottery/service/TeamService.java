package Choi.clean_lottery.service;

import Choi.clean_lottery.domain.Member;
import Choi.clean_lottery.domain.Role;
import Choi.clean_lottery.domain.Team;
import Choi.clean_lottery.ex.NotMemberOfTeam;
import Choi.clean_lottery.repository.MemberRepository;
import Choi.clean_lottery.repository.RoleRepository;
import Choi.clean_lottery.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Team findOne(Long teamId) {
        return teamRepository.findOne(teamId);
    }

    public void save(Team team) {
        teamRepository.save(team);
    }

    public void merge(Team team) {
        teamRepository.merge(team);
    }

    public List<Member> findMembersByTeamId(Long teamId) {
        return teamRepository.findMembers(teamId);
    }

    private Team findTeam(Long teamId) throws IllegalArgumentException {
        Team team = teamRepository.findOne(teamId);
        if (team == null) {
            throw new IllegalArgumentException("존재하지 않는 팀입니다.");
        }
        return team;
    }

    public Long createTeam(Long managerId, String teamName) {
        Member manager = memberRepository.findById(managerId);
        Team team = new Team(teamName, manager);
        teamRepository.save(team);
        return team.getId();
    }

    // 지금은 초대/수락 방식으로 운영한다.
    private void addMember(Member member, Long teamId) throws IllegalArgumentException {
        Team team = findTeam(teamId);
        team.addMember(member);
    }

    // 탈퇴는 매니저(강퇴) 또는 본인만 가능하다.
    public void kickOutMember(Member member, Long teamId) throws IllegalArgumentException {
        Team team = findTeam(teamId);
        if (!team.isMemberOf(member)) {
            throw new NotMemberOfTeam("팀의 멤버가 아닙니다.");
        }
        team.kickOutMember(member);
    }

    public void addRole(Role role, Long teamId) throws IllegalArgumentException {
        Team team = findTeam(teamId);
        team.addRole(role);
        roleRepository.save(role);
    }

    public void subRole(Role role, Long teamId) throws IllegalArgumentException {
        Team team = findTeam(teamId);
        team.subRole(role); // 필요한가??
        roleRepository.delete(role);
    }
}
