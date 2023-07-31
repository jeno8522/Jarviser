package com.ssafy.jarviser;

import com.ssafy.jarviser.domain.User;
import com.ssafy.jarviser.dto.RequestLoginDto;
import com.ssafy.jarviser.dto.RequestUpdateUserDto;
import com.ssafy.jarviser.dto.RequestUserDto;
import com.ssafy.jarviser.repository.UserRepository;
import com.ssafy.jarviser.service.UserService;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

@SpringBootTest
class JarviserApplicationTests {

	@Autowired
	private UserRepository ur;
	@Autowired
	private UserService us;

	@Test
	@DisplayName("회원 가입 테스팅")
	@Transactional
	@Rollback(value = false)
	void testRegisterUser(){
		//given
		User user = User.builder()
				.password("1234")
				.name("wooseok")
				.email("wooseok777777@gmail.com")
				.build();

		//when

		//then
		Optional<User> registeredUser = ur.findByEmail(user.getEmail());

		registeredUser.ifPresent(foundUser ->{
			Assertions.assertThat(user).isEqualTo(foundUser);
		});

		if(!registeredUser.isPresent()){
			Assertions.assertThat(registeredUser).isEqualTo(null);
		}
	}

	@Test
	@DisplayName("회원 마이페이지 테스팅")
	@Transactional
	@Rollback(value = false)
	void testMypage() throws Exception{
	    //given
		User user = User.builder()
				.password("1234")
				.name("wooseok")
				.email("wooseok777777@gmail.com")
				.build();

		//when
		ur.save(user);
	    long userId = 1;
		User findUser = ur.findById(userId);
	    //when

	    //then
		Assertions.assertThat(findUser.getEmail()).isEqualTo("wooseok777777@gmail.com");
	}

	@Test
	@DisplayName("회원 정보 수정")
	@Transactional
	void testUpdateUser() throws Exception{
		//given
		RequestUserDto requestUserDto = RequestUserDto.builder().
										password("1234").name("wooseok").email("wooseok777777@gmail.com").build();

		Optional<User> findUser = ur.findByEmail(requestUserDto.getEmail());
		if(findUser.isPresent()){
			User foundUser = findUser.get();
			RequestUpdateUserDto updateUserDto = new RequestUpdateUserDto(foundUser.getId(),"4321a","yoonseok");
			us.update(foundUser.getId(), updateUserDto);
			Assertions.assertThat(foundUser.getPassword()).isEqualTo("4321a");
			Assertions.assertThat(foundUser.getName()).isEqualTo("yoonseok");
		}

	}

	@Test
	@DisplayName("회원 아이디를 기반한 참여 미티옥록")
	@Transactional
	void testMeetingList() throws Exception{
	    //given
		User user = User.builder()
				.password("1234")
				.name("wooseok")
				.email("wooseok777777@gmail.com")
				.build();

	    //when
	    //then
	}
}
