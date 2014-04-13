'use strict';

/* Services */

angular.module('myAuth.services', ['ngCookies', 'ngStorage', 'ngResource'])

.run(['$rootScope', '$location', 'Auth',
    function ($rootScope, $location, Auth) {
        $rootScope.$on("$routeChangeError", function (event, current) {
            $location.url("/Error");
        });
        $rootScope.$on("$routeChangeStart", function (event, next, current) {
            var skipRoutes = ["/", "/Error", "/AppBootstrap"];
            var routeToVerify = next.$$route.originalPath;

            if (_(skipRoutes).contains(routeToVerify))
                return;

            if (!Auth.isLoggedIn()) {
                $location.path('/Logout');
            };
        });
    }
])


.factory('Auth', function ($http, $cookies) {
    return {
        logout: function () {
            $cookies.token = undefined;
        },
        isLoggedIn: function () {
            var token = $cookies.token;
            return token != null && token != "";
        }
    }
})

.factory('RouteAccessApi', function ($resource) {
    return $resource('json/RoutesAccess.json', {}, {
        get: {
            method: 'GET'
        },
    });
})

.provider("RouteAccess",
    function () {
        var skipRoutes = ["/", "/Error", "/AppBootstrap"];

        var hasAccess = function ($q, $route, RouteAccessApi) {

            var routeToVerify = $route.current.originalPath;
            if (_(skipRoutes).contains(routeToVerify))
                return;

            var asyncVerify = $q.defer();
            RouteAccessApi.get().$promise.then(function (data) {

                var isAllowed = _(data.routes).contains(routeToVerify);

                if (isAllowed)
                    asyncVerify.resolve();
                else {
                    console.log("no right for this route", $route);
                    asyncVerify.reject();
                }
            });

            return asyncVerify.promise;
        };

        return {
            routeResolvers: {
                resolver1: hasAccess
            },

            //todo cip write something in this function...
            $get: function () {
                return 1;
            }
        };
    }
);
