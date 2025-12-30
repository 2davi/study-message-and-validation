package kr.letech.study.demo.user.service;

import kr.letech.study.demo.user.dto.request.UserCreateRequest;
import kr.letech.study.demo.user.dto.response.UserCreateResponse;

/**
 * <pre>
 * 개요: 사용자 관리 서비스
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
public interface UserService {

	public UserCreateResponse createUser(UserCreateRequest userRequestDto);
}
