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
		<div id="editor" class="embedded_ace_code" style="height: 348px;"> ---10.2.2.2. Last Aggregation Function 
select first(*), last(*) from SensorEvent#time(10 sec)  
		---
insert into TriggerStreaMAT select "A" a, 2/7 b, 2+3
c, 2-1 d,  3*111 e from pattern[every timer:interval(1 sec)] ; 
--- cooment 
  
/// --- comment 2233333
insert into TriggerStreaMAT select "A" a, 2/7 b, 2+3

c, 2-1 d,  3*111 e from pattern[every timer:interval(3 sec)];  
--create variable boolean myvar0 = false;
--create variable boolean myvar1 = true;
--create variable boolean myvar2 = true;
create variable boolean myvar0 = false;
create variable boolean myvar1 = false;
create variable boolean myvar2 = false;

@Name("select-streamstar+outputvar")
 select current_timestamp() , a.* 
    from   pattern[every timer:interval(10 sec)] a 
    output after 3 events when myvar0=false 
    then set myvar1=false, myvar2=false 
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