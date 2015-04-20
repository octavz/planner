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

	var url = Alloy.CFG.App.LoginUrl;

	$.activityIndicator.show();
	$.btnLogin.enabled = false;

	var xhr = Ti.Network.createHTTPClient({
		onload : function(e) {

			Ti.API.log(this.responseText);
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

			Ti.API.log(this.responseText);
			Ti.API.log(this);
			if (this.status == '401') {
				alert('Wrong credentials!');
			} else {
				alert('Server side issue');
			}
		},
		timeout : 15000 /* in milliseconds */
	});
	xhr.open("POST", url);
	xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	xhr.setRequestHeader('Accept', 'application/json');
	xhr.send({
		username : userName,
		password : userPwd,
		client_id : "1",
		grant_type : "password",
		client_secret : "secret"
	});
}

$.login.open();

Alloy.Models.loginData.fetch();

$.login.addEventListener('close', function() {
	$.destroy();
});

var postLayoutCallback = function(e) {
	$.login.removeEventListener('postlayout', postLayoutCallback);
	$.login.activity.actionBar.hide();
};

$.login.addEventListener('postlayout', postLayoutCallback);
