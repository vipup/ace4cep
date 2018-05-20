function cep_undo(){
	console.log("undo()");
	
}
function cep_exec(){
	console.log("exec():: "+editor.getValue());	
	Chat.sendMultilineMessage(editor.getValue());
}
function cep_donothing(){
	console.log("donothing()");
}