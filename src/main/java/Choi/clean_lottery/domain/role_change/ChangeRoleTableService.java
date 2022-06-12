package Choi.clean_lottery.domain.role_change;

public interface ChangeRoleTableService {
    ChangeRoleTableInfo.Main register(ChangeRoleTableCommand.RegisterTableRequest registerTableRequest);

    ChangeRoleTableInfo.Main retrieve(Long tableId);

    void completeChanging(Long tableId);
}
