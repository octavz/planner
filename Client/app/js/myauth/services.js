'use strict';

/* Services */

angular.module('myAuth.services', ['ngCookies'])
    .run(['$rootScope', '$location', 'Auth',
        function($rootScope, $location, Auth) {

        	//more details here - http://docs.angularjs.org/api/ngRoute/service/$route
            $rootScope.$on("$routeChangeStart", function(event, next, current) {
                if (!Auth.authorize(next.accessRights)) {
                    if (Auth.isLoggedIn()) $location.path('/');
                    else{
                    	//todo cip (do we need to verify here?)
                    	if(next.OriginalPath!="/Login")
                    		$location.path('/Login');
                    }
                }
            });
        }
    ])
    .factory('serviceId', function() {
        var shinyNewServiceInstance;
        //factory function body that constructs shinyNewServiceInstance
        return shinyNewServiceInstance;
    })
    .factory('AccessRights', function() {
        return {
            Public: 1,
            Registered: 2,
            Right1: 4,
            Right2: 8
        };
    })
    .factory('CurrentUserSession', function($cacheFactory) {
        //todo cip is it ok to store this in cache? what if it expires
        var cache = $cacheFactory('CurrentUserSession');
        return {
            getRights: function() {
                //todo cip - what if is undefined?
                var ret = cache.get("UserRights");
                console.log("get from cache:", ret, cache.info());
                return ret;
            },
            setRights: function(val) {
                console.log("put in cache", val);
                cache.put("UserRights", val);
            }
        };
    })
    .factory('Auth', function($http, $cookieStore, AccessRights, CurrentUserSession) {
        return {
            authorize: function(rights) {
                var currentUserRights = CurrentUserSession.getRights();
                return (currentUserRights & rights) > 0;
            },
            isLoggedIn: function() {
                //todo cip -change here!
                return false;
            }
        }
    });
