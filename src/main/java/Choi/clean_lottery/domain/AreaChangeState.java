package Choi.clean_lottery.domain;

/**
 * YET : 인수 인계 하기 전
 * IN_PROCESS : 멤버가 인수인계 하기/받기 버튼을 누르면 활성화된다. 그때 changer = member가 된다.
 * 취소하면 YET으로 돌아가고 changer = null이 된다.
 * DONE : 멤버가 인수인계 하기/받기 버튼을 눌러서 IN_PROCESS로 만든 후, 완료 버튼을 누르면 DONE이 된다.
 */
public enum AreaChangeState {
    YET, IN_PROCESS, DONE
}
