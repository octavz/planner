// Compiled by ClojureScript 0.0-2371
goog.provide('starter');
goog.require('cljs.core');
cljs.core.enable_console_print_BANG_.call(null);
starter.main = angular.module("main",["ionic"]);
angular.module("main").config(["$stateProvider","$urlRouterProvider",(function ($stateProvider,$urlRouterProvider){var G__9236_9237 = $stateProvider;G__9236_9237.state("app",{"controller": "AppCtrl", "templateUrl": "templates/menu.html", "abstract": true, "url": "/app"});
G__9236_9237.state("app.main",{"views": {"menuContent": {"controller": "ProjectsCtrl", "templateUrl": "templates/projects.html"}}, "url": "/projects"});
return $urlRouterProvider.otherwise("/app/projects");
})]);
starter.main_baseUrl = "http://localhost:9000/api/";
angular.module("main").value("baseUrl",starter.main_baseUrl);
starter.main_ProjectsCtrl = ["$scope",(function ($scope){var o_SHARP_ = $scope;(o_SHARP_["projects"] = [{"id": (1), "title": "Test 1"},{"id": (2), "title": "Test 2"}]);
return o_SHARP_;
})];
angular.module("main").controller("ProjectsCtrl",starter.main_ProjectsCtrl);
starter.main_AppCtrl = ["$scope","$ionicModal","$timeout","$http","$ionicPopup","baseUrl",(function ($scope,$ionicModal,$timeout,$http,$ionicPopup,baseUrl){var o_SHARP__9239 = $scope;(o_SHARP__9239["loadModals"] = ((function (o_SHARP__9239){
return (function (after){return $ionicModal.fromTemplateUrl("templates/login.html",{"backdropClickToClose": false, "scope": $scope}).then(((function (o_SHARP__9239){
return (function (m1){var o_SHARP__9240__$1 = $scope;(o_SHARP__9240__$1["modalLogin"] = m1);
return after.call(null);
});})(o_SHARP__9239))
);
});})(o_SHARP__9239))
);
var o_SHARP__9241 = $scope;(o_SHARP__9241["init"] = ((function (o_SHARP__9241){
return (function (){return $scope.loadModals(((function (o_SHARP__9241){
return (function (){return $scope.showLogin();
});})(o_SHARP__9241))
);
});})(o_SHARP__9241))
);
var o_SHARP__9242 = $scope;(o_SHARP__9242["showLogin"] = ((function (o_SHARP__9242){
return (function (){return ($scope["modalLogin"]).show();
});})(o_SHARP__9242))
);
var o_SHARP__9243 = $scope;(o_SHARP__9243["loginData"] = {"password": "123456", "username": "aaa@aaa.com", "client_secret": "secret", "grant_type": "password", "client_id": "1"});
$ionicModal.fromTemplateUrl("templates/register.html",{"backdropClickToClose": false, "scope": $scope}).then((function (m2){var o_SHARP_ = $scope;(o_SHARP_["modalRegister"] = m2);
return o_SHARP_;
}));
var o_SHARP__9244 = $scope;(o_SHARP__9244["showRegister"] = ((function (o_SHARP__9244){
return (function (){return ($scope["modalRegister"]).show();
});})(o_SHARP__9244))
);
var o_SHARP__9245 = $scope;(o_SHARP__9245["serverLogin"] = ((function (o_SHARP__9245){
return (function (){var postData = {"data": ("client_id="+cljs.core.str.cljs$core$IFn$_invoke$arity$1($scope.loginData.client_id)+"&grant_type="+cljs.core.str.cljs$core$IFn$_invoke$arity$1($scope.loginData.grant_type)+"&client_secret="+cljs.core.str.cljs$core$IFn$_invoke$arity$1($scope.loginData.client_secret)+"&username="+cljs.core.str.cljs$core$IFn$_invoke$arity$1($scope.loginData.username)+"&password="+cljs.core.str.cljs$core$IFn$_invoke$arity$1($scope.loginData.password)), "headers": {"Content-Type": "application/x-www-form-urlencoded", "Accept": "application/json"}, "url": (''+cljs.core.str.cljs$core$IFn$_invoke$arity$1(baseUrl)+"login"), "method": "POST"};var G__9238 = $http.call(null,postData);G__9238.success(((function (G__9238,postData,o_SHARP__9245){
return (function (res){var o_SHARP__9246__$1 = $scope;(o_SHARP__9246__$1["token"] = res.accessToken);
($scope["modalLogin"]).hide();
return cljs.core.prn.call(null,("token: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(($scope["token"]))));
});})(G__9238,postData,o_SHARP__9245))
);
G__9238.error(((function (G__9238,postData,o_SHARP__9245){
return (function (err){return alert(err);
});})(G__9238,postData,o_SHARP__9245))
);
return G__9238;
});})(o_SHARP__9245))
);
var o_SHARP__9247 = $scope;(o_SHARP__9247["doLogin"] = ((function (o_SHARP__9247){
return (function (){return $scope.serverLogin();
});})(o_SHARP__9247))
);
var o_SHARP__9248 = $scope;(o_SHARP__9248["registerData"] = {"password": "123456", "passwordConfirm": "123456", "username": "aaa@aaa.com"});
var o_SHARP__9249 = $scope;(o_SHARP__9249["serverRegister"] = ((function (o_SHARP__9249){
return (function (){var postData = {"password": $scope.registerData.password, "login": $scope.registerData.username};return $http.post((''+cljs.core.str.cljs$core$IFn$_invoke$arity$1(baseUrl)+"register"),postData).success(((function (postData,o_SHARP__9249){
return (function (res){if((cljs.core._EQ_.call(null,res.errCode,(-1))) || (cljs.core.not.call(null,res.errCode)))
{alert("Successfuly registered");
($scope["loginData"]).username = postData.login;
($scope["loginData"]).password = postData.password;
$scope.doLogin();
return $scope.closeRegister();
} else
{return alert(res.errMessage);
}
});})(postData,o_SHARP__9249))
).error(((function (postData,o_SHARP__9249){
return (function (err){return alert(("Error registering: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(err)));
});})(postData,o_SHARP__9249))
);
});})(o_SHARP__9249))
);
var o_SHARP__9250 = $scope;(o_SHARP__9250["doRegister"] = ((function (o_SHARP__9250){
return (function (){return $scope.serverRegister();
});})(o_SHARP__9250))
);
var o_SHARP__9251 = $scope;(o_SHARP__9251["closeRegister"] = ((function (o_SHARP__9251){
return (function (){return ($scope["modalRegister"]).hide();
});})(o_SHARP__9251))
);
var o_SHARP_ = $scope;(o_SHARP_["closeLogin"] = ((function (o_SHARP_){
return (function (){return ($scope["modalLogin"]).hide();
});})(o_SHARP_))
);
return o_SHARP_;
})];
angular.module("main").controller("AppCtrl",starter.main_AppCtrl);
