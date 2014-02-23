'use strict';

/* Services */

angular.module('myAuth.services', ['ngCookies', 'ngStorage'])

.run(['$rootScope', '$location', 'Auth',
    function($rootScope, $location, Auth) {

        //more details here - http://docs.angularjs.org/api/ngRoute/service/$route
        $rootScope.$on("$routeChangeStart", function(event, next, current) {
            if (!Auth.authorize(next.accessRights)) {
                if (Auth.isLoggedIn()) $location.path('Home');
                else {
                    //todo cip (do we need to verify here?)
                    if (next.OriginalPath != "/Login")
                        $location.path('/Login');
                }
            }
        });
    }
])

.factory('AccessRights', function() {
    return {
        Public: 1,
        Registered: 2,
        Right1: 4,
        Right2: 8
    };
})

.factory('CurrentUserSession', function($sessionStorage) {
    //todo cip is it ok to store this in cache? what if it expires

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

    if (username != null)
        loggedIn = true;

    return {
        authorize: function(rights) {
            var currentUserRights = CurrentUserSession.getRights();
            return (currentUserRights & rights) > 0;
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
});
