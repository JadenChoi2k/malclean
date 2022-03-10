package Choi.clean_lottery.web.kakaoapi;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.time.LocalDateTime;

@Getter
public class KakaoUserInfo {
    private Long id;
    private String connected_at;
    // jsonObject로 변환하여 사용한다.
    private JSONObject properties;
    private JSONObject kakao_account;
    // 오류 발생 시 not null
    private String msg;
    private Integer code;

    protected KakaoUserInfo() {
    }

    public KakaoUserInfo(Long id, String connected_at, JSONObject properties, JSONObject kakao_account) {
        this.id = id;
        this.connected_at = connected_at;
        this.properties = properties;
        this.kakao_account = kakao_account;
    }

    public KakaoUserInfo(String msg, Integer code) {
        this.msg = msg;
        this.code = code;
    }

    public static KakaoUserInfo fromJson(String json) {
        JSONObject jsonObject = new JSONObject(json);
        if (jsonObject.isNull("id")) {
            return new KakaoUserInfo(jsonObject.getString("msg"), jsonObject.getInt("code"));
        }

        return new KakaoUserInfo(
                jsonObject.getLong("id"),
                jsonObject.getString("connected_at"),
                jsonObject.getJSONObject("properties"),
                jsonObject.getJSONObject("kakao_account"));
    }

    public boolean hasError() {
        return msg != null || code != null;
    }
}
