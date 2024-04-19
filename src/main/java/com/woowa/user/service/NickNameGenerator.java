package com.woowa.user.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.woowa.user.domain.User;
import com.woowa.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NickNameGenerator {

	private final UserRepository userRepository;

	private final List<String> adj = Arrays.asList(
		"환상적인", "멋진", "밝은", "빛나는", "즐거운",
		"흥겨운", "훌륭한", "아름다운", "자유로운", "열정적인",
		"감동적인", "독특한", "창의적인", "유쾌한", "멋진",
		"따뜻한", "낙천적인", "우아한", "매력적인", "격렬한",
		"희망찬", "활기찬", "자신감 있는", "겸손한", "친근한",
		"신선한", "유머러스한", "진정한", "열심히하는", "꿈같은",
		"우아한", "인정받는", "감사하는", "자랑스러운", "감동적인",
		"사랑스러운", "존경받는", "확신하는", "자비로운", "행복한",
		"안정적인", "찬사를 받는", "인기있는", "빈틈없는", "존경받는",
		"가치있는", "감동적인", "원활한", "기적적인", "원만한",
		"영광스러운", "귀하고 특별한", "이루어지는", "의리있는", "신비로운",
		"안심되는", "감동적인", "독립적인", "자아실현의", "동기부여하는",
		"칭찬받는", "협력적인", "놀라운", "존경스러운", "재능있는",
		"수용력이 있는", "승리의", "진실된", "성취가능한", "진보된",
		"긍정의", "활동적인", "성실한", "존중받는", "고마운",
		"위대한", "자연스러운", "우아한", "확고한", "환영받는",
		"고마운", "슬기로운", "협조하는", "훌륭한", "안전한",
		"즐겁고 행복한", "인정받는", "사랑받는", "진정한", "포용하는",
		"근면한", "창조적인", "자발적인", "안정된", "용감한",
		"확고한", "낙관적인", "행운을 빌어주는", "헌신적인", "믿음직한"
	);

	private final Random random = new Random();

	public String generateUnique() {
		String nickname;
		while (true) {
			nickname = generateNickname();
			Optional<User> user = userRepository.findByNickname(nickname);
			if (user.isEmpty()) {
				break;
			}
		}
		return nickname;
	}

	private String generateNickname() {
		int index = random.nextInt(100);
		int postfix = random.nextInt(1000);
		return adj.get(index) + " 유저" + postfix;
	}
}
