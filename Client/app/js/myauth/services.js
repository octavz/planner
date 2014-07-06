'use strict';

/* Services */

angular.module('myAuth.services', ['ngCookies', 'ngStorage', 'ngResource'])

.run(['$rootScope', '$location', 'Auth', '$window', '$log',
    function ($rootScope, $location, Auth, $window, $log) {
        $rootScope.$on("$routeChangeError", function (event, current, zet, ex) {
            if (ex == "red") {
                //$window.location = $window.location;
                //$location.url("/Error");
                $log.debug("reload");
                $window.location.reload();
                //$log.debug("reload2");
                return;
            }

            $location.url("/Error");
        });
        $rootScope.$on("$routeChangeStart", function (event, next, current) {
            $log.debug("$routeChangeStart");
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
        

        var hasAccess = function ($q, $route, RouteAccessApi, $log) {
            $log.debug('hasAccess');

            var skipRoutes = ["/", "/Error", "/AppBootstrap"];

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

        var isaSwitchBetweenUserAndProject = function ($q, $route, RouteAccessApi, $log, $routeParams) {

            $log.debug('isaSwitchBetweenUserAndProject');

            var asyncVerify = $q.defer();
            RouteAccessApi.get().$promise.then(function (res) {


                var IsUserRoute = function (routeParams) {
                    //var routeParams = route.params;
                    return routeParams.usercode != null && routeParams.projectcode == null;
                }
                var IsUserProjectRoute = function (routeParams) {
                    //var routeParams = route.params;
                    return routeParams.usercode != null && routeParams.projectcode != null;
                }
                var currentRouteParams = $route.current.params;
                var prevRouteParams = $routeParams;

                if (IsUserRoute(currentRouteParams) && IsUserProjectRoute(prevRouteParams)) {
                    return asyncVerify.reject('red');
                }
                if (IsUserProjectRoute(currentRouteParams) && IsUserRoute(prevRouteParams)) {
                    return asyncVerify.reject('red');
                }
                asyncVerify.resolve();
            });

            return asyncVerify.promise;
        };


        return {
            routeResolvers: {
                hasAccessResolver: hasAccess,
                //todo cip ! move this resolver out of the myauth - because is more related to Sitemap
                isaSwitchResolver: isaSwitchBetweenUserAndProject
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
