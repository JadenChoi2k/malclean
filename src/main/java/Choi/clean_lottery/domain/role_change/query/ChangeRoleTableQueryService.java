package Choi.clean_lottery.domain.role_change.query;

public interface ChangeRoleTableQueryService {
    // 팀에서 가장 최근의 변경 테이블의 아이디를 반환한다.
    Long findIdByTeamId(Long teamId);
}
