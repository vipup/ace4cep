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

--4.2.4. Category Segmented Context
create context CategoryByTemp
  group temperature < 65 as cold,
  group temperature between 65 and 85 as normal,
  group temperature > 85 as high
  from SensorEvent;
  // using
  context CategoryByTemp select context.name,sensor, count(*), temperature, context.label from SensorEvent.win:time(5 sec)
  
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