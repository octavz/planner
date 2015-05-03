// Reference to the singleton
var userLoginData = Alloy.Models.loginData;
userLoginData.set('userName', 'aaa@aaa.com');
userLoginData.set('userPassword', '123456');
userLoginData.fetch();

var validation = require('de.mwfire.validate');
var auth = require('services/auth');

function tryLogin() {
	var isValid = validation.validate([$.txtUserName, $.txtUserPassword], {
		showAlert : true
	});

	if (isValid) {
		login();
	}
}

function onok() {
	$.activityIndicator.hide();
	$.btnLogin.enabled = true;
	// navigate to startup ui.
	var startupView = Alloy.createController('ProjectList').getView();
	startupView.open();

	$.login.close();
}

function onerror(statuscode) {
	$.activityIndicator.hide();
	$.btnLogin.enabled = true;

	Ti.API.log(this.responseText);
	Ti.API.log(this);
	if (statuscode == '401') {
		alert('Wrong credentials!');
	} else {
		alert('Server side issue');
	}
}

function login() {
	var userName = $.txtUserName.value;
	var userPwd = $.txtUserPassword.value;

	$.activityIndicator.show();
	$.btnLogin.enabled = false;

	auth.Login(userName, userPwd, onok, onerror);
}

Alloy.Models.loginData.fetch();

$.login.addEventListener('close', function() {
	$.destroy();
});

var postLayoutCallback = function(e) {
	$.login.removeEventListener('postlayout', postLayoutCallback);
	$.login.activity.actionBar.hide();
};

$.login.addEventListener('postlayout', postLayoutCallback);
