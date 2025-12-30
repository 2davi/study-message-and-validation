package kr.letech.study.demo.user.controller;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.letech.study.demo.user.dto.request.UserCreateRequest;
import kr.letech.study.demo.user.dto.response.UserCreateResponse;
import kr.letech.study.demo.user.service.UserService;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 개요: 사용자 정보에 대한 요청/응답을 다루는 Rest Controller
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
@Slf4j @RestController @RequestMapping("/user")
public class UserRestController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/create")
	public ResponseEntity<?> postUser(
			@ModelAttribute @Validated UserCreateRequest userRequestDto
			, BindingResult bindingResult
			) {
		log.debug("▣▣▣▣▣ UserController - POST:/user/create");
		log.debug("▣▣▣▣  DTO(user): {}", userRequestDto);
		
		// 1.검증 (오류가 존재하면 → 오류 목록을 Map으로 변환 → 상태코드 400으로 반환)
		if(bindingResult.hasErrors()) {
			Map<String, String> errorMap = bindingResult.getFieldErrors().stream()
					.collect(Collectors.toMap(
							FieldError::getField
							, FieldError::getDefaultMessage
							//특정 필드에 2개 이상의 에러가 발생할 경우 IllegalStateException 위험.
							//에러가 여러개면 첫 번째 것만 취급.
							, (existing, replacement) -> existing 
					));
			log.debug("▣▣▣▣ MAP(errorMap): {}", errorMap);
			return ResponseEntity.badRequest().body(errorMap);
		}
		
		// 2.서비스
		UserCreateResponse userResponseDto = userService.createUser(userRequestDto);
		
		// 3.반환
		return ResponseEntity.ok(userResponseDto);
	}
}
