<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="x-ua-compatible" content="ie=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<title>Login Error</title>
<link rel="stylesheet" href="css/styles.css">
</head>
<body>
	<div class="errpage">
		<div class="main">
			<div class="help-actions">

				<h3>Login Error</h3>
				<a href="index.jsp">Click to Login Again</a>
				<div>Put follow part into your tomcat-users.xml: (For ex:
					File: /opt/tomcat/conf/tomcat-users.xml )</div>
				<pre>
&lt;role rolename=&quot;employee&quot;/&gt;
&lt;user username=&quot;e1&quot; password=&quot;secret&quot; roles=&quot;employee&quot;/&gt;
       </pre>


			</div>
		</div>
	</div>
</body>
</html>
