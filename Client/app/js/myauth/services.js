'use strict';

/* Services */

angular.module('myAuth.services', ['ngCookies', 'ngStorage'])

.run(['$rootScope', '$location', 'Auth',
    function ($rootScope, $location, Auth) {

        //more details here - http://docs.angularjs.org/api/ngRoute/service/$route
        $rootScope.$on("$routeChangeStart", function (event, next, current) {
            if (next.originalPath == "/Error" || next.originalPath == "/AppBootstrap")
                return;

            if (!Auth.authorize(next.accessRights)) {
                if (Auth.isLoggedIn()) $location.path('Home');
                else {
                    //todo cip (do we need to verify here?)
                    if (next.originalPath != "/Login")
                        $location.path('/Login');
                }
            }
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

.factory('CurrentUserSession', function ($sessionStorage) {

    return {
        getRights: function () {
            //todo cip - what if is undefined?
            var ret = $sessionStorage.UserRights;
            return ret;
        },
        setRights: function (val) {
            $sessionStorage.UserRights = val;
        },
        getUser: function () {
            //todo cip - what if is undefined?
            var ret = $sessionStorage.Username;
            return ret;
        },
        setUser: function (val) {
            $sessionStorage.Username = val;
        },

    };
})

.factory('Auth', function ($http, $cookieStore, AccessRights, CurrentUserSession) {
    var loggedIn = false;
    var username = CurrentUserSession.getUser();

    var InitStorage = function () {
        var rights = CurrentUserSession.getRights();
        if (rights === undefined)
            CurrentUserSession.setRights(AccessRights.Public);
    }

    InitStorage();

    if (username != null)
        loggedIn = true;

    return {
        authorize: function (rights) {
            var currentUserRights = CurrentUserSession.getRights();
            return (currentUserRights & rights) > 0;
        },
        RoutesToRights: function (routes) {
            var routesMapping = [
                {
                    right: AccessRights.Projects,
                    routes: [
                        { get: '/projects' },
                    ]
                },{
                    right: AccessRights.Tasks,
                    routes: [
                        { get: '/tasks' },
                    ]
                }
            ];

            return 0;
        },
        login: function (username, rights) {
            if (username != "" && username != null) {
                CurrentUserSession.setUser(username);
                CurrentUserSession.setRights(rights);
                loggedIn = true;
            } else {
                CurrentUserSession.setUser(null);
                CurrentUserSession.setRights(AccessRights.Public);
            }
        },
        logout: function () {
            CurrentUserSession.setUser(null);
            CurrentUserSession.setRights(AccessRights.Public);
            loggedIn = false;
        },
        isLoggedIn: function () {
            return loggedIn;
        }
    }
})

.factory('AllowedRoutes', function ($resource) {
    return $resource(baseUrl + '/allowedRoutes', {}, {
        get: {
            method: 'GET'
        },
    });
});
