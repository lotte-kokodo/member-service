package shop.kokodo.memberservice.oauth;

import java.util.UUID;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import shop.kokodo.memberservice.entity.Member;
import shop.kokodo.memberservice.repository.MemberRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public PrincipalOauth2UserService(
        MemberRepository memberRepository,
        BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2UserInfo oAuth2UserInfo = null;
        String provider = userRequest.getClientRegistration().getRegistrationId();

        if (provider.equals("naver")) {
            oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
        }

        String providerId = oAuth2UserInfo.getProviderId();
        String loginId = String.format("%s_%s", provider, providerId);

        String uuid = UUID.randomUUID().toString().substring(0, 6);
        String password = bCryptPasswordEncoder.encode("패스워드"+uuid);

        Member member = memberRepository.findByLoginId(loginId);

        //DB에 없는 사용자라면 회원가입처리
        if(member == null){
            member = Member.builder()
                .loginId(loginId)
                .email( oAuth2UserInfo.getEmail())
                .password(password)
                .birthday(String.format("%s-%s", oAuth2UserInfo.getBirthyear(), oAuth2UserInfo.getBirthday()))
                .profileImageUrl(oAuth2UserInfo.getProfileImage())
                .phoneNumber(oAuth2UserInfo.getMobile())
                .build();
            memberRepository.save(member);
        }

        return new PrincipalDetails(member, oAuth2UserInfo);
    }
}
