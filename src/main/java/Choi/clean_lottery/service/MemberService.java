package Choi.clean_lottery.service;

import Choi.clean_lottery.domain.member.Member;
import Choi.clean_lottery.domain.team.Team;
import Choi.clean_lottery.repository.MemberRepository;
import Choi.clean_lottery.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;

    @Transactional(readOnly = true)
    public Member findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

    @Transactional(readOnly = true)
    public Optional<Member> findWithTeam(Long memberId) {
        return memberRepository.findWithTeam(memberId);
    }

    public Member join(Member member) {
        return memberRepository.save(member);
    }

    public Member merge(Member member) {
        member.setLastLoginDateTime(LocalDateTime.now());
        return memberRepository.merge(member);
    }

    public void getOutOfTeam(Long memberId) {
        Member member = memberRepository.findById(memberId);
        Team team = member.getTeam();
        // 만약 팀의 인원 수가 없거나 매니저가 탈퇴하면 remove
        List<Member> members = team.getMembers();
        if (members.size() <= 1 || team.getManager() == member) {
            teamRepository.delete(team);
        } else {
            member.getOutOfTeam();
        }
    }

    public boolean changeManager(Long prevManager, Long nextManager) {
        Member _old = memberRepository.findById(prevManager);
        Member _new = memberRepository.findById(nextManager);
        if (_old.getTeam().getManager() != _old || prevManager == nextManager) {
            return false;
        }
        if (_old.getTeam() == _new.getTeam()) {
            _old.getTeam().setManager(_new);
            return true;
        }
        return false;
    }

    public boolean kickOutMember(Long managerId, Long memberId) {
        Member manager = memberRepository.findById(managerId);
        Team managerTeam = manager.getTeam();
        if (managerTeam.getManager() != manager || managerId.equals(memberId)) {
            return false;
        }
        Member member = memberRepository.findById(memberId);
        if (managerTeam.isMemberOf(member)) {
            member.getOutOfTeam();
            return true;
        }
        return false;
    }

    public void changeTeam(Long memberId, Long teamId) throws IllegalArgumentException{
        Member member = memberRepository.findById(memberId);
        if (member == null)
            throw new IllegalArgumentException("존재하지 않는 멤버입니다.");
        if (member.getTeam() != null) {
            getOutOfTeam(memberId);
        }
        Team team = teamRepository.findOne(teamId);
        if (team == null)
            throw new IllegalArgumentException("존재하지 않는 팀입니다.");
        member.changeTeam(team);
    }

    // 회원 탈퇴
    public void deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId);
        if (member == null)
            return;
        getOutOfTeam(memberId);
        memberRepository.delete(member);
    }
}
