'use strict';

angular.module('myApp')


.config(['$routeProvider', 'AccessRights',
function ($routeProvider, AccessRights) {

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
        accessRights: AccessRights.Registered
    });
    $routeProvider.when('/Error', {
        templateUrl: 'partials/Error.html',
        controller: 'ErrorCtrl',
        accessRights: 1
    });
    $routeProvider.when('/AppBootstrap', {
        templateUrl: 'partials/Private/AppBootstrap.html',
        controller: 'AppBootstrapCtrl',
        //accessRights: 2
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
]);
