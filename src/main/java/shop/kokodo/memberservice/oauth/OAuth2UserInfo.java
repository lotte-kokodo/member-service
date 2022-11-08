package shop.kokodo.memberservice.oauth;

import java.util.Map;

public interface OAuth2UserInfo {

    Map<String, Object> getAttributes();
    String getProviderId();
    String getProvider();
    String getName();
    String getEmail();
    String getBirthyear();
    String getBirthday();
    String getProfileImage();
    String getMobile();

}
