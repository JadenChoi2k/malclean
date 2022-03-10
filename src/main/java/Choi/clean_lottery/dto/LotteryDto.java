package Choi.clean_lottery.dto;

import Choi.clean_lottery.domain.Lottery;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class LotteryDto {
    private Long id;
    private String name;
    private List<MemberDto> participants;
    private RoleDto role;
    private LocalDateTime lastRoleDateTime;
    private List<LotteryResultDto> result;

    public LotteryDto(Lottery lottery) {
        id = lottery.getId();
        name = lottery.getName();
        participants = lottery.getParticipants().stream().map(MemberDto::new).collect(Collectors.toList());
        role = new RoleDto(lottery.getRole());
        lastRoleDateTime = lottery.getLastRoleDateTime();
        result = lottery.getResults().stream().map(LotteryResultDto::new).collect(Collectors.toList());
    }

    public List<LotteryResultDto> findResultByMemberId(Long memberId) {
        List<LotteryResultDto> ret = new ArrayList<>();
        for (LotteryResultDto lotteryResultDto : result) {
            if (lotteryResultDto.getMember().getId().equals(memberId))
                ret.add(lotteryResultDto);
        }
        return ret;
    }

    public Map<String, List<MemberDto>> getCompanions(List<LotteryResultDto> lotteryResult) {
        Map<String, List<MemberDto>> ret = new HashMap<>();
        for (LotteryResultDto lotteryResultDto : lotteryResult) {
            for (LotteryResultDto resultDto : result) {
                // 구역이 같고 자신이 아닐 시 멤버를 삽입
                if (resultDto.getArea().equals(lotteryResultDto.getArea()) &&
                        !resultDto.getMember().equals(lotteryResultDto.getMember())) {
                    String areaName = lotteryResultDto.getArea().getName();
                    // 리스트가 없으면 생성
                    if (!ret.containsKey(areaName)) {
                        ret.put(areaName, new ArrayList<>());
                    }
                    List<MemberDto> companions = ret.get(areaName);
                    companions.add(resultDto.getMember());
                }
            }
        }
        return ret;
    }
}
