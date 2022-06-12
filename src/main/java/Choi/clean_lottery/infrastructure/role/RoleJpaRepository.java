package Choi.clean_lottery.infrastructure.role;

import Choi.clean_lottery.domain.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleJpaRepository extends JpaRepository<Role, Long> {
}
