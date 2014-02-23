'use strict';

/* Services */


angular.module('myAuth.services', ['ngCookies'])
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
        }
    });
