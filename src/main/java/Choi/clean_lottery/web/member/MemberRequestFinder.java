package Choi.clean_lottery.web.member;

import Choi.clean_lottery.web.kakaoapi.KakaoApiHelper;
import Choi.clean_lottery.web.kakaoapi.KakaoUserInfo;
import Choi.clean_lottery.web.kakaoapi.KakaoUserTokenInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MemberRequestFinder {

    private final KakaoApiHelper kakaoApiHelper;

    public boolean isValidToken(HttpServletRequest request) throws JsonProcessingException {
        Cookie[] cookies = request.getCookies();
        Optional<Cookie> token = Arrays.stream(cookies)
                .filter(c -> c.getName().equals("authorize-access-token"))
                .findFirst();

        if (token.isEmpty()) {
            return false;
        }

        return kakaoApiHelper.isValidToken(token.get().getValue());
    }

    public Long getUserId(HttpServletRequest request) throws JsonProcessingException {
        Cookie[] cookies = request.getCookies();
        Optional<Cookie> token = Arrays.stream(cookies)
                .filter(c -> c.getName().equals("authorize-access-token"))
                .findFirst();

        if (token.isEmpty()) {
            return null;
        }

        return kakaoApiHelper.getUserIdByToken(token.get().getValue());
    }

    public KakaoUserInfo getKakaoUserInfo(HttpServletRequest request) throws JsonProcessingException {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        try {
            Optional<Cookie> token = Arrays.stream(cookies)
                    .filter(c -> c.getName().equals("authorize-access-token"))
                    .findFirst();
            if (token.isEmpty()) {
                return null;
            }
            return getKakaoUserInfo(token.get().getValue());
        } catch (NoSuchElementException ex) {
            // 토큰을 못 찾았을 경우.
            return null;
        }
    }

    public KakaoUserInfo getKakaoUserInfo(String token) throws JsonProcessingException {
        Long userId = kakaoApiHelper.getUserIdByToken(token);
        if (userId == null) {
            return null;
        }
        return kakaoApiHelper.getUserInfoByUserId(userId);
    }
}
