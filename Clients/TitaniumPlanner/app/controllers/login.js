// Reference to the singleton
var userLoginData = Alloy.Models.loginData;
userLoginData.set('userName', 'Andrei');
userLoginData.fetch();

var validation = require('de.mwfire.validate');

function tryLogin() {
	var isValid = validation.validate([$.txtUserName, $.txtUserPassword], {
		showAlert : true
	});

	if (isValid) {
		login();
	}
}

function login() {
	var userName = $.txtUserName.value;
	var userPwd = $.txtUserPassword.value;

	var url = "https://rt-pay.com/vrtwebapi/api/user/login";
	url = url + "?username=" + userName + "&password=" + userPwd + "&customerNumber=100";

	$.activityIndicator.show();
	$.btnLogin.enabled = false;
	var xhr = Ti.Network.createHTTPClient({
		onload : function(e) {

			$.activityIndicator.hide();
			$.btnLogin.enabled = true;
			
			var data = JSON.parse(this.responseText);
			Ti.API.log(this.responseText);

			// navigate to startup ui.
			var startupView = Alloy.createController('main').getView();
			startupView.open();
			
			//$.index.close();
			//$.index = null;
		},
		onerror : function(e) {
			$.activityIndicator.hide();
			$.btnLogin.enabled = true;
			
			if(this.status == '401'){
				alert('Wrong credentials!');	
			}else{
				alert('Server side issue');
			}
		},
		timeout : 15000 /* in milliseconds */
	});
	xhr.open("GET", url);
	xhr.send();
}

$.login.open();

Alloy.Models.loginData.fetch();

$.login.addEventListener('close', function() {
	$.destroy();
});

var postLayoutCallback  = function(e){
  $.login.removeEventListener('postlayout', postLayoutCallback);
  $.login.activity.actionBar.hide();
};

$.login.addEventListener('postlayout', postLayoutCallback);
