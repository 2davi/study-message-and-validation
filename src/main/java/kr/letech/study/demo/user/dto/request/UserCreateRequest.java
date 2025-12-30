package kr.letech.study.demo.user.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class UserCreateRequest {
	
	@NotBlank(message = "{valid.id.notBlank}")
	@Size(min = 4, max = 20, message = "{valid.id.size}")
	private String userId;
	
	@NotBlank(message = "{valid.pw.notBlank}")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$", message = "{valid.pw.pattern}")
	private String userPw;
	
	@NotBlank(message = "{valid.nm.notBlank}")
	private String userNm;
	
	
	//Nullable한 필드의 경우에는 @Size를 사용하지 않는다. 왜냐하면,
	//Form 요청으로 들어온 입력 없는 값은 null이 아니라 "" 빈문자열 처리가 된다.
	//그렇기 때문에 min값이 0으로 취급되는 것.
	//결론: @Pattern 하나로 처리하는 것이 옳다. 논리적으로도 그렇다.
	//@Size(min = 6, max = 6)
	@Pattern(regexp = "^[0-9]*${6}", message = "{valid.regno.pattern}")
	private String regno1;			

	//값이 없을 때는 통과, 있을 때는 형식을 체크
	@Pattern(regexp = "^[0-9]*${7}", message = "{valid.regno.pattern}")
	private String regno2;
	
	private String profileGrpId;
}
