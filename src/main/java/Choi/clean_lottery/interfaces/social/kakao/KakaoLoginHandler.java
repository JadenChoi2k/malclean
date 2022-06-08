package Choi.clean_lottery.interfaces.social.kakao;

import Choi.clean_lottery.interfaces.social.SocialLoginHandler;
import Choi.clean_lottery.interfaces.social.SocialUserInfo;
import Choi.clean_lottery.web.member.MemberRequestFinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoLoginHandler implements SocialLoginHandler {
    private static final String NICKNAME_PROPERTY = "nickname";
    private static final String IMAGE_PROPERTY = "profile_image_url";

    private final KakaoApiHelper kakaoApiHelper;
    private final MemberRequestFinder memberRequestFinder;

    @Override
    public SocialUserInfo handle(HttpServletRequest request, HttpServletResponse response) {
        // get token info
        KakaoTokenInfo tokenInfo = getTokenInfo(request);
        // access token injection to cookie
        injectAccessTokenCookie(tokenInfo.getAccess_token(), tokenInfo.getExpires_in(), response);
        // returns kakao info fetch by access token
        return getKakaoUserInfo(tokenInfo.getAccess_token());
    }

    private String getCode(HttpServletRequest request) {
        String code = request.getParameter("code");
        if (code == null) {
            log.warn("인가 코드가 없는 오류!");
            throw new IllegalArgumentException("카카오 로그인 실패. 인가 코드가 없습니다.");
        }
        return code;
    }

    private String getTokenRedirectUrl(HttpServletRequest request) {
        return request.getRequestURL().toString();
    }

    private KakaoTokenInfo getTokenInfo(HttpServletRequest request) {
        String code = getCode(request);
        String tokenRedirectUrl = getTokenRedirectUrl(request);
        KakaoTokenInfo tokenInfo = kakaoApiHelper.getTokenByCode(code, tokenRedirectUrl);
        if (tokenInfo.hasError()) {
            log.error("tokenInfo hasError: {}", tokenInfo.errorToString());
            throw new IllegalStateException("카카오 로그인 실패. 로그인 토큰을 받아오는 데 실패하였습니다.");
        }
        return tokenInfo;
    }

    private void injectAccessTokenCookie(String accessToken, Integer expireIn, HttpServletResponse response) {
        Cookie tokenCookie = new Cookie("authorize-access-token", accessToken);
        tokenCookie.setMaxAge(expireIn);
        tokenCookie.setPath("/");
        response.addCookie(tokenCookie);
    }

    private KakaoUserInfo getKakaoUserInfo(String accessToken) {
        KakaoUserResponse kakaoUserResponse;
        try {
            kakaoUserResponse = memberRequestFinder.fetchKakaoUserInfo(accessToken);
        } catch (Exception e) {
            log.error("멤버 정보를 파싱하던 중 오류 발생");
            throw new IllegalStateException(e);
        }
        if (kakaoUserResponse == null || kakaoUserResponse.hasError()) {
            log.error("kakaoUserInfo에 오류 발생: {}", kakaoUserResponse != null ? kakaoUserResponse.getMsg() : null);
            throw new IllegalStateException("카카오 가입 멤버를 조회하던 중에 오류가 발생하였습니다.");
        }
        JSONObject profile = getProfileFromKakaoUserResponse(kakaoUserResponse);
        return new KakaoUserInfo(
                kakaoUserResponse.getId(),
                profile.getString(NICKNAME_PROPERTY),
                profile.getString(IMAGE_PROPERTY)
        );
    }

    private JSONObject getProfileFromKakaoUserResponse(KakaoUserResponse kakaoUserResponse) {
        return kakaoUserResponse.getKakao_account().getJSONObject("profile");
    }
}
