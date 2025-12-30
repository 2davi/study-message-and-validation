package kr.letech.study.demo.user.dto.response;

import lombok.Data;

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
@Data
public class UserCreateResponse {
	
	private String userId;
	private String userNm;

}
