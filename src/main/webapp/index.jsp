<!DOCTYPE html>
<html lang="en">
<head>
<title>ACE in Action</title>
<link rel="stylesheet" type="text/css" href="css/ace.css" />
<script type="text/javascript" src="ace-builds/src-noconflict/ace.js" charset="utf-8"></script>
<script type="text/javascript" src="js/wscommunicator.js"></script>
</head>
<body>
	<div class="noscript">		<h2 style="color: #ff0000">Seems your browser doesn't support Javascript! Websockets rely on Javascript being enabled. Please enable Javascript and reload this page!</h2>	</div>
	<input id="chat"  type="text" placeholder="hier is the hidden content of editor-area. Used as buffer for ws-chat-communication" />
	<div style="text-align: top; width: 100%">
		<div style="text-align: left; width: 80px;">
		</div>
		<div id="editor" class="embedded_ace_code" style="height: 348px;">

		--- PI calc ala :  https://doc.akka.io/docs/akka/2.0.2/intro/getting-started-first-java.html
@Name("THE_Pi") create schema PI as ( acc double,  i double,  maxI double);
create variable PI pi;
insert into IP1
   select 
    (i +1.0) i,
    acc  +  4.0 * (1.0 - (i % 2.0) * 2.0) / (2.0 * i + 1.0)  acc,
    maxI
   from PI where i < maxI
;

insert into PI select * from IP1 where (i < maxI);
insert into PI select 0.0 acc, 0 i,  new Integer(value)  maxI  from MyKafkaEvent;

@Name ("readKafka_and_forward_to_PI") insert into PI select 0.000000000000000001123 acc, 0 i,  new Integer(value)  maxI  from MyKafkaEvent;
-- USE it after hideall: .stmt_10.65#3:[{pi=='{acc=3.1415826533897158, i=99998.0, maxI=99999.0}'}]
@Name ("last_assingment") on PI(i>=maxI-1) xxx set pi = xxx;

    			

    			</div>
		<div style="text-align: bottom; width: 480px;">
			<button class="button button3" onmousedown="cep_exec()"
				onmouseup="cep_donothing()" ontouchstart="cep_exec()">EXEC</button>
 
			<button class="button button3" onmousedown="undo()"
				onmouseup="cep_donothing()" ontouchstart="cep_undo()">STEPBACK</button>
		</div>
		<div id="console-container">
			<div id="ViewConsole" />
		</div>
	</div> 

	<script>
		var editor = ace.edit("editor");
		editor.setTheme("ace/theme/tomorrow_night_blue");
		editor.session.setMode("ace/mode/sqlserver");
	</script>
	<script type="text/javascript" src="js/ace4cep.js">
 
	</script>	
</body>
</html>