# MessageSource and Validation
> #DTO #Controller #BindingResult #MessageSource #프로젝트생성 #SpringBoot 

[01. 의존성 주입](#01-의존성-주입)
[02. MessageSource 주입](#02-messagesource-주입)
[03. messages.properties 작성](#03-messages-properties-작성)
[04. DTO 필드에 message 속성 부여](#04-dto-필드에-message-속성-부여)
[05. Controller에서 파라미터 검증](#05-controller에서-파라미터-검증)

---

## 01. 의존성 주입 (`pom.xml`)

> MessageSource와 Validation 기능을 사용하려면 다음 의존성들이 필요합니다.

#### 필수 의존성

**1. Spring Boot Validation Starter**
- Bean Validation (JSR-380) 구현 제공
- `@NotBlank`, `@Pattern`, `@Size` 등 검증 어노테이션 지원
- BindingResult (검증 결과)를 자동으로 바인딩
- MessageSource 의존성 포함

**2. Spring Boot Web Starter**
- Spring Web MVC 전체 제공
- `@RestController`, `@PostMapping` 등 웹 컨트롤러 어노테이션 지원
- MessageSource 자동 등록 및 구성
- LocaleResolver (언어 설정) 자동 구성

```xml
<!-- Spring Boot Validation Starter -->
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-validation</artifactId>
</dependency>
<!-- Spring Boot Web Starter -->
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

#### 권장 의존성

**3. Lombok**
- [보일러플레이트 코드](#a-보일러플레이트-코드) 감소
- DTO 클래스에서 `@Data` 로 Getter/Setter 자동 생성
- 코드 가독성 및 작성 시간 단축

```xml
<!-- Lombok -->
<dependency>
	<groupId>org.projectlombok</groupId>
	<artifactId>lombok</artifactId>
	<optional>true</optional>
</dependency>
```

#### 개발 편의 의존성

**4. Spring Boot Dev Tools**
- 개발 중 생산성 향상
- `messages.properties` 파일 변경 시 자동 리로드
- 애플리케이션 재시작 없이 메시지 파일 수정 사항 즉시 반영

```xml
<!-- Spring Boot Dev Tools -->
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-devtools</artifactId>
	<scope>runtime</scope>
	<optional>true</optional>
</dependency>
```

**5. Tomcat Embed Jasper**
- JSP 렌터링 지원
- HTML Form 기반 검증이 필요할 때 포함
- REST API 기반 개발이면 불필요

```xml
<!-- Tomcat Embed Jasper -->
<dependency>
	<groupId>org.apache.tomcat.embed</groupId>
	<artifactId>tomcat-embed-jasper</artifactId>
</dependency>
```

---

## 02. MessageSource 주입

MessageSource는 애플리케이션의 메시지를 외부 파일(`application.properties`)로 분리하여 관리하는 Spring의 Internationalization (i18n) 메커니즘이다. 이를 통해 하드코딩된 메시지를 제거하고 유지보수성을 높일 수 있다.

### 02-1. Spring Boot 자동 구성 (`application.properties`)

```properties
spring.messages.basename=messages
spring.messages.encoding=UTF-8
spring.messages.fallback-to-system-locale=false
```

**자동 구성의 한계**
이론상 위 설정만으로 작동해야 하지만, 환경에 따라서는 검증 어노테이션(`@Validated`)이 **MessageSource**를 인식하지 못하는 문제가 빈번하게 발생한다.
- 검증 실패 시, 정의된 메시지(`"아이디는 필수입니다."`) 대신 메시지 키(`{valid.id.notBlank}`)가 그대로 반환된다.
- Spring Boot가 자동으로 등록하는 `Validator`가 `MessageSource`와 연결되지 않은 상태로 초기화되기 때문이다.

### 02-2. Configuration Bean 등록

자동 구성의 불확실성을 제거하기 위해, **Java Config**를 통해 명시적으로 **Bean**을 등록하고 연결하는 방식을 사용했다.

**02-2-1. MessageSource Bean 등록**
`messages.properties` 파일을 읽어 메모리에 로드하는 역할을 한다.

```java
import java.nio.charset.StandardCharsets;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

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
```

**02-2-2. Validator와 MessageSource 연결 (`WebMvcConfigurer`)**
단순히 `MessageSource`만 등록해서는 검증 로직에 적용되지 않는다. ~~되던데?~~ `WebMvcConfigurer`를 구현하여 글로벌 **Validator**를 재정의해야 한다.

1. `application.properties` 정보를 바탕으로 생성된 **MessageSource**
2. `LocalValidatorFactoryBean`이 자동으로 생성한 **Validator**
각각이 무사 생성되었는데도 문제가 있다면, Spring Boot의 자동 구성이 두 객체를 서로 연결해주지 못해서라고 진단할 수 있다. -> 이때 **WebMvcConfigurer**가 개입한다.

**WebMvcConfigurer**는 Spring MVC 설정을 커스터마이징(`Strategy Pattern`)할 수 있는 인터페이스.
- `LocalValidatorFactoryBean`: 실제 검증을 수행하는 구현체. `setValidationMessageSource()`를 통해 "어떤 메시지 소스를 쓸지" 정해야 한다.
- `getValidator()` Override: Spring MVC에게 "전역(Global)에서 내가 정의한 Validator를 사용"하라고 선언해야 한다.

```java
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;

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
```

**실행 흐름**

- 요청 수신: `@Validated`가 붙은 DTO가 Controller에 도착
- 검증 위임: **WebMvcConfig**에서 등록한 `WebMvcConfig.getValidator()`를 호출
- 검증 수행: **[LocalValidatorFactoryBean](#b-LocalValidatorFactoryBean)**이 검증을 수행하고 오류 감지
- 메시지 조회: 연결된 **MessageSource**를 통해 `{valid.id.notBlank}` 키를 조회
- 결과 반환: 실제 에러 메시지가 **BindingResult**에 저장

---

## 03. messages.properties 작성

메시지 파일은 `src/main/resources/messages/.` 아래에 저장했다.

```text
src/main/resources/messages/.
├── messages_ko.properties
└── messages_en.properties
```

프로퍼티스 파일은 아래와 같고...

```properties
# messages_ko.properties
valid.id.notBlank=아이디는 필수 입력 값입니다.
valid.id.size=아이디는 {min}자에서 {max}자 사이여야 합니다.
valid.pw.notBlank=비밀번호는 필수 입력 값입니다.
valid.pw.pattern=비밀번호는 영문, 숫자, 특수문자를 포함하여 8자 이상이어야 합니다.
valid.number=숫자만 입력 가능합니다.
valid.nm.notBlank=이름은 필수 입력 값입니다.
valid.regno.pattern=주민번호가 올바르지 않습니다.

valid.common=입력값이 올바르지 않습니다.

# messages_en.properties
valid.id.notBlank=ID is required.
valid.id.size=ID must be between {min} and {max} characters.
valid.pw.notBlank=Password is required.
valid.pw.pattern=Password must be at least 8 characters long and include letters, numbers, and special characters.
valid.number=Only numbers are allowed.
valid.nm.notBlank=Name is required.
valid.regno.pattern=Invalid resident registration number.

valid.common=Invalid input value.
```

언어 설정, **Locale**을 변경하는 방법에는 크게 세 가지가 있었다.

1. Session 방식: query string
2. Cookie 방식: 브라우저 간 영속성
3. Header 방식 (Accept-Language): 클라이언트의 명시적 변경 방지

#### 03-1. Session 방식

- 클라이언트가 `?lang=en` 파라미터로 요청 -> LocaleChangeInterceptor가 세션에 저장
- 이후 요청들은 Session에서 Locale을 읽음
- MessageSource가 해당 Locale에 맞는 메시지 파일 조회 `messages_en.properties`


```java
import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

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
```

#### 03-2. Cookie 방식

+ loacleResolver()
- 영속성: 브라우저 종료 후에도 언어 설정이 유지

```java
import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
public class LocaleHeaderConfig implements WebMvcConfigurer {
	
	@Bean
	public LocaleResolver localeResolver() {
		AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
		resolver.setDefaultLocale(Locale.KOREA);
		return resolver;
	}
}
```

#### 03-3. HTTP Header 방식

- 브라우저의 `Accept-Language` 헤더로 자동 감지
- 사용자가 명시적으로 변경할 수 없음

```java
import java.time.Duration;
import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

@Configuration
public class LocaleCookieConfig implements WebMvcConfigurer {

//	@Bean
//	public LocaleResolver localeResolver() {
//		CookieLocaleResolver resolver = new CookieLocaleResovler();
//		resolver.setDefaultLocale(Locale.KOREA);
//		resolver.setCookieName("lang");
//		resolver.setCookieMaxAge(365*24*60*60); //1sus
//		return resolver;
//	}
//	->> DEPRECATED WAY.
	
	@Bean
	public LocaleResolver localeResolver() {
		// 1. 생성자를 통해 쿠키 이름을 설정한다. (setCookiename 대체)
		CookieLocaleResolver resolver = new CookieLocaleResolver("lang");
		
		// 2. Duration을 사용하여 만료 시간을 설정 (int 타입 setCookieMaxAge 대체)
		resolver.setCookieMaxAge(Duration.ofDays(365));
		
		resolver.setDefaultLocale(Locale.KOREA);
		return resolver;
	}
}
```

**사진자료**
![한국어일 때](./img/20251230_490(1727).png)
![영어 파라미터가 붙을 때](./img/20251230_489(1724).png)
---

## 04. DTO 필드에 message 속성 부여

- `@Size`는 빈문자열`""` 의 길이를 0 취급함.
- Nullable한 값에는 `@Pattern`의 정규식 안에서 `{}` 로 검증하자.

```java
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

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
	
	@Pattern(regexp = "^[0-9]*${6}", message = "{valid.regno.pattern}")
	private String regno1;			

	@Pattern(regexp = "^[0-9]*${7}", message = "{valid.regno.pattern}")
	private String regno2;
	
	private String profileGrpId;
}
```

---

## 05. Controller에서 파라미터 검증

```java
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.letech.study.demo.user.dto.request.UserCreateRequest;
import kr.letech.study.demo.user.dto.response.UserCreateResponse;
import kr.letech.study.demo.user.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j @RestController @RequestMapping("/user")
public class UserRestController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/create")
	public ResponseEntity<?> postUser(
			@ModelAttribute @Validated UserCreateRequest userRequestDto
			, BindingResult bindingResult
			) {
		log.debug("▣▣▣▣▣ UserController - POST:/user/create");
		log.debug("▣▣▣▣  DTO(user): {}", userRequestDto);
		
		// 1.검증 (오류가 존재하면 → 오류 목록을 Map으로 변환 → 상태코드 400으로 반환)
		if(bindingResult.hasErrors()) {
			Map<String, String> errorMap = bindingResult.getFieldErrors().stream()
					.collect(Collectors.toMap(
							FieldError::getField
							, FieldError::getDefaultMessage
							//특정 필드에 2개 이상의 에러가 발생할 경우 IllegalStateException 위험.
							//에러가 여러개면 첫 번째 것만 취급.
							, (existing, replacement) -> existing 
					));
			log.debug("▣▣▣▣ MAP(errorMap): {}", errorMap);
			return ResponseEntity.badRequest().body(errorMap);
		}
		
		// 2.서비스
		UserCreateResponse userResponseDto = userService.createUser(userRequestDto);
		
		// 3.반환
		return ResponseEntity.ok(userResponseDto);
	}
}
```

## 00. 주석

### a. 보일러플레이트 코드

...

### b. LocalValidatorFactoryBean

**LocalValidatorFactoryBean**은 그자체로 `Factory`이면서 `Validator`다.

```java
//LocalvalidatorFactoryBean 클래스의 상속 구조
public class LocalValidatorFactoryBean extends SpringValidatorAdapter
    implements ValidatorFactory, ApplicationContextAware, InitializingBean { ... }

//부모인 SpringValidatorAdapter
public class SpringValidatorAdapter implements SmartValidator, jakarta.validation.Validator { ... }
```

1. **Factory 역할**: `jakarta.validation.ValidatorFactory`를 구현하여 내부적으로 Hibernate Validator 같은 실제 검증 엔진을 부트스트래핑(생성/초기화)한다.
2. **Validator 역할**: `org.springframework.validation.Validator` 인터페이스를 직접 구현하고 있어서, **스스로가 검증 메서드(`validate()`)를 갖는다.**

_더 정확히는... Adapter Pattern으로 동작한다._
_객체가 생성한 '실제 검증 엔진(Validator 구현체)'을 내부적으로 들고 있다가, `vaildate()`가 호출되면 작업을 위임(Delegate)하는 것._

**Spring의 추상화 전략**
- Spring MVC는 뭐가 되었든 Spring의 표준 Validator 인터페이스만 호출한다.
- Bean Validation(Hibernate): 그저 JSR-303 표준대로 검증할 따름.
둘 사이를 중재하는 역할을 **LocalValidatorFactoryBean**이 담당한다.

- Spring MVC 앞에서는 내가 Validator인 척을 하고
- 실제 업무는 Hibernate Validator에게 시킨다.
- `Errors`나 `BindingResult`를 들고 내가 한 것처럼 보고한다.

> Spring MVC가 Global Validator `LocalValidatorFactoryBean`의 `validate()`를 호출합니다.
> `LocalValidatorFactoryBean`은 내부의 Hibernate Validator에게 검증을 위임합니다.
> Hibernate Validator가 검증 오류를 발견하면, `LocalValidatorFactoryBean`은 미리 주입받은 `MessageSource`를 사용해 오류 코드를 실제 메시지로 변환합니다.
> 변환된 메시지를 `BindingResult`에 담아 반환합니다.
