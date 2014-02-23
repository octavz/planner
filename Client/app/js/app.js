'use strict';

// Declare app level module which depends on filters, and services
angular.module('myApp', [
    'ngRoute',
    'myApp.filters',
    'myApp.services',
    'myApp.directives',
    'myApp.controllers',
    'myAuth.services',
    'ngStorage'
])

.config(['$routeProvider',
    function($routeProvider) {

        //todo cip - would be nice to have accessRights taken from AccessRights service
        $routeProvider.when('/Register', {
            templateUrl: 'partials/Register.html',
            controller: 'RegisterCtrl',
            accessRights: 1
        });
        $routeProvider.when('/Login', {
            templateUrl: 'partials/LoginMock.html',
            controller: 'LoginMockCtrl',
            accessRights: 1
        });
        $routeProvider.when('/RegisterOk', {
            templateUrl: 'partials/RegisterOk.html',
            controller: 'RegisterOkCtrl',
            accessRights: 1
        });
        $routeProvider.when('/Home', {
            templateUrl: 'partials/Private/Home.html',
            controller: 'HomeCtrl',
            accessRights: 2
        });
        $routeProvider.when('/Other', {
            templateUrl: 'partials/Private/Other.html',
            controller: 'OtherCtrl',
            accessRights: 2
        });
        $routeProvider.otherwise({
            redirectTo: '/Login'
        });

    }
])

.run(['Auth', 'AccessRights',
    function(Auth, AccessRights) {
        //exemplu de logare
        //Auth.login("gigi@gigi.com", AccessRights.Registered);
        // Auth.logout();
    }
])
