package kr.letech.study.demo.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.letech.study.demo.user.dto.request.UserCreateRequest;
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
@Slf4j @Controller @RequestMapping("/user")
public class UserController {

	@GetMapping("/create")
	public String postUser() {
		String lvn = "user/userCreate";
		return lvn;
	}

}
