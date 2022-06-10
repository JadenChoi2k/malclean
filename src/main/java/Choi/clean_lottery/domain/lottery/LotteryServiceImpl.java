package Choi.clean_lottery.domain.lottery;

import Choi.clean_lottery.domain.role.area.Area;
import Choi.clean_lottery.domain.role.area.AreaReader;
import Choi.clean_lottery.domain.member.Member;
import Choi.clean_lottery.domain.member.MemberReader;
import Choi.clean_lottery.domain.role.Role;
import Choi.clean_lottery.domain.role.RoleReader;
import Choi.clean_lottery.domain.team.Team;
import Choi.clean_lottery.domain.team.TeamReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LotteryServiceImpl implements LotteryService {
    private final LotteryReader lotteryReader;
    private final LotteryStore lotteryStore;
    private final TeamReader teamReader;
    private final RoleReader roleReader;
    private final MemberReader memberReader;
    private final AreaReader areaReader;

    @Override
    @Transactional(readOnly = true)
    public LotteryInfo.Main retrieve(Long lotteryId) {
        return new LotteryInfo.Main(lotteryReader.getLotteryById(lotteryId));
    }

    @Override
    @Transactional
    public LotteryInfo.Main drawLottery(LotteryCommand.DrawLotteryRequest drawLotteryRequest) {
        // get team
        Team team = teamReader.getTeamById(drawLotteryRequest.getTeamId());
        if (team == null) throw new IllegalArgumentException("존재하지 않는 팀입니다.");
        // extract participants
        List<Member> participants = extractParticipants(team, drawLotteryRequest.getParticipantIdList());
        // get role
        Role role = getRoleById(team, drawLotteryRequest.getRoleId());
        // create lottery
        Lottery lottery = Lottery.createLottery(drawLotteryRequest.getLotteryName(), team, role);
        // draw lottery
        lottery.drawLottery(
                extractPickAreaList(role, drawLotteryRequest.getPickAreaIdList()),
                participants
        );
        lotteryStore.store(lottery);
        return new LotteryInfo.Main(lottery);
    }

    private List<Member> extractParticipants(Team team, List<Long> participantIdList) throws IllegalArgumentException {
        return participantIdList.stream()
                .map(memberId -> {
                    Member member = memberReader.getMemberById(memberId);
                    if (member == null) throw new IllegalArgumentException("존재하지 않는 멤버 입니다.");
                    if (!team.isMemberOf(member)) throw new IllegalArgumentException("팀에 없는 멤버입니다.");
                    return member;
                })
                .collect(Collectors.toList());
    }

    private Role getRoleById(Team team, Long roleId) {
        Role role = roleReader.getRoleById(roleId);
        if (role == null) throw new IllegalArgumentException("존재하지 않는 역할입니다.");
        if (!team.isRoleOf(role)) throw new IllegalArgumentException("팀의 역할이 아닙니다.");
        return role;
    }

    private List<Area> extractPickAreaList(Role role, List<Long> pickAreaIdList) {
        return pickAreaIdList.stream()
                .map(areaId -> {
                    Area area = areaReader.getAreaById(areaId);
                    if (area == null) throw new IllegalArgumentException("존재하지 않는 구역입니다.");
                    if (!role.isAreaOf(area)) throw new IllegalArgumentException("역할에 존재하지 않는 구역입니다.");
                    return area;
                })
                .collect(Collectors.toList());
    }
}
