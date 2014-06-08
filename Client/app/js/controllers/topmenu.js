'use strict';

/* Controllers */

angular.module('myApp.controllers')

.controller('TopMenuCtrl', ['Auth', '$rootScope', '$scope', '$location', 'SiteMap', '$log',
    function (Auth, $rootScope, $scope, $location, SiteMap, $log) {

        //todo cip is it ok to init here?
        SiteMap.init().then(function () {

            $log.debug("SiteMap was initialized");
            $scope.SiteMap = SiteMap;
        })

        $scope.isActive = function (menuItem) {
            var loc = "#" + $location.path();
            if (menuItem.link == loc) {
                return 'active';
            }
        }

        $scope.RedirectToUser = function () {
            SiteMap.SwitchToUser();
        }


        $scope.isLoggedIn = Auth.isLoggedIn;
    }
]);
