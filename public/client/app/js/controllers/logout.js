'use strict';

/* Controllers */

angular.module('myApp.controllers')

.controller('LogoutCtrl', ['Auth', '$scope', '$location',
    function(Auth, $scope, $location) {
        Auth.logout();


        //todo cip ! how do we hide all the screen on logout?
        //todo cip dont forgot to clear everything on logout
        $location.path('/Exit');
    }
]);
