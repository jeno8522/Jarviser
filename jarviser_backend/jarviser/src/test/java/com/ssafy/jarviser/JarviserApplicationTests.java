package com.ssafy.jarviser;

import com.ssafy.jarviser.domain.User;
import com.ssafy.jarviser.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
class JarviserApplicationTests {

	@Autowired
	private UserRepository ur;

	@Test
	@DisplayName("회원 가입 테스팅")
	@Transactional
	@Rollback(value = false)
	void testRegisterUser(){
		//given
		User user = User.builder()
				.uid("wooseok777777")
				.password("1234")
				.name("wooseok")
				.email("wooseok777777@gmail.com")
				.build();

		//when
		ur.save(user);
		//then
		User registeredUser = ur.findByUid(user.getUid());

		Assertions.assertThat(user).isEqualTo(registeredUser);
	}

}
