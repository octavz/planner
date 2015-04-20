function __processArg(obj, key) {
    var arg = null;
    if (obj) {
        arg = obj[key] || null;
        delete obj[key];
    }
    return arg;
}

function Controller() {
    function tryLogin() {
        var isValid = validation.validate([ $.txtUserName, $.txtUserPassword ], {
            showAlert: true
        });
        isValid && login();
    }
    function login() {
        var userName = $.txtUserName.value;
        var userPwd = $.txtUserPassword.value;
        var url = "http://system.ml:9000/api/login";
        $.activityIndicator.show();
        $.btnLogin.enabled = false;
        var xhr = Ti.Network.createHTTPClient({
            onload: function() {
                Ti.API.log(this.responseText);
                $.activityIndicator.hide();
                $.btnLogin.enabled = true;
                JSON.parse(this.responseText);
                Ti.API.log(this.responseText);
                var startupView = Alloy.createController("main").getView();
                startupView.open();
            },
            onerror: function() {
                $.activityIndicator.hide();
                $.btnLogin.enabled = true;
                Ti.API.log(this.responseText);
                Ti.API.log(this);
                alert("401" == this.status ? "Wrong credentials!" : "Server side issue");
            },
            timeout: 15e3
        });
        xhr.open("POST", url);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        xhr.setRequestHeader("Accept", "application/json");
        xhr.send({
            username: userName,
            password: userPwd,
            client_id: "1",
            grant_type: "password",
            client_secret: "secret"
        });
    }
    require("alloy/controllers/BaseController").apply(this, Array.prototype.slice.call(arguments));
    this.__controllerPath = "login";
    if (arguments[0]) {
        {
            __processArg(arguments[0], "__parentSymbol");
        }
        {
            __processArg(arguments[0], "$model");
        }
        {
            __processArg(arguments[0], "__itemTemplate");
        }
    }
    var $ = this;
    var exports = {};
    var __defers = {};
    Alloy.Models.instance("loginData");
    $.__views.login = Ti.UI.createWindow({
        backgroundColor: "white",
        titleControl: "false",
        layout: "vertical",
        navBarHidden: "true",
        id: "login"
    });
    $.__views.login && $.addTopLevelView($.__views.login);
    $.__views.view_login = Ti.UI.createView({
        top: "10%",
        layout: "vertical",
        left: "10dp",
        right: "10dp",
        backgroundColor: "white",
        id: "view_login"
    });
    $.__views.login.add($.__views.view_login);
    $.__views.lblWelcome = Ti.UI.createLabel({
        width: Ti.UI.SIZE,
        height: Ti.UI.SIZE,
        textAlign: Ti.UI.TEXT_ALIGNMENT_CENTER,
        font: {
            fontSize: 18
        },
        text: "Welcome to Planner",
        id: "lblWelcome"
    });
    $.__views.view_login.add($.__views.lblWelcome);
    $.__views.txtUserName = Ti.UI.createTextField({
        width: "100%",
        height: Ti.UI.SIZE,
        top: "10dp",
        color: "#3975A9",
        borderColor: "#3975A9",
        borderStyle: Ti.UI.INPUT_BORDERSTYLE_NONE,
        font: {
            fontSize: 16
        },
        hintText: "User Name",
        tintColor: "#3975A9",
        rules: "required",
        id: "txtUserName",
        name: "User Name"
    });
    $.__views.view_login.add($.__views.txtUserName);
    $.__views.txtUserPassword = Ti.UI.createTextField({
        width: "100%",
        height: Ti.UI.SIZE,
        top: "10dp",
        color: "#3975A9",
        borderColor: "#3975A9",
        borderStyle: Ti.UI.INPUT_BORDERSTYLE_NONE,
        font: {
            fontSize: 16
        },
        hintText: "User Password",
        passwordMask: "true",
        rules: "required",
        id: "txtUserPassword",
        name: "User Password"
    });
    $.__views.view_login.add($.__views.txtUserPassword);
    $.__views.btnLogin = Ti.UI.createButton({
        backgroundColor: "#3975A9",
        color: "white",
        width: "100%",
        top: "15dp",
        title: "Login",
        id: "btnLogin"
    });
    $.__views.view_login.add($.__views.btnLogin);
    tryLogin ? $.__views.btnLogin.addEventListener("click", tryLogin) : __defers["$.__views.btnLogin!click!tryLogin"] = true;
    $.__views.activityIndicator = Ti.UI.createActivityIndicator({
        top: 10,
        left: 10,
        height: Ti.UI.SIZE,
        width: "100%",
        id: "activityIndicator",
        message: "Loading..."
    });
    $.__views.view_login.add($.__views.activityIndicator);
    var __alloyId2 = function() {
        $.txtUserName.value = _.isFunction(Alloy.Models.loginData.transform) ? Alloy.Models.loginData.transform()["userName"] : _.template("<%=loginData.userName%>", {
            loginData: Alloy.Models.loginData.toJSON()
        });
        $.txtUserPassword.value = _.isFunction(Alloy.Models.loginData.transform) ? Alloy.Models.loginData.transform()["userPassword"] : _.template("<%=loginData.userPassword%>", {
            loginData: Alloy.Models.loginData.toJSON()
        });
    };
    Alloy.Models.loginData.on("fetch change destroy", __alloyId2);
    exports.destroy = function() {
        Alloy.Models.loginData.off("fetch change destroy", __alloyId2);
    };
    _.extend($, $.__views);
    var userLoginData = Alloy.Models.loginData;
    userLoginData.set("userName", "Andrei");
    userLoginData.fetch();
    var validation = require("de.mwfire.validate");
    $.login.open();
    Alloy.Models.loginData.fetch();
    $.login.addEventListener("close", function() {
        $.destroy();
    });
    var postLayoutCallback = function() {
        $.login.removeEventListener("postlayout", postLayoutCallback);
        $.login.activity.actionBar.hide();
    };
    $.login.addEventListener("postlayout", postLayoutCallback);
    __defers["$.__views.btnLogin!click!tryLogin"] && $.__views.btnLogin.addEventListener("click", tryLogin);
    _.extend($, exports);
}

var Alloy = require("alloy"), Backbone = Alloy.Backbone, _ = Alloy._;

module.exports = Controller;