'use strict';

var module = angular.module('myApp.utils', []);
module.service('CurrentView', [
    '$location', '$http', '$log', '$route', function ($location, $http, $log, $route) {
        var detect = function () {

            var userUrlMatches = [];
            userUrlMatches.push(/^\/[^\/]+\/Projects[\/]{0,1}$/);
            userUrlMatches.push(/^\/[^\/]+\/ProjectNew[\/]{0,1}$/);
            userUrlMatches.push(/^\/[^\/]+\/MySettings[\/]{0,1}$/);
            userUrlMatches.push(/^\/[^\/]+\/Details[\/]{0,1}$/);
            userUrlMatches.push(/^\/[^\/]*[\/]{0,1}$/);

            var $bIsUser = false;
            var i, l;
            for (i = 0; i < (l = userUrlMatches.length) ; i++)
                $bIsUser = $bIsUser || userUrlMatches[i].test($location.$$path);
            //$log.debug($location, $bIsUser);
            return $bIsUser;
        };

        //var detect2 = function () {

        //    var IsUserRoute = function (routeParams) {
        //        //var routeParams = route.params;
        //        return routeParams.usercode != null && routeParams.projectcode == null;
        //    }
        //    var IsUserProjectRoute = function (routeParams) {
        //        //var routeParams = route.params;
        //        return routeParams.usercode != null && routeParams.projectcode != null;
        //    }
        //    var currentRouteParams = $route.current.params;

        //    if (IsUserRoute(currentRouteParams))
        //        return true;
        //    else if (IsUserProjectRoute(currentRouteParams))
        //        return false;
        //    else {
        //        $log.error("no user no project");
        //        return false;
        //    }

        //}
        return {
            IsUser: function () {
                return detect();
            }
        }
    }
    ]);