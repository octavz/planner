'use strict';

/* Services */

angular.module('myAuth.services', ['ngCookies', 'ngStorage', 'ngResource'])

.run(['$rootScope', '$location', 'Auth',
    function($rootScope, $location, Auth) {
        $rootScope.$on("$routeChangeError", function(event, current) {
            $location.url("/Error");
        });
        $rootScope.$on("$routeChangeStart", function(event, next, current) {
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

.factory('CurrentUserSession', function($sessionStorage) {

    return {
        getUser: function() {
            //todo cip - what if is undefined?
            var ret = $sessionStorage.Username;
            return ret;
        },
        setUser: function(val) {
            $sessionStorage.Username = val;
        },

    };
})

.factory('Auth', function($http, $cookieStore, CurrentUserSession) {
    var loggedIn = false;
    var username = CurrentUserSession.getUser();

    if (username != null)
        loggedIn = true;

    return {
        login: function(username) {
            if (username != "" && username != null) {
                CurrentUserSession.setUser(username);
                loggedIn = true;
            } else {
                CurrentUserSession.setUser(null);
            }
        },
        logout: function() {
            CurrentUserSession.setUser(null);
            loggedIn = false;
        },
        isLoggedIn: function() {
            return loggedIn;
        }
    }
})

.factory('RouteAccessApi', function($resource) {
    return $resource('json/RoutesAccess.json', {}, {
        get: {
            method: 'GET'
        },
    });
})

.provider("RouteAccess",
    function() {
        var skipRoutes = ["/", "/Error", "/AppBootstrap"];

        var hasAccess = function($q, $route, RouteAccessApi) {

            var routeToVerify = $route.current.originalPath;
            if (_(skipRoutes).contains(routeToVerify))
                return;

            var asyncVerify = $q.defer();
            RouteAccessApi.get().$promise.then(function(data) {

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
            $get: function() {
                return 1;
            }
        };
    }
);
