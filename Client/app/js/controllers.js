'use strict';

/* Controllers */

angular.module('myApp.controllers', [])

.controller('RegisterCtrl', ['$scope', '$location', 'UsersApi', function ($scope, $location, UsersApi) {

    $scope.title = 'Welcome';

    $scope.update = function (user) {
        $scope.master = angular.copy(user);

        if (!$scope.registerForm.$valid) {
            return;
        }

        UsersApi.register({
            Email: user.Email,
            Password: user.Password,
        }
        , function (data, headers) {
            if (typeof (data.error) !== "undefined") {
                return;
            }

            if (data.ok) {
                $location.path('/RegisterOk');
            }

        });

    };

}])
.controller('RegisterOkCtrl', [function () {

}]);

