'use strict';

/* Controllers */

angular.module('myApp.controllers')

.controller('LogoutCtrl', ['Auth', '$scope', '$location',
    function(Auth, $scope, $location) {
        Auth.logout();

        $location.path('/Exit');
    }
]);
