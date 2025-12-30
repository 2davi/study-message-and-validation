<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>User Creation Page</title>
<style>
.block {
	display: block;
	margin: 5px 3px 3px 5px;
}
</style>
</head>
<body>

<h2>userCreate.jsp에 오셨습니다.</h2>



<form method="POST" action="/user/create">

	<label class="block" for="userId">
		>아이디
		<input type="text" id="userId" name="userId" placeholder="아이디를 입력하세요." title="아이디" 
			required />
	</label>
	<label class="block" for="userPw">
		>비밀번호
		<input type="password" id="userPw" name="userPw" placeholder="비밀번호를 입력하세요." title="비밀번호" 
			maxlength="20" required />
	</label>
	<label class="block" for="userNm">
		>이름
		<input type="text" id="userNm" name="userNm" placeholder="회원 이름을 입력하세요." title="이름" 
			maxlength="20" required />
	</label>
	<label class="block" for="regno1">
		>주민번호
		<input type="text" id="regno1" name="regno1" placeholder="앞 6자리" title="주민번호 13자리" 
			maxlength="6" oninput="this.value = this.value.replace(/[^0-9]/g, '')" />
		
		<input name="regno2" id="regno2" placeholder="뒤 7자리" title="주민번호 13자리"
			maxlength="7" oninput="this.value = this.value.replace(/[^0-9]/g, '')" />
	</label>

	<input type="hidden" name="profileGrpId" value="demo" />
	
	<label class="block">
		<button type="submit" title="입력된 정보로 회원가입을 진행합니다."> 등록 </button>
		<button type="reset"  title="입력된 정보를 모두 지웁니다."> 초기화 </button>
	</label>
</form>

</body>
</html>