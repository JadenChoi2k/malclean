package Choi.clean_lottery.domain.team;

public interface TeamStore {
    Team store(Team team);

    void delete(Long teamId);
}
