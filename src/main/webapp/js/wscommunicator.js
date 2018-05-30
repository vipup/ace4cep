var Chat = {};
Chat.socket = null;
var MYCHATURI= '/ace4cep/websocket/chat';
var DELIM= '^';

Chat.connect = (function(host) {
	if ('WebSocket' in window) {
		Chat.socket = new WebSocket(host);
	} else if ('MozWebSocket' in window) {
		Chat.socket = new MozWebSocket(host);
	} else {
		ChatConsole.log('Error: WebSocket is not supported by this browser.');
		return;
	}

	Chat.socket.onopen = function() {
		ChatConsole.log('Info: WebSocket connection opened.');
		document.getElementById('chat').onkeydown = function(event) {
			if (event.keyCode == 13) {
				Chat.sendMessage();
			}
		};
	};

	Chat.socket.onclose = function() {
		document.getElementById('chat').onkeydown = null;
		ChatConsole.log('Info: WebSocket closed.');
	};

	Chat.socket.onmessage = function(message) {
		ChatConsole.log(message.data);
	};
});

Chat.initialize = function() {
	if (window.location.protocol == 'http:') {
		// Chat.connect('ws://' + window.location.host + '/examples/websocket/chat');
		Chat
				.connect('ws://' + window.location.host
						+ MYCHATURI);
	} else {
		Chat.connect('wss://' + window.location.host
				+ MYCHATURI);
	}
};

// On-Post-New-text-in chat
Chat.sendMessage = (function() {
	var message = document.getElementById('chat').value;
	if (message != '') {
		ChatConsole.log("> : "+message);
		Chat.socket.send(message);
	}else{
		ChatConsole.log(message);
	}
	
	document.getElementById('chat').value = '';

});
// sendMultilineMessage
Chat.sendMultilineMessage = (function(messagePar) {
	var message = messagePar;
	if (message != '') {
		//ChatConsole.log("> : "+message);
		Chat.socket.send(message);
	}
});

var ChatConsole = {};
// create initial Chat-panel
ChatConsole.log = (function(message) {
	var usernameTmp =  message.slice(0,message.indexOf(DELIM));
	var textTmp =  message.slice(message.indexOf(DELIM)+1);
	var ViewConsole = document.getElementById('ViewConsole');
		
	// add into message-pane
	if ("ver" == textTmp){
		var messageTmp = "<div>" +
				"<a class=\"label\" href=\"#\">"+"_"+"</a> " +
				"<img src=\"/rrdsaas/speed.gif\"/>"+
				"<p>" + textTmp + "</p>"+
				"</div>";
		var liTmp = document.createElement('div');
		liTmp.innerHTML = messageTmp;
		ViewConsole.insertBefore(liTmp, ViewConsole.childNodes[0]) ;
	}else
	{	// add into console
		var messageTmp =  
						'<small><a class="label" href="#">'+
						usernameTmp+
						'</a>'+'</small>'+						
						'<span class="spacer">:</span>'+
						'<small data-timestamp="'+new Date()+'">'+(new Date()).toTimeString()+'</small>'+
						'<div>'+
						'<pre id="embedded_ace_code"   class=" ace_editor ace-tm" draggable="false">'+
						textTmp+
						'</pre> ' +
						'<div>';
		var liTmp = document.createElement('div');
		liTmp.innerHTML = messageTmp; 
		ViewConsole.insertBefore(liTmp, ViewConsole.childNodes[0]) ;
	}
	ViewConsole.scrollTop = ViewConsole.scrollHeight;
	
	var toDEL = ViewConsole.children.length - 5;
	for( i=0; i<toDEL;i++ ){ // kill the last 
		try{ 
			ViewConsole.children[5].remove();
		}catch(err){
			console.log("del ERROR!"+err);
		}
	};
});

Chat.initialize();

document.addEventListener("DOMContentLoaded", function() {
	// Remove elements with "noscript" class - <noscript> is not allowed in XHTML
	var noscripts = document.getElementsByClassName("noscript");
	for (var i = 0; i < noscripts.length; i++) {
		noscripts[i].parentNode.removeChild(noscripts[i]);
	}
}, false);
