'use strict';


// Declare app level module which depends on filters, and services
angular.module('myApp', [
  'ngRoute',
  'myApp.filters',
  'myApp.services',
  'myApp.directives',
  'myApp.controllers',
  'myAuth.services'
]).
config(['$routeProvider', function ($routeProvider) {

	//todo cip - would be nice to have accessRights taken from AccessRights service
    $routeProvider.when('/Register', { templateUrl: 'partials/Register.html', controller: 'RegisterCtrl' ,accessRights:1});
    $routeProvider.when('/Login', { templateUrl: 'partials/LoginMock.html', controller: 'LoginMockCtrl',accessRights:1 });
    $routeProvider.when('/RegisterOk', { templateUrl: 'partials/RegisterOk.html', controller: 'RegisterOkCtrl',accessRights:1 });
    $routeProvider.otherwise({ redirectTo: '/Register' });


    
}]).
run(['CurrentUserSession','AccessRights', function (CurrentUserSession,AccessRights) {

    CurrentUserSession.setRights(AccessRights.Public);
}]);

