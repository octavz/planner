'use strict';

/* Controllers */

angular.module('myApp.controllers')

.controller('TopMenuCtrl', ['Auth', '$scope', '$location', 'SiteMap',
    function(Auth, $scope, $location, SiteMap) {

        var publicLinks = [{
            'title': 'Login',
            'link': '#/Login'
        }, {
            'title': 'Register',
            'link': '#/Register'
        }];

        if (Auth.isLoggedIn()) {
            SiteMap.getLinks().then(function(links) {
                $scope.menuItems = links;
            })
        } else {
            $scope.menuItems = publicLinks;
        }

        //register this
        $scope.$watch(Auth.isLoggedIn, function(value, oldValue) {

            if (!value && oldValue) {
                console.log("Change menu items (public links");
                // $scope.menuItems = publicLinks;
            }

            if (value) {
                console.log("Change menu items (private links)");
                // $scope.menuItems = SiteMap.menuLinks;
            }

        }, true);

        $scope.isActive = function(menuItem) {
            var loc = "#" + $location.path();
            //            console.log("isactive?", menuItem, loc);
            if (menuItem.link == loc) {
                return 'active';
            }
        }

        $scope.logOut = function() {
            Auth.logout();
            //todo cip ! how do we hide all the screen on logout?
            $location.path('/');
        }

        $scope.isLoggedIn = Auth.isLoggedIn;
    }
]);
