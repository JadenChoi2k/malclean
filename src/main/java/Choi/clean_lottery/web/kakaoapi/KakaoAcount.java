package Choi.clean_lottery.web.kakaoapi;

import lombok.Getter;

@Getter
public class KakaoAcount {
    @Getter
    private static class Profile {
        private String nickname;
        private String thumbnail_image_url;
        private String profile_image_url;
        private Boolean is_default_image;
    }

    private Boolean profile_needs_agreement;
}
