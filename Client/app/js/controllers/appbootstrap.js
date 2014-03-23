'use strict';

/* Controllers */

angular.module('myApp.controllers')

.controller('AppBootstrapCtrl', ['$scope', '$location',
    function($scope, $location) {
        $location.path('/Home');
    }
]);
