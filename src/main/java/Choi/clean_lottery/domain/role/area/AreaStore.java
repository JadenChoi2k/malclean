package Choi.clean_lottery.domain.role.area;

public interface AreaStore {
    Area store(Area area);

    void detachFromRole(Long areaId);
}
