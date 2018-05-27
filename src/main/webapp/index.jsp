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
----
@Name ("The_KaF-KA") select  percent(1.0* 1, 3.00001 + new Integer(value)) MyFun , value.split(",").size()   from MyKafkaEvent  ; 
 	 	
--- kafka reader
select  value, 3 + new Integer(value), value.split(",").size()   from MyKafkaEvent
		
--- pattern using example
--- .stmt_21.6#2:[{a.e=='30003.3'},{sum(a.e*b.temperature)=='2628138.508860812'}]
select a.e, sum(a.e * b.temperature)
from pattern [every a=MyEvent -> 
    b=SensorEvent(temperature < a.e) where timer:within(11 sec)]#time(2 sec) 
where a.type in ('A', 'b', "c" )
group by a.e
having sum(a.e +1) > 100

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