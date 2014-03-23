'use strict';

angular.module('myApp')


.config(['$routeProvider', 'RouteAccessProvider',
    function($routeProvider, RouteAccessProvider) {

        $routeProvider.when('/Register', {
            templateUrl: 'partials/Register.html',
            controller: 'RegisterCtrl'
        });
        $routeProvider.when('/Login', {
            templateUrl: 'partials/LoginMock.html',
            controller: 'LoginMockCtrl'
        });
        $routeProvider.when('/RegisterOk', {
            templateUrl: 'partials/RegisterOk.html',
          });
        $routeProvider.when('/Home', {
            templateUrl: 'partials/Private/Home.html',
            resolve: RouteAccessProvider.routeResolvers
        });
        $routeProvider.when('/Error', {
            templateUrl: 'partials/Error.html',
            resolve: RouteAccessProvider.routeResolvers
        });
        $routeProvider.when('/AppBootstrap', {
            templateUrl: 'partials/Private/AppBootstrap.html',
            controller: 'AppBootstrapCtrl',
            resolve: RouteAccessProvider.routeResolvers
        });
        $routeProvider.when('/Projects', {
            templateUrl: 'partials/Private/Projects.html',
            resolve: RouteAccessProvider.routeResolvers
        });
        $routeProvider.when('/ProjectNew', {
            templateUrl: 'partials/Private/ProjectNew.html',
            resolve: RouteAccessProvider.routeResolvers
        });
        $routeProvider.when('/Tasks', {
            templateUrl: 'partials/Private/Tasks.html',
            resolve: RouteAccessProvider.routeResolvers
        });
        $routeProvider.when('/TaskNew', {
            templateUrl: 'partials/Private/TaskNew.html',
            resolve: RouteAccessProvider.routeResolvers
        });
        $routeProvider.when('/Other', {
            templateUrl: 'partials/Private/Other.html',
            resolve: RouteAccessProvider.routeResolvers
        });
        $routeProvider.otherwise({
            redirectTo: '/Login'
        });

    }
]);
