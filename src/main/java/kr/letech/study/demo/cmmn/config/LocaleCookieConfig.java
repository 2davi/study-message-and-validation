//package kr.letech.study.demo.cmmn.config;
//
//import java.time.Duration;
//import java.util.Locale;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.LocaleResolver;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.servlet.i18n.CookieLocaleResolver;
//
///**
// * <pre>
// * 개요: 언어(Locale) 설정 변경하기
// * 
// * 2. 쿠키 기반
// * + loacleResolver()
// * - 영속성: 브라우저 종료 후에도 언어 설정이 유지
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
//public class LocaleCookieConfig implements WebMvcConfigurer {
//
////	@Bean
////	public LocaleResolver localeResolver() {
////		CookieLocaleResolver resolver = new CookieLocaleResovler();
////		resolver.setDefaultLocale(Locale.KOREA);
////		resolver.setCookieName("lang");
////		resolver.setCookieMaxAge(365*24*60*60); //1sus
////		return resolver;
////	}
////	->> DEPRECATED WAY.
//	
//	@Bean
//	public LocaleResolver localeResolver() {
//		// 1. 생성자를 통해 쿠키 이름을 설정한다. (setCookiename 대체)
//		CookieLocaleResolver resolver = new CookieLocaleResolver("lang");
//		
//		// 2. Duration을 사용하여 만료 시간을 설정 (int 타입 setCookieMaxAge 대체)
//		resolver.setCookieMaxAge(Duration.ofDays(365));
//		
//		resolver.setDefaultLocale(Locale.KOREA);
//		return resolver;
//	}
//}
