package kr.letech.study.demo.cmmn.config;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

/**
 * <pre>
 * 개요: 언어(Locale) 설정 변경하기
 * 
 * 1. 세션 기반
 * + loacleResolver(), localeChangeInterceptor(), addInterceptor(InterceptorRegistry registry)
 * - 클라이언트가 `?lang=en` 파라미터로 요청 -> LocaleChangeInterceptor가 세션에 저장
 * - 이후 요청들은 Session에서 Locale을 읽음
 * - MessageSource가 해당 Locale에 맞는 메시지 파일 조회 (`messages_en.properties`)
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
@Configuration
public class LocaleSessionConfig implements WebMvcConfigurer {

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver resolver = new SessionLocaleResolver();
		resolver.setDefaultLocale(Locale.KOREA);
		return resolver;
	}
	
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
		interceptor.setParamName("lang"); //URL 파라미터명: ?lang=en
		return interceptor;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}
}
