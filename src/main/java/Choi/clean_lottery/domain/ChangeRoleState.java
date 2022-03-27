package Choi.clean_lottery.domain;

/**
 * IN_PROCESS : 초기 상태.
 * DONE : 인수인계 완료. 완료 시 현재 역할이 바뀐다.
 */
public enum ChangeRoleState {
    IN_PROCESS, DONE
}
