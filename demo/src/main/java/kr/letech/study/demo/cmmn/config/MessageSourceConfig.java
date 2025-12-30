package kr.letech.study.demo.cmmn.config;

import java.nio.charset.StandardCharsets;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * <pre>
 * 개요: MessageSource Bean 등록 (명시적 선언 필수. application.properties로는 자동설정이 안 됨.)
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
 * 2025-12-30		kcy0122			Spring Boot 자동 등록이 안 먹혀서 Bean으로 등록
 * </pre>
 */
@Configuration
public class MessageSourceConfig {

	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("messages/messages");
		messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
		messageSource.setCacheSeconds(3600);
		return messageSource;
	}
}