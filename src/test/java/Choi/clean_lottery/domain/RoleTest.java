package Choi.clean_lottery.domain;

import Choi.clean_lottery.domain.role.area.Area;
import Choi.clean_lottery.domain.role.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class RoleTest {
    private final DomainTestHelper testHelper = new DomainTestHelper();

    @Test
    public void create_test() throws Exception {
        // given
        List<Area> areas = testHelper.createAreas();
        Role role = testHelper.createRole(
                areas,
                1L,
                "role"
        );
        // when

        // then
        areas.forEach(a -> Assertions.assertEquals(a.getRole(), role));
        Assertions.assertIterableEquals(areas, role.getAreas());
        Assertions.assertEquals(role.getId(), 1L);
        Assertions.assertEquals(role.getName(), "role");
    }

    @Test
    public void add_area_test() throws Exception {
        // given
        List<Area> areas = testHelper.createAreas();
        Role role = testHelper.createRole(
                areas,
                1L,
                "role"
        );
        // when
        Area area = new Area(null, "add area", 0, 0);
        areas.add(area);
        role.addArea(area);
        // then
        Assertions.assertEquals(area.getRole(), role);
        Assertions.assertEquals(areas, role.getAreas());
    }

    @Test
    public void sub_area_test() throws Exception {
        // given
        List<Area> areas = testHelper.createAreas();
        Role role = testHelper.createRole(
                areas,
                1L,
                "role"
        );
        // when
        Area area = areas.get(areas.size() - 1);
        areas.remove(area);
        role.subArea(area);
        // then
        Assertions.assertNull(area.getRole());
        Assertions.assertEquals(areas, role.getAreas());
    }
}
