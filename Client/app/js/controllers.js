'use strict';

/* Controllers */

angular.module('myApp.controllers', [])

.controller('RegisterCtrl', ['$scope', '$location', 'UsersApi',
    function($scope, $location, UsersApi) {

        $scope.title = 'Welcome';

        $scope.update = function(user) {
            $scope.master = angular.copy(user);

            if (!$scope.registerForm.$valid) {
                return;
            }

            UsersApi.register({
                Email: user.Email,
                Password: user.Password,
            }, function(data, headers) {
                if (typeof(data.error) !== "undefined") {
                    return;
                }

                if (data.ok) {
                    $location.path('/RegisterOk');
                }

            });

        };

    }
])

.controller('RegisterOkCtrl', [
    function() {

    }
])

.controller('LoginMockCtrl', ['$scope', '$location', 'Auth',
    function($scope, $location, Auth) {
        $scope.Login = function(user) {
            if (user.Email != "") {
                Auth.login(user.Email);
                $location.path('/AppBootstrap');
            }
        }
    }
])

.controller('HomeCtrl', [
    function() {

    }
])

.controller('OtherCtrl', [
    function() {

    }
])

.controller('TopMenuCtrl', ['Auth', '$scope', '$location',
    function(Auth, $scope, $location) {

        var publicLinks = [{
            'title': 'Login',
            'link': '#/Login'
        }, {
            'title': 'Register',
            'link': '#/Register'
        }];

        var privateLinks = [{
            'title': 'Home',
            'link': '#/Home'
        }, {
            'title': 'Other',
            'link': '#/Other'
        }];

        if (Auth.isLoggedIn()) {
            $scope.menuItems = privateLinks;
        } else {
            $scope.menuItems = publicLinks;
        }

        //register this
        $scope.$watch(Auth.isLoggedIn, function(value, oldValue) {

            if (!value && oldValue) {
                console.log("Change menu items (public links");
                $scope.menuItems = publicLinks;
            }

            if (value) {
                console.log("Change menu items (private links)");
                $scope.menuItems = privateLinks;
            }

        }, true);

        $scope.isActive = function(menuItem) {
            var loc = "#" + $location.path();
            console.log("isactive?", menuItem, loc);
            if (menuItem.link == loc){
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
