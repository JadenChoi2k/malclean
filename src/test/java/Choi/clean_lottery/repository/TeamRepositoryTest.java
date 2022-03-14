package Choi.clean_lottery.repository;

import Choi.clean_lottery.domain.DomainTestHelper;
import Choi.clean_lottery.domain.Member;
import Choi.clean_lottery.domain.Team;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootTest
@Transactional
public class TeamRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;
    @PersistenceContext
    private EntityManager em;

    @Test
    public void 팀_불러오기_postLoad() {
        // given
        Member manager1 = new Member(1L, "manager1", null, null, null);
        Team team1 = new Team("team1", manager1);
        em.persist(team1);
        em.flush();
        em.clear();
        // when
        Team findTeam = em.find(Team.class, team1.getId());
        // then

    }

    @Test
    public void 팀_저장() throws Exception {
        // given
        Member manager1 = new Member(1L, "manager1", null, null, null);
        Team team1 = new Team("team1", manager1);
        Member manager2 = new Member(2L, "manager2", null, null, null);
        Team team2 = new Team("team2", manager2);
        teamRepository.save(team1);
        teamRepository.save(team2);

        // when
        List<Team> all = teamRepository.findAll();

        // then
        Assertions.assertThat(all).hasSize(2);
        for (Team team : all) {
            System.out.println("team.getName() = " + team.getName());
        }
    }
    
    @Test
    public void 팀_가져오기() throws Exception {
        // given
        List<Team> all = teamRepository.findAll();

        // then
        for (Team team : all) {
            System.out.println("team.getName() = " + team.getName());
        }

    }
}
