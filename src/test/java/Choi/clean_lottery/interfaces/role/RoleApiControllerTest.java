package Choi.clean_lottery.interfaces.role;

import Choi.clean_lottery.application.role.RoleFacade;
import Choi.clean_lottery.common.response.CommonResponse;
import Choi.clean_lottery.domain.role.RoleInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleApiControllerTest {
    /**
     * spec
     * role1 = {id: 1, name: 'role 1', areaList: [], startDate: ...}
     * role1 (edited) = {id: 1, name: 'role 1 edit', areaList: [], startDate: ...}
     * role1_register_request (request body) = {
     *     name: 'role 1',
     *     teamId: 11,
     *     initAreaList: []
     * }
     * role1_edit_request (request body) = {
     *     name: 'role 1 edit'
     * }
     */
    public final RoleInfo ROLE1_INFO = RoleInfo.builder()
            .roleId(1L)
            .name("role 1")
            .areaList(new ArrayList<>())
            .startDate(LocalDate.now())
            .build();
    public final RoleInfo ROLE1_EDIT_INFO = RoleInfo.builder()
            .roleId(1L)
            .name("role 1 edit")
            .areaList(new ArrayList<>())
            .startDate(LocalDate.now())
            .build();
    public final RoleDto.RegisterRoleRequest ROLE1_REGISTER_DTO = RoleDto.RegisterRoleRequest.builder()
            .name("role 1")
            .teamId(11L)
            .initAreaList(new ArrayList<>())
            .build();
    public final RoleDto.EditRoleRequest ROLE1_EDIT_DTO = RoleDto.EditRoleRequest.builder()
            .name("role 1 edit")
            .build();
    /**
     * fields for components of spring
     */
    private RoleApiController roleApiController;
    private RoleDtoMapper roleDtoMapper = Mappers.getMapper(RoleDtoMapper.class);
    @Mock
    private RoleFacade roleFacade;

    @BeforeEach
    void before() {
        roleApiController = new RoleApiController(roleFacade, roleDtoMapper);
    }

    @Test
    void retrieve() {
        // given
        when(roleFacade.retrieve(1L)).thenReturn(ROLE1_INFO);
        CommonResponse<RoleInfo> response = roleApiController.retrieve(ROLE1_INFO.getRoleId());
        // when
        RoleInfo roleInfo = response.getData();
        // then
        assertThat(roleInfo).isEqualTo(ROLE1_INFO);
    }

    @Test
    void register() {
        // given
        when(roleFacade.register(any())).thenReturn(ROLE1_INFO);
        CommonResponse<RoleInfo> response = roleApiController.register(ROLE1_REGISTER_DTO);
        // when
        RoleInfo roleInfo = response.getData();
        // then
        assertThat(roleInfo).isEqualTo(ROLE1_INFO);
    }

    @Test
    void edit() {
        // given
        when(roleFacade.edit(any())).thenReturn(ROLE1_EDIT_INFO);
        CommonResponse<RoleInfo> response = roleApiController.edit(ROLE1_INFO.getRoleId(), ROLE1_EDIT_DTO);
        // when
        RoleInfo roleInfo = response.getData();
        // then
        assertThat(roleInfo).isEqualTo(ROLE1_EDIT_INFO);
    }

    @Test
    void detachFromTeam() {
        // given
        CommonResponse<RoleInfo> response = roleApiController.detachFromTeam(1L);
        // when
        CommonResponse.Result result = response.getResult();
        //
        assertThat(result).isEqualTo(CommonResponse.Result.SUCCESS);
    }
}