package Choi.clean_lottery.interfaces.social;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class SocialUserInfo {
    private Long socialId;
    private String nickname;
    private String profileImageUrl;
}
