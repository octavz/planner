'use strict';

var module = angular.module('myApp.utils', []);
module.service('CurrentView', [
    '$location', '$http', '$log', function ($location, $http, $log) {
        var detect = function () {

            var userUrlMatches = [];
            userUrlMatches.push(/^\/[^\/]+\/Projects[\/]{0,1}$/);
            userUrlMatches.push(/^\/[^\/]+\/ProjectNew[\/]{0,1}$/);
            userUrlMatches.push(/^\/[^\/]+\/Details[\/]{0,1}$/);
            userUrlMatches.push(/^\/[^\/]*[\/]{0,1}$/);

            var $bIsUser = false;
            var i, l;
            for (i = 0; i < (l = userUrlMatches.length) ; i++)
                $bIsUser = $bIsUser || userUrlMatches[i].test($location.$$path);
            $log.debug($location);
            return $bIsUser;
        };

        return {
            IsUser: function () {
                return detect();
            },
        }
    }
]);