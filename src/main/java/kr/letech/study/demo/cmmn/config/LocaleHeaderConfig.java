//package kr.letech.study.demo.cmmn.config;
//
//import java.util.Locale;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.LocaleResolver;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
//
///**
// * <pre>
// * 개요: 언어(Locale) 설정 변경하기
// * 
// * 3. HTTP Accept-Language Header 기반
// * + loacleResolver()
// * - 브라우저의 `Accept-Language` 헤더로 자동 감지
// * - 사용자가 명시적으로 변경할 수 없음
// * </pre>
// *
// * @author kcy0122
// * @since 2025. 12. 30.
// * @version 1.0
// * <pre>
// * << 개정이력 >>
// * 
// * 수정일			수정자			수정내용
// * ----------		----------		----------------------------------
// * 2025-12-30		kcy0122			최초 생성
// * </pre>
// */
//@Configuration
//public class LocaleHeaderConfig implements WebMvcConfigurer {
//	
//	@Bean
//	public LocaleResolver localeResolver() {
//		AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
//		resolver.setDefaultLocale(Locale.KOREA);
//		return resolver;
//	}
//}
