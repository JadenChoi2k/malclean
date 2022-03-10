package Choi.clean_lottery.service.query;

import Choi.clean_lottery.domain.Role;
import Choi.clean_lottery.dto.RoleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoleQueryService {

    private final EntityManager em;

    public RoleDto findDto(Long roleId) {
        Role role = em.find(Role.class, roleId);
        return new RoleDto(role);
    }
}
