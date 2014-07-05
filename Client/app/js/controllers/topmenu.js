'use strict';

/* Controllers */

angular.module('myApp.controllers')

.controller('TopMenuCtrl', ['Auth', '$rootScope', '$scope', '$location', 'SiteMap', '$log',
    function (Auth, $rootScope, $scope, $location, SiteMap, $log) {

        $scope.homelink = SiteMap.getCurrentUserLink();
        
        //todo cip is it ok to init here?
        SiteMap.init().then(function () {

            $log.debug("SiteMap was initialized");
            $scope.SiteMap = SiteMap;
        })

        $scope.attachTopNodeClass = function (menuItem) {
            var cssClasses = [];
            cssClasses.push(isActive(menuItem));
            //todo cip wouldnt be better to put this in html?
            cssClasses.push(menuItem.items != null ? "dropdown" : "");

            return cssClasses.join(" ");
        }

        var isActive = function (menuItem) {
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
