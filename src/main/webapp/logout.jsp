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
	<div class="page">
		<div class="main">
			<div class="help-actions">
<%
session.invalidate();
response.sendRedirect("index.jsp");
%> 
			</div>
		</div>
	</div>
</body>
</html>