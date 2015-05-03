function Login(username, password, onok, onerror) {

	var url = Alloy.CFG.App.LoginUrl;
	var xhr = Ti.Network.createHTTPClient({
		onload : function(e) {

			Ti.API.log(this.responseText);

			var data = JSON.parse(this.responseText);
			Ti.API.log(this.responseText);

			Alloy.Globals.UserToken = data.accessToken;
			Alloy.Globals.UserId = data.id;
			
			onok();
		},
		onerror : function(e) {

			Ti.API.log(this.responseText);
			Ti.API.log(this);
			onerror(this.status);
		},
		timeout : 15000 /* in milliseconds */
	});
	xhr.open("POST", url);
	xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	xhr.setRequestHeader('Accept', 'application/json');
	xhr.send({
		username : username,
		password : password,
		client_id : "1",
		grant_type : "password",
		client_secret : "secret"
	});
}

function LoginFake(username, password, onok, onerror) {
	onok();
}

module.exports.Login = Alloy.CFG.App.UseFakeData ? LoginFake : Login;

