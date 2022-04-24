package Choi.clean_lottery.web.kakaoapi;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.util.Optional;

@Getter
@Slf4j
public class KakaoTokenInfo {
    private String access_token;
    private Integer expires_in;
    private String refresh_token;
    private Integer refresh_token_expires_in;
    private String msg;
    private Integer code;

    private KakaoTokenInfo(String access_token, Integer expires_in, String refresh_token, Integer refresh_token_expires_in) {
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.refresh_token = refresh_token;
        this.refresh_token_expires_in = refresh_token_expires_in;
    }

    // 에러 났을 시 생성자
    private KakaoTokenInfo(String msg, Integer code) {
        this.msg = msg;
        this.code = code;
    }

    public static KakaoTokenInfo fromJson(String json) {
        JSONObject jsonObject = new JSONObject(json);
        if (jsonObject.isNull("access_token")) {
            if (jsonObject.isNull("msg") || jsonObject.isNull("code")) {
                log.info("토큰을 찾을 수 없습니다. -> {}", json);
                return new KakaoTokenInfo("token_not_found", 500);
            }
            return new KakaoTokenInfo(jsonObject.getString("msg"), jsonObject.getInt("code"));
        }

        return new KakaoTokenInfo(
                jsonObject.getString("access_token"),
                jsonObject.getInt("expires_in"),
                jsonObject.getString("refresh_token"),
                jsonObject.getInt("refresh_token_expires_in"));
    }

    public boolean hasError() {
        return msg != null || code != null;
    }

    public String errorToString() {
        return String.format("%s: %s",
                Optional.ofNullable(code).orElse(0),
                Optional.ofNullable(msg).orElse("msg null"));
    }
}
