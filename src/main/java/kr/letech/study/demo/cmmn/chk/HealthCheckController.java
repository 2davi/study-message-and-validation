package kr.letech.study.demo.cmmn.chk;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 개요: 프로젝트 정상 구동 여부 확인 컨트롤러
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
@Slf4j
@RestController
@RequestMapping("/check")
public class HealthCheckController {

	@GetMapping("/health")
	public ResponseEntity<String> health() {
		
		String text = "It's fine!";
		return ResponseEntity.ok(text);
	}
}
