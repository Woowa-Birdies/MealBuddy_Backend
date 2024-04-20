package com.woowa.user.service;

import static com.woowa.common.domain.SecurityConstant.*;

import java.util.Optional;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woowa.user.domain.SocialLogin;
import com.woowa.user.domain.User;
import com.woowa.user.domain.dto.CustomOAuth2User;
import com.woowa.user.domain.dto.UserDTO;
import com.woowa.user.repository.SocialLoginRepository;
import com.woowa.user.repository.UserRepository;
import com.woowa.user.service.dto.GoogleResponse;
import com.woowa.user.service.dto.KakaoResponse;
import com.woowa.user.service.dto.NaverResponse;
import com.woowa.user.service.dto.OAuth2Response;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final SocialLoginRepository socialLoginRepository;
	private final UserRepository userRepository;
	private final NickNameGenerator nickNameGenerator;

	@Override
	@Transactional
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);

		log.debug("oAuth2User = {}", oAuth2User);

		String registrationId = userRequest.getClientRegistration().getRegistrationId();

		OAuth2Response oAuth2Response;
		oAuth2Response = getOAuth2Response(registrationId, oAuth2User);
		if (oAuth2Response == null)
			return null;

		String externalId = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
		UserDTO userDto;
		Optional<SocialLogin> socialLogin = socialLoginRepository.findByExternalId(externalId);
		if (socialLogin.isEmpty()) {
			User savedUser = userRepository.save(new User(nickNameGenerator.generateUnique()));
			userDto = new UserDTO(savedUser.getId(), ROLE_USER, savedUser.getNickname(), externalId,
				oAuth2Response.getProvider());
			socialLoginRepository.save(new SocialLogin(savedUser.getId(), oAuth2Response.getProvider(), externalId));
		} else {
			User user = userRepository.findById(socialLogin.get().getUserId()).get();
			userDto = new UserDTO(socialLogin.get().getUserId(), ROLE_USER, user.getNickname(), externalId,
				oAuth2Response.getProvider());
		}

		return new CustomOAuth2User(userDto);
	}

	private static OAuth2Response getOAuth2Response(String registrationId, OAuth2User oAuth2User) {
		OAuth2Response oAuth2Response;
		if ("naver".equals(registrationId)) {
			oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
		} else if ("google".equals(registrationId)) {
			oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
		} else if ("kakao".equals(registrationId)) {
			oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
		} else {
			return null;
		}
		return oAuth2Response;
	}

}
