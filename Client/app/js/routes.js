'use strict';

angular.module('myApp')


.config(['$routeProvider', 'RouteAccessProvider',
    function($routeProvider, RouteAccessProvider) {

        RegisterDynamicPlugin($routeProvider);

        function RegisterDynamicPlugin($routeProvider) {
            //see https://coderwall.com/p/y0zkiw
            $routeProvider.when('/tasks/items', {
                templateUrl: 'plugins/tasks/html/items.html',
                resolve: {
                    load: ['$q', '$rootScope',
                        function($q, $rootScope) {
                            var deferred = $q.defer();
                            // At this point, use whatever mechanism you want 
                            // in order to lazy load dependencies. e.g. require.js
                            // In this case, "itemsController" won't be loaded
                            // until the user hits the '/items' route
                            require(['plugins/tasks/js/itemsController'], function() {
                                $rootScope.$apply(function() {
                                    deferred.resolve();
                                });
                            });
                            return deferred.promise;
                        }
                    ]
                }
            });
        }

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
        $routeProvider.when('/Logout', {
            controller: "LogoutCtrl",
            template: "<div></div>"
        });
        $routeProvider.when('/Exit', {
            controller: function() {
                window.setTimeout(function() {
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
