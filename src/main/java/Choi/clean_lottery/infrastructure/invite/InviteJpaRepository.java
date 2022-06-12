package Choi.clean_lottery.infrastructure.invite;

import Choi.clean_lottery.domain.invite.Invite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InviteJpaRepository extends JpaRepository<Invite, String> {
}
