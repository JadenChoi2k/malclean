package Choi.clean_lottery.repository;

import Choi.clean_lottery.domain.Member;
import Choi.clean_lottery.domain.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public Member findById(Long id) {
        return em.find(Member.class, id);
    }

    public Optional<Member> findWithTeam(Long id) {
        return Optional.of(em.createQuery("select m from Member m" +
                " join fetch m.team t" +
                " where m.id = :id", Member.class)
                .setParameter("id", id)
                .getSingleResult());
    }

    public Member merge(Member member) {
        return em.merge(member);
    }

    public void delete(Member member) {
        em.remove(member);
    }

    public void deleteCascadeById(Long id) {
        em.createQuery("update ChangeAreaCard set changer = null" +
                        " where changer.id = :memberId")
                .setParameter("memberId", id)
                .executeUpdate();
        em.createQuery("update LotteryResult set member = null" +
                        " where member.id = :memberId")
                .setParameter("memberId", id)
                .executeUpdate();
        em.createQuery("delete from Invite where sender.id = :memberId or receiver.id = :memberId")
                .setParameter("memberId", id)
                .executeUpdate();
        // TODO 해결할 문제... lottery의 participants에서 요 멤버만 쏙 빼기.
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m")
                .getResultList();
    }
}
