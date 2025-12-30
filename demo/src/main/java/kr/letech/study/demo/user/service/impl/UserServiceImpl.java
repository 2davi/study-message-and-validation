package kr.letech.study.demo.user.service.impl;

import org.springframework.stereotype.Service;

import kr.letech.study.demo.user.dto.request.UserCreateRequest;
import kr.letech.study.demo.user.dto.response.UserCreateResponse;
import kr.letech.study.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 개요: (여기에 클래스 설명을 입력하세요)
 * </pre>
 *
 * @author kcy0122
 * @since 2025. 12. 30.
 * @version 1.0
 * <pre>
 * << 개정이력 >>
 * 
 * 수정일			수정자			수정내용
 * ----------		----------		----------------------------------
 * 2025-12-30		kcy0122			최초 생성
 * </pre>
 */
@Slf4j @Service @RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	@Override
	public UserCreateResponse createUser(UserCreateRequest userRequestDto) {
		// TODO Auto-generated method stub
		UserCreateResponse userResponseDto = null;
		return userResponseDto;
	}

}
