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

        if (!validate(user)) {
            showError("Invalid inputs");
            return;
        }

        updateTitle();

        $location.path('/RegisterOk');
    };

    function validate(user) {
        if (user == null)
            return false;
        return user.Email != "" && user.Password != "" && user.RePassword;
    }

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