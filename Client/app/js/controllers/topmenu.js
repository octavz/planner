'use strict';

/* Controllers */

angular.module('myApp.controllers')

.controller('TopMenuCtrl', ['Auth', '$rootScope', '$scope', '$location', 'SiteMap','$log',
    function(Auth, $rootScope, $scope, $location, SiteMap, $log) {

        SiteMap.init().then(function () {
            $log.log("a");
            $log.debug("d");
            $log.warn("aw");
            console.log("SiteMap was initialized");
            $scope.SiteMap = SiteMap;
        })

        $scope.isActive = function(menuItem) {
            var loc = "#" + $location.path();
            if (menuItem.link == loc) {
                return 'active';
            }
        }

        $scope.isLoggedIn = Auth.isLoggedIn;
    }
]);
