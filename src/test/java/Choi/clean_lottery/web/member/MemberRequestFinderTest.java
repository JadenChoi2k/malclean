package Choi.clean_lottery.web.member;

import org.junit.jupiter.api.Test;

import javax.servlet.http.Cookie;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MemberRequestFinderTest {

    @Test
    public void getKakaoUserInfoTest() throws Exception {
        // given
        Cookie cookie = new Cookie("no-authorize-access-token", "123456789");
        Cookie[] cookies = {cookie, new Cookie("cookie", "123")};

        // when
        Optional<Cookie> token = Arrays.stream(cookies)
                .filter(c -> c.getName().equals("authorize-access-token"))
                .findFirst();

        // then
        System.out.println("token.isEmpty() = " + token.isEmpty());
        System.out.println("token.isPresent() = " + token.isPresent());

    }
}