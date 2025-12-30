package kr.letech.study.demo.cmmn.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;

/**
 * <pre>
 * 개요: Spring Web Configuration (없어도 되는 파일.)
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
 * 2025-12-30		kcy0122			MessageSource를 읽도록 설정
 * </pre>
 */
@Configuration @RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer{

	private final MessageSource messageSource;
	
	@Bean
	public LocalValidatorFactoryBean validator() {
		LocalValidatorFactoryBean lvfBean = new LocalValidatorFactoryBean();
		lvfBean.setValidationMessageSource(messageSource);
		return lvfBean;
	}
	
	@Override
	public Validator getValidator() {
		return validator();
	}
}