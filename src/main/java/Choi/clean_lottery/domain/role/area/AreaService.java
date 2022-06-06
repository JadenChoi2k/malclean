package Choi.clean_lottery.domain.role.area;

public interface AreaService {
    AreaInfo registerArea(AreaCommand.RegisterAreaRequest registerAreaRequest);

    AreaInfo editArea(AreaCommand.EditAreaRequest editAreaRequest);

    void detachFromRole(Long areaId);

    AreaInfo retrieve(Long areaId);
}
