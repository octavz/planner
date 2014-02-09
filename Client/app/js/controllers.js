'use strict';

/* Controllers */

angular.module('myApp.controllers', [])
.controller('MyCtrl1', [function () {

}])
.controller('MyCtrl2', [function () {

}])
.controller('RegisterCtrl', ['$scope', '$location', function ($scope, $location) {

    $scope.title = 'Welcome';

    $scope.update = function (user) {
        $scope.master = angular.copy(user);

        if (!$scope.registerForm.$valid)
        {
            return;
        }

        $location.path('/RegisterOk');
    };

}])
.controller('RegisterOkCtrl', [function () {

}])
;