package com.ssafy.jarviser;

import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.ssafy.jarviser.domain.Meeting;
import com.ssafy.jarviser.domain.User;
import com.ssafy.jarviser.dto.RequestLoginDto;
import com.ssafy.jarviser.dto.RequestUpdateUserDto;
import com.ssafy.jarviser.dto.RequestUserDto;
import com.ssafy.jarviser.dto.ResponseMypageDto;
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

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
class JarviserApplicationTests {

	@Autowired
	private UserService us;

	@Test
	@DisplayName("회원 가입 테스팅")
	@Transactional
	@Rollback(value = false)
	void testRegisterUser() throws Exception {
		//given
		RequestUserDto requestUserDto = RequestUserDto.builder().name("wooseok").password("1234").email("wooseok777777@gmail.com").build();
		us.regist(requestUserDto);

		//when
		User user = us.getUser(requestUserDto.getEmail());

		//then
		Assertions.assertThat(requestUserDto.getName()).isEqualTo(user.getName());
	}

	@Test
	@DisplayName("회원 마이페이지 테스팅")
	@Transactional
	@Rollback(value = false)
	void testMypage() throws Exception{
		//given

		//임의 유저 등록
		RequestUserDto requestUserDto = RequestUserDto.builder().name("wooseok").password("1234").email("wooseok777777@gmail.com").build();
		us.regist(requestUserDto);

		//유저 영속성으로 조회
		User user = us.getUser(requestUserDto.getEmail());
		ResponseMypageDto mypageUser = us.mypage(user.getId());

		//when
		//then
		Assertions.assertThat(mypageUser.getName()).isEqualTo(user.getName());
		Assertions.assertThat(mypageUser.getEmail()).isEqualTo(user.getEmail());
	}

	@Test
	@DisplayName("회원 정보 수정")
	@Transactional
	@Rollback(value = false)
	void testUpdateUser() throws Exception{
		//given
		//임의 유저 등록
		RequestUserDto requestUserDto = RequestUserDto.builder().name("wooseok").password("1234").email("wooseok777777@gmail.com").build();
		us.regist(requestUserDto);

		//유저 영속성으로 조회
		User user = us.getUser(requestUserDto.getEmail());
		long userId = user.getId();
		//when
		RequestUpdateUserDto requestUpdateUserDto = new RequestUpdateUserDto(user.getId(),"abcdefg","sexking");


		us.update(user.getId(),requestUpdateUserDto);

		//then
		Assertions.assertThat(user.getName()).isEqualTo("sexking");
		Assertions.assertThat(user.getPassword()).isEqualTo("abcdefg");
	}

	@Test
	@DisplayName("회원 탈퇴")
	@Transactional
	@Rollback(value = false)
	void testDeleteUser() throws Exception{
		//given

		RequestUserDto requestUserDto = new RequestUserDto();
		requestUserDto.setName("wooseok");
		requestUserDto.setPassword("1234");
		requestUserDto.setEmail("wooseok777777@gmail.com");

		us.regist(requestUserDto);

		User user = us.getUser(requestUserDto.getEmail());
		//when

		us.withdrawal(user.getId());

		//then

		User notFoundUser = us.getUser("wooseok777777@gmail.com");
		Assertions.assertThat(notFoundUser).isEqualTo(null);
	}

	@Test
	@DisplayName("회원 아이디를 기반한 참여 미탕목록")
	@Transactional
	@Rollback(value = false)
	void testMeetingListByUserId() throws Exception{
	    //given

		//유저 등록
		RequestUserDto requestUserDto = new RequestUserDto();
		requestUserDto.setName("wooseok");
		requestUserDto.setPassword("1234");
		requestUserDto.setEmail("wooseok777777@gmail.com");

		us.regist(requestUserDto);

		//유저가 참여한 미팅 등록
		Meeting meeting = Meeting.builder()
				.meetingName("testMeeting")
				.meetingUrl("www.ssafy.com/sampleurl")
				.hostId(123L)
				.startTime(LocalDateTime.now())
				.build();


	    //when

	    //then
	}
}
