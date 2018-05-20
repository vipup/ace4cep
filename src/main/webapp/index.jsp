<!DOCTYPE html>
<html lang="en">
<head>
<title>ACE in Action</title>
<style type="text/css" media="screen">
#embedded_ace_code {
    border: 1px solid #DDD;
    border-radius: 4px;
    border-bottom-right-radius: 0px;
    margin-top: 5px;
}
#editor {
    position: relative;
    overflow: hidden;
    font: 12px/normal 'Monaco', 'Menlo', 'Ubuntu Mono', 'Consolas', 'source-code-pro', monospace;
    direction: ltr;
    text-align: left;
    -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
}
#___editor {
	position: absolute;
	top: 0;
	right: 0;
	bottom: 0;
	left: 0;
}

.button {
	background-color: #4CAF50;
	border: none;
	color: white;
	padding: 20px;
	text-align: center;
	text-decoration: none;
	display: inline-block;
	font-size: 16px;
	margin: 4px 2px;
}

.button3 {
	border-radius: 8px;
}

canvas {
	border: 1px solid #d3d3d3;
	background-color: #f1f1f1;
}
</style>
</head>
<body>
	<div style="text-align: top; width: 100%">
		<div style="text-align: left; width: 80px;">
		</div>
		<div id="editor" class="embedded_ace_code" style="height: 348px;">
select rstream * from
  StartEvent#time(30 sec) start
    left outer join
  AbortedEvent#time(30 sec) abort
    on about.exchangeId = start.exchangeId
    left outer join
  FinishedEvent#time(30 sec) finished
    on finished.exchangeId = start.exchangeId
    
-- The next statement outputs the count of events within the last 3 minutes:
select count(*) from MyEvent#time(3 min)
    			</div>
		<div style="text-align: bottom; width: 480px;">
			<button class="button button3" onmousedown="cep_exec()"
				onmouseup="cep_donothing()" ontouchstart="cep_exec()">EXEC</button>
			<br>
			<br>
			<button class="button button3" onmousedown="undo()"
				onmouseup="cep_donothing()" ontouchstart="cep_undo()">STEPBACK</button>
		</div>
	</div>


	<script src="ace-builds/src-noconflict/ace.js" type="text/javascript"
		charset="utf-8"></script>
	<script>
		var editor = ace.edit("editor");
		editor.setTheme("ace/theme/tomorrow_night_blue");
		editor.session.setMode("ace/mode/sqlserver");
	</script>
	<script type="text/javascript" src="js/ace4cep.js">
 
	</script>	
</body>
</html>