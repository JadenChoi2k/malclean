package Choi.clean_lottery.repository;

import Choi.clean_lottery.domain.role_change.ChangeRoleTable;
import Choi.clean_lottery.domain.team.Team;
import Choi.clean_lottery.dto.ChangeRoleTableDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChangeRoleTableRepository
        extends JpaRepository<ChangeRoleTable, Long>, ChangeRoleTableRepositoryCustom {

    ChangeRoleTable findFirstByTeamOrderByCreateDateDesc(Team team);

    @Query(value = "select t from ChangeRoleTable t" +
            " join fetch t.team tm" +
            " where t.id = :tableId")
    ChangeRoleTable getTableWithTeamAndCards(@Param(value = "tableId") Long tableId);

    @Query(value = "select new Choi.clean_lottery.dto.ChangeRoleTableDto(t)" +
            " from ChangeRoleTable t" +
            " where t.team.id = :teamId")
    ChangeRoleTableDto getTableDtoByTeamId(@Param(value = "teamId") Long teamId);

    @Query(value = "select new Choi.clean_lottery.dto.ChangeRoleTableDto(t)" +
            " from ChangeRoleTable t" +
            " where t.id = :id")
    ChangeRoleTableDto getTableDtoById(@Param(value = "id") Long id);
}
