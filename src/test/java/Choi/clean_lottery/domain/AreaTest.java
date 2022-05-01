package Choi.clean_lottery.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AreaTest {

    @Test
    public void sorting_test() {
        Area a1 = new Area(null, "a1", 5, 0);
        Area a2 = new Area(null, "a2", 2, 0);
        Area a3 = new Area(null, "a3", 10, 0);
        List<Area> areas = new ArrayList<>();
        areas.add(a1);
        areas.add(a2);
        areas.add(a3);
        // difficulty {5, 2, 10}

        areas.sort(Comparator.comparing(Area::getDifficulty).reversed());
        // expected {10, 5, 2}
        for (int i = 0; i < areas.size() - 1; i++) {
            if (areas.get(i).getDifficulty() < areas.get(i + 1).getDifficulty()) {
                Assertions.fail();
            }
        }
    }

    @Test
    public void change_attribute_test() throws Exception {
        // given
        Area area = new Area(null, "area to change", 10, 2, true);
        // when
        area.changeAttribute("changed", 6, 1, false);
        // then
        Assertions.assertEquals(area.getName(), "changed");
        Assertions.assertEquals(area.getDifficulty(), 6);
        Assertions.assertEquals(area.getMinimumPeople(), 1);
        Assertions.assertEquals(area.getChangeable(), false);
    }
}
