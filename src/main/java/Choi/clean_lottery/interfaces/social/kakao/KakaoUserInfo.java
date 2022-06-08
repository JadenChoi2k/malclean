package Choi.clean_lottery.interfaces.social.kakao;

import Choi.clean_lottery.interfaces.social.SocialUserInfo;
import lombok.Getter;

@Getter
public class KakaoUserInfo extends SocialUserInfo {
    public KakaoUserInfo(Long socialId, String nickname, String profileImageUrl) {
        setSocialId(socialId);
        setNickname(nickname);
        setProfileImageUrl(profileImageUrl);
    }
}
