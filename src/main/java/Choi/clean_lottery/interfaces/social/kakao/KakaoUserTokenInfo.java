package Choi.clean_lottery.interfaces.social.kakao;

import lombok.Getter;

@Getter
public class KakaoUserTokenInfo {
    private Long id;
    private Long expiresInMillis;
    private Long expires_in;
    private Long app_id;
    private Long appId;

    private String msg;
    private Integer code;

    protected KakaoUserTokenInfo() {
    }

    public boolean isValid() {
        return msg == null && code == null;
    }
}
