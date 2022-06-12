package Choi.clean_lottery.interfaces.role_changing;

import Choi.clean_lottery.application.member.MemberFacade;
import Choi.clean_lottery.application.role_change.ChangeRoleTableFacade;
import Choi.clean_lottery.common.response.CommonResponse;
import Choi.clean_lottery.common.response.ErrorCode;
import Choi.clean_lottery.domain.member.query.MemberQueryInfo;
import Choi.clean_lottery.domain.role_change.ChangeRoleTableInfo;
import Choi.clean_lottery.domain.role_change.card.ChangeAreaCardCommand;
import Choi.clean_lottery.web.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController("/api/v1/role-changing")
@RequiredArgsConstructor
public class RoleChangingApiController {
    private final ChangeRoleTableFacade changeRoleTableFacade;
    private final MemberFacade memberFacade;
    private final RoleChangingDtoMapper dtoMapper;

    @GetMapping("/{tableId}")
    public CommonResponse getChangeRoleTable(HttpServletRequest request,
                                             @PathVariable Long tableId) {
        Object memberId = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return CommonResponse.fail(ErrorCode.COMMON_INVALID_ACCESS);
        }
        MemberQueryInfo.WithTeam withTeam = memberFacade.withTeam((Long) memberId);
        ChangeRoleTableInfo.Main tableInfo = changeRoleTableFacade.retrieve(tableId);
        if (!withTeam.getTeamInfo().getTeamId().equals(tableInfo.getTeamId())) {
            return CommonResponse.fail(ErrorCode.COMMON_INVALID_ACCESS);
        }
        return CommonResponse.success(dtoMapper.of(tableInfo));
    }

    @PostMapping("/{tableId}/{areaCardId}/register")
    public CommonResponse getInChargeOf(HttpServletRequest request, @PathVariable Long tableId,
                                        @PathVariable Long areaCardId) {
        Object memberId = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return CommonResponse.fail(ErrorCode.COMMON_INVALID_ACCESS);
        }
        MemberQueryInfo.WithTeam withTeam = memberFacade.withTeam((Long) memberId);
        ChangeRoleTableInfo.Main tableInfo = changeRoleTableFacade.retrieve(tableId);
        if (!tableInfo.getTeamId().equals(withTeam.getTeamInfo().getTeamId())) {
            return CommonResponse.fail("팀의 테이블이 아닙니다.", ErrorCode.COMMON_INVALID_PARAMETER.name());
        }
        changeRoleTableFacade.areaCardRegisterChanger(ChangeAreaCardCommand.RegisterChangerRequest.builder()
                .memberId((Long) memberId)
                .areaCardId(areaCardId)
                .build());
        return CommonResponse.success(null, "ok");
    }

    @DeleteMapping("/area/{areaCardId}")
    public CommonResponse detachChanger(HttpServletRequest request, @PathVariable Long areaCardId) {
        Object memberId = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return CommonResponse.fail(ErrorCode.COMMON_INVALID_ACCESS);
        }
        ChangeRoleTableInfo.AreaCardInfo areaCardInfo = changeRoleTableFacade.areaCardRetrieve(areaCardId);
        if (!areaCardInfo.getMember().getMemberId().equals(memberId)) {
            return CommonResponse.fail(ErrorCode.COMMON_INVALID_ACCESS);
        }
        changeRoleTableFacade.areaCardDetachChanger(areaCardId);
        return CommonResponse.success(null, "ok");
    }

    @PostMapping("/area/{areaCardId}")
    public CommonResponse completeArea(HttpServletRequest request, @PathVariable Long areaCardId) {
        Object memberId = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return CommonResponse.fail(ErrorCode.COMMON_INVALID_ACCESS);
        }
        ChangeRoleTableInfo.AreaCardInfo areaCardInfo = changeRoleTableFacade.areaCardRetrieve(areaCardId);
        if (!areaCardInfo.getMember().getMemberId().equals(memberId)) {
            return CommonResponse.fail(ErrorCode.COMMON_INVALID_ACCESS);
        }
        changeRoleTableFacade.areaCardCompleteChanging(areaCardId);
        return CommonResponse.success(null, "ok");
    }

    @RequestMapping("/{tableId}/complete")
    public CommonResponse completeRoleChanging(HttpServletRequest request, @PathVariable Long tableId) {
        Object memberId = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return CommonResponse.fail(ErrorCode.COMMON_INVALID_ACCESS);
        }
        MemberQueryInfo.WithTeam withTeam = memberFacade.withTeam((Long) memberId);
        ChangeRoleTableInfo.Main tableInfo = changeRoleTableFacade.retrieve(tableId);
        if (!withTeam.getMemberInfo().getIsManager()) {
            return CommonResponse.fail(ErrorCode.COMMON_INVALID_ACCESS);
        }
        if (!withTeam.getTeamInfo().getTeamId().equals(tableInfo.getTeamId())) {
            return CommonResponse.fail(ErrorCode.COMMON_INVALID_ACCESS);
        }
        changeRoleTableFacade.completeChanging(tableId);
        return CommonResponse.success(null, "ok");
    }
}
