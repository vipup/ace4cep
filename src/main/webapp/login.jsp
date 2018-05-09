<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="x-ua-compatible" content="ie=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<title>Login page</title>
<link rel="stylesheet" href="css/styles.css">
</head>
<body>
	<div class="loginpage">
		<div class="main">
			<div class="help-actions">
				<h2>Login Demo Using j_security_check</h2>
				<form name="loginForm" method="POST" action="j_security_check">
					<p>
						User name: <input type="text" name="j_username" size="20" />
					</p>
					<p>
						Password: <input type="password" size="20" name="j_password" />
					</p>
					<p>
						<input type="submit" value="Submit" />
					</p>
				</form>
			</div>
		</div>
	</div>
</body>
</html>
