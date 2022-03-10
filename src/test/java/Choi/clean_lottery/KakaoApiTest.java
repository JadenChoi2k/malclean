package Choi.clean_lottery;

import Choi.KakaoRestApiHelper;
import Choi.clean_lottery.web.kakaoapi.KakaoAppConst;
import Choi.clean_lottery.web.kakaoapi.KakaoUserInfo;
import Choi.clean_lottery.web.kakaoapi.KakaoUserTokenInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class KakaoApiTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void tokenValidTest() throws Exception {
        // given
        KakaoRestApiHelper apiHelper = new KakaoRestApiHelper();
        apiHelper.setAccessToken("g1i9VQreQ1uv3DyYKtPNblI1DlM_WVl2hUSCTgopdSkAAAF-hVawpA");

        // when
        String userTokenInfo = apiHelper.getUserTokenInfo();

        // then
        KakaoUserTokenInfo info = mapper.readValue(userTokenInfo, KakaoUserTokenInfo.class);
    }

    @Test
    public void memberIdListTest() throws Exception {
        // given
        KakaoRestApiHelper apiHelper = new KakaoRestApiHelper();
        apiHelper.setAdminKey("7ba5611d81ad5df111acee947ef28120");

        // when
        String userIds = apiHelper.getUserIds();

        // then
        System.out.println("userIds = " + userIds);

    }

    @Test
    public void getUserInfo() throws Exception {
        // given
        KakaoRestApiHelper apiHelper = new KakaoRestApiHelper();
        apiHelper.setAdminKey(KakaoAppConst.ADMIN_KEY);
        apiHelper.setAccessToken("md73ki5pC_wPK2aIXcSQ26jl3SXfwXJaZJ7A_AorDKgAAAF-pEUeFA");
        String userTokenInfo = apiHelper.getUserTokenInfo();
        KakaoUserTokenInfo tokenInfo = mapper.readValue(userTokenInfo, KakaoUserTokenInfo.class);

        // when

        // then
    }

    @Test
    public void tokenExpiredTest() throws Exception {
        // given
        KakaoRestApiHelper apiHelper = new KakaoRestApiHelper();
        apiHelper.setAdminKey(KakaoAppConst.ADMIN_KEY);
        apiHelper.setAccessToken("md73ki5pC_wPK2aIXcSQ26jl3SXfwXJaZJ7A_AorDKgAAAF-pEUeFA");

        // when
        String userTokenInfo = apiHelper.getUserTokenInfo();
        KakaoUserTokenInfo tokenInfo = mapper.readValue(userTokenInfo, KakaoUserTokenInfo.class);

        // then
        Assertions.assertThat(tokenInfo.isValid()).isEqualTo(false);
    }

    @Test
    public void getTokenTest() throws Exception {
        // given
        String code = "27S6La7nyL6N1DQwn0SHn1-VTfwzC37LjY3QTmjgqvm-1qYTu4CXDE6u5mlzrS0GUz_gcgorDNMAAAF-kKEFZg";
        String path = "https://kapi.kakao.com/oauth/token";
        String redirect = "http://localhost:8080/member/join-pass";

        // when

        // then

    }
}
