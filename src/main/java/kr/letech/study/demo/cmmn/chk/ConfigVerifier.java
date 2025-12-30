package kr.letech.study.demo.cmmn.chk;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.logging.LoggerConfiguration;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


/**
 * <pre>
 * 개요: 프로파일 적용이 잘 되었는지 확인
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
@Slf4j @Component
public class ConfigVerifier implements InitializingBean {

	@Value("${spring.profiles.active:default}") // 현재 활성화된 프로파일
	private String activeProfile;
	
	@Value("${spring.datasource.url}")
	private String dbUrl;
		
	@Value("${spring.messages.cache-duration}")
	private String cacheTime;
	
	//logging.level은 Spring Boot가 내부적으로 사용하는 설정이지만,
	//내가 직접 @Value로 주입받을 수는 없다고 한다.
	//@Value("${logging.level.kr.letech}")
	//private String logLevel;
	@Autowired
	private LoggingSystem loggingSystem;
	
	public void logConfiguration() {
		log.warn("다음은 초기 실행 시에 출력되는 검증 로그로, WARN 레벨로 출력되지만 문제 없습니다.");
		log.warn("=== Configuration Loaded ===");
		log.warn("Active Profile: {}", activeProfile);
		log.warn("DB_URL: {}", dbUrl);
		log.warn("Message Cache Duration: {}", cacheTime);
		
		LoggerConfiguration logConfig = loggingSystem.getLoggerConfiguration("kr.letech");
		if(logConfig != null) {
			log.warn("Log Level for kr.letech: {}", logConfig.getEffectiveLevel());
		} else {
			log.warn("Log Level for kr.letech: uncertified");
		}
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			logConfiguration();
			validateConfiguration();
		}
		// 설정 검증 실패 - 명확한 에러메시지와 함께 애플리케이션 중단
		catch(IllegalStateException e) {
			log.error("애플리케이션 설정이 올바르지 않습니다: \n{}", e.getMessage());
			throw e;
		}
		// 예측하지 못한 에러 - 시스템 장애로 간주
		catch(Exception e) {
			log.error("설정 검증 과정 중 예상치 못한 오류 발생: \n{}", e);
			throw new RuntimeException("설정 검증 중 예상치 못한 오류가 발생했습니다.", e);
		}
		
	}
	
	private void validateConfiguration() {
		if(dbUrl == null || dbUrl.isBlank()) {
			throw new IllegalStateException("데이터베이스 URL이 설정되지 않았습니다.");
		}
		if(!dbUrl.startsWith("jdbc:")) {
			throw new IllegalStateException(String.format("데이터베이스 URL 형식이 올바르지 않습니다. `%s`", dbUrl));
		}
		if("prod".equals(activeProfile)) {
			if(dbUrl.contains("192.168") || dbUrl.contains("localhost")) {
				throw new IllegalStateException(String.format("프로덕션 환경에서 로컬 데이터베이스를 사용할 수 없습니다. `%s`", dbUrl));
			}
		}
		
	}
	
    @SuppressWarnings("unused")
	private String maskSensitiveInfo(String url) {
        // 비밀번호 마스킹 (로그에 남을 때 보안)
        return url.replaceAll("password=[^&]*", "password=****");
    }
}
