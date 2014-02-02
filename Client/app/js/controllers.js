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
            showError("Invalid inputs");
            return;
        }

        updateTitle();

        $location.path('/RegisterOk');
    };

    function showError(msg) {
        $scope.hasError = true;
        $scope.ErrorMessage = msg;
    }

    function updateTitle() {
        $scope.title = 'Welcome ' + $scope.master.Email;
    }
}])
.controller('RegisterOkCtrl', [function () {

}])
;