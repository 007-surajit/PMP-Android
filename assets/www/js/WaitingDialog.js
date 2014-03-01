// window.plugins.waitingDialog

function WaitingDialog() {
}

WaitingDialog.prototype.show = function(text) {
	cordova.exec(null, null, "WaitingDialog", "show", [text]);
}

WaitingDialog.prototype.hide = function() {
	cordova.exec(null, null, "WaitingDialog", "hide", []);
}

WaitingDialog.prototype.getLocation = function(successCallback,errorCallback) {
	cordova.exec(successCallback, errorCallback, "WaitingDialog", "gps", []);
}

WaitingDialog.prototype.playTone = function() {
	//alert('playTone');
	cordova.exec(null, null, "WaitingDialog", "ringtone", []);
}

cordova.addConstructor(function()  {
	if(!window.plugins) {
	   window.plugins = {};
	}

   // shim to work in 1.5 and 1.6
   if (!window.Cordova) {
	   window.Cordova = cordova;
   };

   window.plugins.waitingDialog = new WaitingDialog();
   //alert(window.plugins.waitingDialog);
});