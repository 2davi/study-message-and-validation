<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Log-in Page</title>
</head>
<body>

<form method="POST" action="#">
	<label>
		<input type="text" name="username" placeholder="username" title="아이디" />
		<span class="error" id="usernameError"></span>
	</label>
	<label>
		<input type="password" name="password" placeholder="password" title="비밀번호" />
		<span class="error" id="passwordError"></span>
	</label>
</form>

</body>
</html>