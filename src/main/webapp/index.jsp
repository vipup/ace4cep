<!DOCTYPE html>
<html lang="en">
<head>
<title>ACE in Action</title>
<link rel="stylesheet" type="text/css" href="css/ace.css" />
<link rel="stylesheet" type="text/css" href="init" />
<link rel="stylesheet" type="text/css" href="initcep" />
<script type="text/javascript" src="ace-builds/src-noconflict/ace.js" charset="utf-8"></script>
<script type="text/javascript" src="js/wscommunicator.js"></script>
</head>
<body>
	<div class="noscript">		<h2 style="color: #ff0000">Seems your browser doesn't support Javascript! Websockets rely on Javascript being enabled. Please enable Javascript and reload this page!</h2>	</div>
	<div id="shellcommandaread">
		<div id="shell">
			<input id="chat" style="float: top; text-align: top; width: 63%"
				type="text"
				placeholder="type here the ace4cep command. Try  'help' for ex. ">
		</div>
		<div id="logout">
			<form name="logoutForm" method="GET" action="logout.jsp">
				<button class="button2 " style="float: right;"
					onmousedown="document.logoutForm.submit()"
					onmouseup="document.logoutForm.submit()"
					ontouchstart="document.logoutForm.submit()">LOGOUT</button>
				<!-- <input type="submit" value="sign OFF"> -->
			</form>

		</div>

	</div>
	<div style="text-align: top; width: 100%">
		<div style="text-align: left; width: 80px;">
		</div>
		<div id="editor" class="embedded_ace_code" style="height: 348px;">-- THE_Pi ==: [
@Name("THE_Pi") create schema PI as ( acc double,  i double,  maxI double);
@Name ("defive_VARs") create variable PI pi ;
@Name ("last_assingment") on PI(i>=maxI-1) xxx set pi = xxx  ;
@Name ("readKafka_and_forward_to_PI") insert into PI select 0.000000000000000001123 acc, 0 i,  new Integer(value)  maxI  from MyKafkaEvent ;
-- cycle connection : IP1 <-> PI
insert into IP1
  select
  (i +1.0) i,
  acc  +  4.0 * (1.0 - (i % 2.0) * 2.0) / (2.0 * i + 1.0)  acc,
  maxI
  from PI where i < maxI ;
insert into PI select * from IP1 where (i < maxI) ;
-- take messages from Kafka-topic
 insert into PI select 0.0 acc, 0 i,  new Integer(value)  maxI  from MyKafkaEvent ;
 -- now it is possible to post something into KafkaTopic with: http://localhost:8080/ace4cep/send?message=222

</div>
		<div style="text-align: bottom; width: 480px;">
			<button class="button button3" onmousedown="cep_exec()"
				onmouseup="cep_donothing()" ontouchstart="cep_exec()">EXECUTE ALL from editor </button>
 
			<button class="button button3" onmousedown="example_1()"
				onmouseup="example_1()" ontouchstart="example_1()">Example1</button>
			<button class="button button3" onmousedown="example_2()"
				onmouseup="example_2()" ontouchstart="example_2()">httpEX</button>				
		</div>
		<div id="console-container">
			<div id="ViewConsole" />
		</div>
	</div> 
 
	<script>
		var editor = ace.edit("editor");
		editor.setTheme("ace/theme/tomorrow_night_blue");
		editor.session.setMode("ace/mode/sqlserver");
		function example_1(){
			var val = "-- here is the comment";
			val += "\n select value, amount ,e , size from MyEvent"
			editor.setValue(val);
		}
		function example_2(){
			var val = "-- this example will read all input from http://localhost:8080/ace4cep/push/123";
			val += "\n  select fetch (\"http://localhost:8080/rrdsaas/fetch.json\"  , \"X-1396676775.rrd\" ) from myHttpEvent;"
			editor.setValue(val);
		}
	</script>
	<script type="text/javascript" src="js/ace4cep.js">
 
	</script>	
</body>
</html>