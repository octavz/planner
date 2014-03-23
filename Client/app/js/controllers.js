'use strict';

/* Controllers */

angular.module('myApp.controllers', [])



.controller('LoginMockCtrl', ['$scope', '$location', 'Auth',
    function($scope, $location, Auth) {
        $scope.Login = function(user) {
            if (user.Email != "") {
                Auth.login(user.Email);
                $location.path('/AppBootstrap');
            }
        }
    }

]);
