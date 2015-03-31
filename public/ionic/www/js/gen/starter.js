// Compiled by ClojureScript 0.0-2371
goog.provide('starter');
goog.require('cljs.core');
cljs.core.enable_console_print_BANG_.call(null);
starter.main = angular.module("main",["ionic"]);
angular.module("main").config(["$stateProvider","$urlRouterProvider",(function ($stateProvider,$urlRouterProvider){var G__5567_5568 = $stateProvider;G__5567_5568.state("app",{"controller": "AppCtrl", "templateUrl": "templates/menu.html", "abstract": true, "url": "/app"});
G__5567_5568.state("app.main",{"views": {"menuContent": {"controller": "ProjectsCtrl", "templateUrl": "templates/projects.html"}}, "url": "/projects"});
return $urlRouterProvider.otherwise("/app/projects");
})]);
starter.main_baseUrl = "http://localhost:9000/api/";
angular.module("main").value("baseUrl",starter.main_baseUrl);
starter.main_ProjectsCtrl = ["$scope","$rootScope","$http","baseUrl",(function ($scope,$rootScope,$http,baseUrl){var o_SHARP_ = $scope;(o_SHARP_["projects"] = [{"id": (1), "title": "Test 1"},{"id": (2), "title": "Test 2"}]);
return o_SHARP_;
})];
angular.module("main").controller("ProjectsCtrl",starter.main_ProjectsCtrl);
starter.main_AppCtrl = ["$scope","$ionicModal","$timeout","$http","$ionicPopup","$rootScope","baseUrl",(function ($scope,$ionicModal,$timeout,$http,$ionicPopup,$rootScope,baseUrl){var o_SHARP__5570 = $scope;(o_SHARP__5570["loadModals"] = ((function (o_SHARP__5570){
return (function (after){return $ionicModal.fromTemplateUrl("templates/login.html",{"backdropClickToClose": false, "scope": $scope}).then(((function (o_SHARP__5570){
return (function (m1){var o_SHARP__5571__$1 = $scope;(o_SHARP__5571__$1["modalLogin"] = m1);
return after.call(null);
});})(o_SHARP__5570))
);
});})(o_SHARP__5570))
);
var o_SHARP__5572 = $scope;(o_SHARP__5572["init"] = ((function (o_SHARP__5572){
return (function (){return $scope.loadModals(((function (o_SHARP__5572){
return (function (){return $scope.showLogin();
});})(o_SHARP__5572))
);
});})(o_SHARP__5572))
);
var o_SHARP__5573 = $scope;(o_SHARP__5573["showLogin"] = ((function (o_SHARP__5573){
return (function (){return ($scope["modalLogin"]).show();
});})(o_SHARP__5573))
);
var o_SHARP__5574 = $scope;(o_SHARP__5574["loginData"] = {"password": "123456", "username": "aaa@aaa.com", "client_secret": "secret", "grant_type": "password", "client_id": "1"});
$ionicModal.fromTemplateUrl("templates/register.html",{"backdropClickToClose": false, "scope": $scope}).then((function (m2){var o_SHARP_ = $scope;(o_SHARP_["modalRegister"] = m2);
return o_SHARP_;
}));
var o_SHARP__5575 = $scope;(o_SHARP__5575["showRegister"] = ((function (o_SHARP__5575){
return (function (){return ($scope["modalRegister"]).show();
});})(o_SHARP__5575))
);
var o_SHARP__5576 = $scope;(o_SHARP__5576["serverLogin"] = ((function (o_SHARP__5576){
return (function (){var postData = {"data": ("client_id="+cljs.core.str.cljs$core$IFn$_invoke$arity$1($scope.loginData.client_id)+"&grant_type="+cljs.core.str.cljs$core$IFn$_invoke$arity$1($scope.loginData.grant_type)+"&client_secret="+cljs.core.str.cljs$core$IFn$_invoke$arity$1($scope.loginData.client_secret)+"&username="+cljs.core.str.cljs$core$IFn$_invoke$arity$1($scope.loginData.username)+"&password="+cljs.core.str.cljs$core$IFn$_invoke$arity$1($scope.loginData.password)), "headers": {"Content-Type": "application/x-www-form-urlencoded", "Accept": "application/json"}, "url": (''+cljs.core.str.cljs$core$IFn$_invoke$arity$1(baseUrl)+"login"), "method": "POST"};var G__5569 = $http.call(null,postData);G__5569.success(((function (G__5569,postData,o_SHARP__5576){
return (function (res){var o_SHARP__5577__$1 = $rootScope;(o_SHARP__5577__$1["user"] = res);
($scope["modalLogin"]).hide();
return cljs.core.prn.call(null,("token: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1((function (){var obj_SHARP_ = JSON;var fn_SHARP_ = (obj_SHARP_["stringify"]);return fn_SHARP_.call(obj_SHARP_,($rootScope["user"]));
})())));
});})(G__5569,postData,o_SHARP__5576))
);
G__5569.error(((function (G__5569,postData,o_SHARP__5576){
return (function (err){return alert(err);
});})(G__5569,postData,o_SHARP__5576))
);
return G__5569;
});})(o_SHARP__5576))
);
var o_SHARP__5578 = $scope;(o_SHARP__5578["doLogin"] = ((function (o_SHARP__5578){
return (function (){return $scope.serverLogin();
});})(o_SHARP__5578))
);
var o_SHARP__5579 = $scope;(o_SHARP__5579["registerData"] = {"password": "123456", "passwordConfirm": "123456", "username": "aaa@aaa.com"});
var o_SHARP__5580 = $scope;(o_SHARP__5580["serverRegister"] = ((function (o_SHARP__5580){
return (function (){var postData = {"password": $scope.registerData.password, "login": $scope.registerData.username};return $http.post((''+cljs.core.str.cljs$core$IFn$_invoke$arity$1(baseUrl)+"register"),postData).success(((function (postData,o_SHARP__5580){
return (function (res){if((cljs.core._EQ_.call(null,res.errCode,(-1))) || (cljs.core.not.call(null,res.errCode)))
{alert("Successfuly registered");
($scope["loginData"]).username = postData.login;
($scope["loginData"]).password = postData.password;
$scope.doLogin();
return $scope.closeRegister();
} else
{return alert(res.errMessage);
}
});})(postData,o_SHARP__5580))
).error(((function (postData,o_SHARP__5580){
return (function (err){return alert(("Error registering: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(err)));
});})(postData,o_SHARP__5580))
);
});})(o_SHARP__5580))
);
var o_SHARP__5581 = $scope;(o_SHARP__5581["doRegister"] = ((function (o_SHARP__5581){
return (function (){return $scope.serverRegister();
});})(o_SHARP__5581))
);
var o_SHARP__5582 = $scope;(o_SHARP__5582["closeRegister"] = ((function (o_SHARP__5582){
return (function (){return ($scope["modalRegister"]).hide();
});})(o_SHARP__5582))
);
var o_SHARP_ = $scope;(o_SHARP_["closeLogin"] = ((function (o_SHARP_){
return (function (){return ($scope["modalLogin"]).hide();
});})(o_SHARP_))
);
return o_SHARP_;
})];
angular.module("main").controller("AppCtrl",starter.main_AppCtrl);
