'use strict';

/* Controllers */

angular.module('myApp.controllers')

.controller('TopMenuCtrl', ['Auth', '$rootScope', '$scope', '$location', 'SiteMap',
    function(Auth, $rootScope, $scope, $location, SiteMap) {

        SiteMap.getLinks().then(function(links) {
            console.log("TopMenuCtrl has some links", links);
            $scope.menuItems = links;
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
