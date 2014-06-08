'use strict';

/* Controllers */

angular.module('myApp.controllers')

.controller('AppBootstrapCtrl', ['Auth', '$scope', '$location',
    function(Auth, $scope, $location) {

        if (Auth.isLoggedIn()) {
            $location.path('/Home');
        } else {
            $location.path('/Exit');
        }
    }
]);