'use strict';

angular.module('myApp')


.config(['$routeProvider', 'RouteAccessProvider',
    function ($routeProvider, RouteAccessProvider) {
        $routeProvider.when('/', {
            redirectTo: '/AppBootstrap'
        });
        $routeProvider.when('/Home', {
            templateUrl: 'partials/Private/Home.html',
            resolve: RouteAccessProvider.routeResolvers
        });
        $routeProvider.when('/Error', {
            templateUrl: 'partials/Private/Error.html',
            resolve: RouteAccessProvider.routeResolvers
        });
        $routeProvider.when('/AppBootstrap', {
            controller: 'AppBootstrapCtrl',
            template: " ",
            resolve: RouteAccessProvider.routeResolvers
        });
        $routeProvider.when('/Projects', {
            templateUrl: 'partials/Private/Projects.html',
            controller: 'ProjectsCtrl',
            resolve: RouteAccessProvider.routeResolvers
        });
        $routeProvider.when('/ProjectNew', {
            templateUrl: 'partials/Private/ProjectNew.html',
            controller: 'ProjectsCtrl',
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
        $routeProvider.when('/Logout', {
            controller: "LogoutCtrl",
            template: "<div></div>"
        });
        $routeProvider.when('/Exit', {
            controller: function () {
                window.setTimeout(function () {
                    window.location = "Public.html";
                }, 100);
            },
            template: "<div></div>"
        });
        $routeProvider.otherwise({
            redirectTo: '/Home'
        });

    }
]);
