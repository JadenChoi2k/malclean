package Choi.clean_lottery.domain.lottery.query;

import Choi.clean_lottery.domain.lottery.Lottery;
import Choi.clean_lottery.domain.lottery.result.LotteryResult;
import Choi.clean_lottery.domain.member.MemberInfo;
import Choi.clean_lottery.domain.role.RoleInfo;
import Choi.clean_lottery.domain.role.area.AreaInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
public class LotteryQueryInfo {
    private Long id;
    private String name;
    private RoleInfo role;
    private LocalDateTime lastRoleDateTime;
    private List<LotteryResultQueryInfo> result;

    @Getter
    public static class LotteryResultQueryInfo {
        private MemberInfo member;
        private AreaInfo area;

        public LotteryResultQueryInfo(LotteryResult lotteryResult) {
            this.member = new MemberInfo(lotteryResult.getMember());
            this.area = new AreaInfo(lotteryResult.getArea());
        }
    }

    public LotteryQueryInfo(Lottery lottery) {
        this.id = lottery.getId();
        this.name = lottery.getName();
        this.role = new RoleInfo(lottery.getRole());
        this.lastRoleDateTime = lottery.getLastModifiedDate();
        this.result = lottery.getResults().stream()
                .map(LotteryResultQueryInfo::new)
                .collect(Collectors.toList());
    }

    public List<String> getMyAreaNameList(Long memberId) {
        return getResult().stream()
                .filter(lr -> lr.getMember().getMemberId().equals(memberId))
                .map(LotteryQueryInfo.LotteryResultQueryInfo::getArea)
                .map(AreaInfo::getName)
                .collect(Collectors.toList());
    }

    public Map<String, List<MemberInfo>> getMyCompanionList(Long memberId) {
        Map<String, List<MemberInfo>> areaNameToMemberInfoList = getAreaNameToMemberInfoList();
        return getMyAreaNameList(memberId).stream()
                .collect(Collectors.toMap(Function.identity(), areaNameToMemberInfoList::get));
    }

    private Map<String, List<MemberInfo>> getAreaNameToMemberInfoList() {
        Map<String, List<MemberInfo>> ret = new HashMap<>();
        getResult().forEach(lr -> {
            String areaName = lr.getArea().getName();
            if (!ret.containsKey(areaName)) {
                ret.put(areaName, new ArrayList<>());
            }
            ret.get(areaName).add(lr.getMember());
        });
        return ret;
    }
}
