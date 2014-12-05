// Compiled by ClojureScript 0.0-2371
goog.provide('starter');
goog.require('cljs.core');
cljs.core.enable_console_print_BANG_.call(null);
starter.main = angular.module("main",["ionic"]);
angular.module("main").config(["$stateProvider","$urlRouterProvider",(function ($stateProvider,$urlRouterProvider){var G__6894_6895 = $stateProvider;G__6894_6895.state("app",{"controller": "AppCtrl", "templateUrl": "templates/menu.html", "abstract": true, "url": "/app"});
G__6894_6895.state("app.main",{"views": {"menuContent": {"controller": "ProjectsCtrl", "templateUrl": "templates/projects.html"}}, "url": "/projects"});
return $urlRouterProvider.otherwise("/app/projects");
})]);
starter.main_baseUrl = "http://planme.herokuapp.com/api/";
angular.module("main").value("baseUrl",starter.main_baseUrl);
starter.main_ProjectsCtrl = ["$scope","$rootScope","$http","baseUrl",(function ($scope,$rootScope,$http,baseUrl){var o_SHARP_ = $scope;(o_SHARP_["projects"] = [{"id": (1), "title": "Test 1"},{"id": (2), "title": "Test 2"}]);
return o_SHARP_;
})];
angular.module("main").controller("ProjectsCtrl",starter.main_ProjectsCtrl);
starter.main_AppCtrl = ["$scope","$ionicModal","$timeout","$http","$ionicPopup","$rootScope","baseUrl",(function ($scope,$ionicModal,$timeout,$http,$ionicPopup,$rootScope,baseUrl){var o_SHARP__6897 = $scope;(o_SHARP__6897["loadModals"] = ((function (o_SHARP__6897){
return (function (after){return $ionicModal.fromTemplateUrl("templates/login.html",{"backdropClickToClose": false, "scope": $scope}).then(((function (o_SHARP__6897){
return (function (m1){var o_SHARP__6898__$1 = $scope;(o_SHARP__6898__$1["modalLogin"] = m1);
return after.call(null);
});})(o_SHARP__6897))
);
});})(o_SHARP__6897))
);
var o_SHARP__6899 = $scope;(o_SHARP__6899["init"] = ((function (o_SHARP__6899){
return (function (){return $scope.loadModals(((function (o_SHARP__6899){
return (function (){return $scope.showLogin();
});})(o_SHARP__6899))
);
});})(o_SHARP__6899))
);
var o_SHARP__6900 = $scope;(o_SHARP__6900["showLogin"] = ((function (o_SHARP__6900){
return (function (){return ($scope["modalLogin"]).show();
});})(o_SHARP__6900))
);
var o_SHARP__6901 = $scope;(o_SHARP__6901["loginData"] = {"password": "123456", "username": "aaa@aaa.com", "client_secret": "secret", "grant_type": "password", "client_id": "1"});
$ionicModal.fromTemplateUrl("templates/register.html",{"backdropClickToClose": false, "scope": $scope}).then((function (m2){var o_SHARP_ = $scope;(o_SHARP_["modalRegister"] = m2);
return o_SHARP_;
}));
var o_SHARP__6902 = $scope;(o_SHARP__6902["showRegister"] = ((function (o_SHARP__6902){
return (function (){return ($scope["modalRegister"]).show();
});})(o_SHARP__6902))
);
var o_SHARP__6903 = $scope;(o_SHARP__6903["serverLogin"] = ((function (o_SHARP__6903){
return (function (){var postData = {"data": ("client_id="+cljs.core.str.cljs$core$IFn$_invoke$arity$1($scope.loginData.client_id)+"&grant_type="+cljs.core.str.cljs$core$IFn$_invoke$arity$1($scope.loginData.grant_type)+"&client_secret="+cljs.core.str.cljs$core$IFn$_invoke$arity$1($scope.loginData.client_secret)+"&username="+cljs.core.str.cljs$core$IFn$_invoke$arity$1($scope.loginData.username)+"&password="+cljs.core.str.cljs$core$IFn$_invoke$arity$1($scope.loginData.password)), "headers": {"Content-Type": "application/x-www-form-urlencoded", "Accept": "application/json"}, "url": (''+cljs.core.str.cljs$core$IFn$_invoke$arity$1(baseUrl)+"login"), "method": "POST"};var G__6896 = $http.call(null,postData);G__6896.success(((function (G__6896,postData,o_SHARP__6903){
return (function (res){var o_SHARP__6904__$1 = $rootScope;(o_SHARP__6904__$1["user"] = res);
($scope["modalLogin"]).hide();
return cljs.core.prn.call(null,("token: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1((function (){var obj_SHARP_ = JSON;var fn_SHARP_ = (obj_SHARP_["stringify"]);return fn_SHARP_.call(obj_SHARP_,($rootScope["user"]));
})())));
});})(G__6896,postData,o_SHARP__6903))
);
G__6896.error(((function (G__6896,postData,o_SHARP__6903){
return (function (err){return alert(err);
});})(G__6896,postData,o_SHARP__6903))
);
return G__6896;
});})(o_SHARP__6903))
);
var o_SHARP__6905 = $scope;(o_SHARP__6905["doLogin"] = ((function (o_SHARP__6905){
return (function (){return $scope.serverLogin();
});})(o_SHARP__6905))
);
var o_SHARP__6906 = $scope;(o_SHARP__6906["registerData"] = {"password": "123456", "passwordConfirm": "123456", "username": "aaa@aaa.com"});
var o_SHARP__6907 = $scope;(o_SHARP__6907["serverRegister"] = ((function (o_SHARP__6907){
return (function (){var postData = {"password": $scope.registerData.password, "login": $scope.registerData.username};return $http.post((''+cljs.core.str.cljs$core$IFn$_invoke$arity$1(baseUrl)+"register"),postData).success(((function (postData,o_SHARP__6907){
return (function (res){if((cljs.core._EQ_.call(null,res.errCode,(-1))) || (cljs.core.not.call(null,res.errCode)))
{alert("Successfuly registered");
($scope["loginData"]).username = postData.login;
($scope["loginData"]).password = postData.password;
$scope.doLogin();
return $scope.closeRegister();
} else
{return alert(res.errMessage);
}
});})(postData,o_SHARP__6907))
).error(((function (postData,o_SHARP__6907){
return (function (err){return alert(("Error registering: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(err)));
});})(postData,o_SHARP__6907))
);
});})(o_SHARP__6907))
);
var o_SHARP__6908 = $scope;(o_SHARP__6908["doRegister"] = ((function (o_SHARP__6908){
return (function (){return $scope.serverRegister();
});})(o_SHARP__6908))
);
var o_SHARP__6909 = $scope;(o_SHARP__6909["closeRegister"] = ((function (o_SHARP__6909){
return (function (){return ($scope["modalRegister"]).hide();
});})(o_SHARP__6909))
);
var o_SHARP_ = $scope;(o_SHARP_["closeLogin"] = ((function (o_SHARP_){
return (function (){return ($scope["modalLogin"]).hide();
});})(o_SHARP_))
);
return o_SHARP_;
})];
angular.module("main").controller("AppCtrl",starter.main_AppCtrl);
