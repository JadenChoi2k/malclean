/*
package Choi.clean_lottery.service;

import Choi.clean_lottery.domain.Member;
import Choi.clean_lottery.repository.MemberRepository;
import Choi.clean_lottery.web.join.MemberJoinForm;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
    public void memberJoin() throws Exception {
        // given
        MemberJoinForm form = createMemberJoinForm();

        // when
        Member joinMember = memberService.join(form);

        // then
        Member findMember = memberRepository.findOne(joinMember.getId());
        assertThat(findMember).isEqualTo(joinMember);
    }

    @Test
    public void memberLogin() throws Exception {
        // given
        Member joinMember = getJoinMember();

        // when
        Member loginMember = memberService.login(joinMember.getLoginId(), joinMember.getPassword());

        // then
        assertThat(joinMember).isEqualTo(loginMember);
    }

    @Test
    public void memberLoginFailed() throws Exception {
        // given
        Member joinMember = getJoinMember();

        // when
        String wrongId = "not a login id";
        String wrongPassword = "not a password";

        // then
        assertThrows(NoResultException.class,
                () -> memberService.login(wrongId, joinMember.getPassword()));
        assertThrows(IllegalArgumentException.class,
                ()->memberService.login(joinMember.getLoginId(), wrongPassword));
    }

    @Test
    public void test() throws Exception {
        // given

        // when

        // then

    }

    private MemberJoinForm createMemberJoinForm() {
        MemberJoinForm form = new MemberJoinForm();
        form.setName("choi");
        form.setLoginId("jkya01");
        form.setPassword("1234");
        return form;
    }

    private Member getJoinMember() {
        MemberJoinForm memberJoinForm = createMemberJoinForm();
        return memberService.join(memberJoinForm);
    }
}*/
