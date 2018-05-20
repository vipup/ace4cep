function cep_undo(){
	console.log("undo()");
	
}
function cep_exec(){
	console.log("exec():: "+editor.getValue());
	document.getElementById("chat").value = editor.getValue();
	Chat.sendMessage();
}
function cep_donothing(){
	console.log("donothing()");
}