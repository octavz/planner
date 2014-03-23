'use strict';

/* Services */

angular.module('myAuth.services', ['ngCookies', 'ngStorage', 'ngResource'])

.run(['$rootScope', '$location',
    function($rootScope, $location) {
        $rootScope.$on("$routeChangeError", function(event, current) {
            $location.url("/Error");
        });
    }
])

//constants
.constant('AccessRights', {
    Public: 1,
    Registered: 2,
    Projects: 4,
    Tasks: 8,
    Users: 16,
})

.factory('CurrentUserSession', function($sessionStorage) {

    return {
        getRights: function() {
            //todo cip - what if is undefined?
            var ret = $sessionStorage.UserRights;
            return ret;
        },
        setRights: function(val) {
            $sessionStorage.UserRights = val;
        },
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

.factory('Auth', function($http, $cookieStore, AccessRights, CurrentUserSession) {
    var loggedIn = false;
    var username = CurrentUserSession.getUser();

    var InitStorage = function() {
        var rights = CurrentUserSession.getRights();
        if (rights === undefined)
            CurrentUserSession.setRights(AccessRights.Public);
    }

    InitStorage();

    if (username != null)
        loggedIn = true;

    return {
        authorize: function(rights) {
            var currentUserRights = CurrentUserSession.getRights();
            return (currentUserRights & rights) > 0;
        },
        RoutesToRights: function(routes) {
            // var routesMapping = [{
            //     right: AccessRights.Projects,
            //     routes: [{
            //         get: '/projects'
            //     }, ]
            // }, {
            //     right: AccessRights.Tasks,
            //     routes: [{
            //         get: '/tasks'
            //     }, ]
            // }];

            return 0;
        },
        login: function(username, rights) {
            if (username != "" && username != null) {
                CurrentUserSession.setUser(username);
                CurrentUserSession.setRights(rights);
                loggedIn = true;
            } else {
                CurrentUserSession.setUser(null);
                CurrentUserSession.setRights(AccessRights.Public);
            }
        },
        logout: function() {
            CurrentUserSession.setUser(null);
            CurrentUserSession.setRights(AccessRights.Public);
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

// .factory('RouteAccess', function($resource) {
//     return $resource( '/json/RouteAccess.json', {}, {
//         get: {
//             method: 'GET'
//         },
//     });
// })

.provider("RouteAccess",
    function() {
        var skipRoutes = ["/Home", "/Error", "/AppBootstrap"];

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
