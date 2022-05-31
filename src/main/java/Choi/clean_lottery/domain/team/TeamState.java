package Choi.clean_lottery.domain.team;

/**
 * ON : 정상적으로 작동하는 팀
 * CHANGING_ROLE : 인수인계 날 역할을 바꾸는 중. 역할에 대한 조작 등을 할 수 없다.
 * CLEANING : 청소하는 중 (기능 추가 예정)
 */
public enum TeamState {
    ON, CHANGING_ROLE, CLEANING
}
