package Choi.clean_lottery.service;

import Choi.clean_lottery.domain.*;
import Choi.clean_lottery.dto.LotteryDto;
import Choi.clean_lottery.ex.NotMemberOfTeam;
import Choi.clean_lottery.ex.NotRoleOfTeam;
import Choi.clean_lottery.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LotteryService {

    private final LotteryRepository lotteryRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final AreaRepository areaRepository;

    @Transactional(readOnly = true)
    public Lottery findOne(Long lotteryId) {
        return lotteryRepository.findById(lotteryId);
    }

    @Transactional(readOnly = true)
    public List<Lottery> paging(Long teamId, int page, int count) {
        return lotteryRepository.pagingInTeam(teamId, page, count);
    }

    @Transactional(readOnly = true)
    public Long getTotalSizeInTeam(Long teamId) {
        return lotteryRepository.getTotalSizeInTeam(teamId);
    }

    /*@Transactional(readOnly = true)
    public List<Lottery> findAll(Long teamId) {
        return lotteryRepository.findAllInTeam(teamId);
    }*/

    public Lottery drawLottery(String name, Long teamId, Set<Long> participantIds, Long roleId, List<Long> pick)
            throws IllegalArgumentException {
        Team team = teamRepository.findOne(teamId);
        if (team == null)
            throw new IllegalArgumentException("존재하지 않는 팀입니다.");

        List<Member> participants = getMembers(participantIds, team);

        Role role = roleRepository.findById(roleId);
        if (role == null)
            throw new IllegalArgumentException("존재하지 않는 역할입니다.");
        if (!team.isRoleOf(role))
            throw new NotRoleOfTeam("팀의 역할이 아닙니다.");

        Lottery lottery = Lottery.createLottery(name, team, participants, role);
        lottery.drawLottery(pick.stream().map(id -> areaRepository.findById(id).orElse(null)).collect(Collectors.toList()));
        lotteryRepository.save(lottery);

        return lottery;
    }

    public Lottery redraw(Long lotteryId, Long teamId, Set<Long> participantIds,
                          Long roleId, List<Long> pick) {
        Lottery lottery = findOne(lotteryId);
        Team team = teamRepository.findOne(teamId);
        if (lottery == null || team == null)
            return null;
        List<Member> participants = getMembers(participantIds, team);

        Role role = roleRepository.findById(roleId);
        lottery.redrawLottery(participants, role,
                pick.stream().map(id -> areaRepository.findById(id).orElse(null)).collect(Collectors.toList()));
        return lottery;
    }

    private List<Member> getMembers(Set<Long> participantIds, Team team) {
        List<Member> participants = new ArrayList<>();
        for (Long participantId : participantIds) {
            Member participant = memberRepository.findById(participantId);
            if (participant == null)
                throw new IllegalArgumentException("존재하지 않는 멤버입니다.");
            if (!team.isMemberOf(participant))
                throw new NotMemberOfTeam("팀의 멤버가 아닙니다.");
            participants.add(participant);
        }
        return participants;
    }
}
