package Choi.clean_lottery.domain.member;

public interface MemberReader {
    Member getMemberById(Long memberId);

    boolean exists(Long memberId);
}
