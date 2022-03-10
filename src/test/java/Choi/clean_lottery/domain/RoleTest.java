package Choi.clean_lottery.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class RoleTest {
    private final DomainTestHelper testHelper = new DomainTestHelper();

    @Test
    public void 역할_상태_얻기() throws Exception {
        // given
        Role role = new Role();
        // when
        LocalDate now = LocalDate.now();
        LocalDate day7 = now.plusDays(7);
        LocalDate day13 = now.plusDays(14 - 1);
        LocalDate day21 = now.plusDays(21);
        // then
        assertEquals(role.getRoleState(now), RoleState.ON);
        role.setStartDate(now);
        role.setDuration(14);
        assertEquals(role.getRoleState(now), RoleState.ON);
        assertEquals(role.getRoleState(day7), RoleState.ON);
        assertEquals(role.getRoleState(day13), RoleState.CHANGE_DAY);
        assertEquals(role.getRoleState(day21), RoleState.ALREADY_CHANGED);
    }
}
