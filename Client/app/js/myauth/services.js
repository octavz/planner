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

.constant('AuthConstants', {
    AuthCookieName: 'access_token',
    AuthHttpHeader: 'access_token',
})

.factory('Auth', function ($http, $cookies, AuthConstants) {
    return {
        logout: function () {
            $cookies[AuthConstants.AuthCookieName] = undefined;
        },
        isLoggedIn: function () {
            var token = $cookies[AuthConstants.AuthCookieName];
            return token != null && token != "";
        }
    };
})

.factory('RouteAccessApi', function ($resource) {
    return $resource('/routes', {}, {
        get: {
            method: 'GET'
        },
    });
})

.provider("RouteAccess",
    function () {
        var skipRoutes = ["/", "/Error", "/AppBootstrap"];

        var hasAccess = function ($q, $route, RouteAccessApi, $log) {

            var routeToVerify = $route.current.originalPath;
            if (_(skipRoutes).contains(routeToVerify))
                return;

            var asyncVerify = $q.defer();
            RouteAccessApi.get().$promise.then(function (res) {
                var routeToVerifyHashed = calcMD5(routeToVerify);

                var isAllowed = _(res.data).contains(routeToVerifyHashed);

                if (isAllowed)
                    asyncVerify.resolve();
                else {
                    $log.debug("no right for this route", $route);
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
)

.factory('AuthInterceptor', function ($rootScope, $q, $window, AuthConstants, $cookies) {
    return {
        request: function (config) {
            config.headers = config.headers || {};
            var authToken = $cookies[AuthConstants.AuthCookieName];
            if (authToken != undefined) {
                config.headers[AuthConstants.AuthHttpHeader] = authToken;
            }
            return config;
        },
        response: function (response) {
            if (response.status === 401) {
                //todo cip should we do something here?
                // handle the case where the user is not authenticated
            }
            return response || $q.when(response);
        }
    };
})

.config(function ($httpProvider) {
    $httpProvider.interceptors.push('AuthInterceptor');
});
