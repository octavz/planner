'use strict';

/* Controllers */

angular.module('myAppPublic.controllers')

.controller('RegisterCtrl', ['$scope', '$location', 'UsersApi',
    function ($scope, $location, UsersApi) {

        $scope.alerts = [];

        $scope.title = 'Welcome';

        $scope.update = function (user) {
            $scope.master = angular.copy(user);
            $scope.alerts = [];

            if (!$scope.registerForm.$valid) {
                return;
            }

            UsersApi.register({
                email: user.Email,
                password: user.Password,
            }, function (data, headers) {
                //if we have the error property, than we have an error
                if (typeof (data.error) !== "undefined") {
                    $scope.alerts.push({ type: 'danger', msg: data.error });
                    return;
                }
                $location.path('/RegisterOk');
            }, function (error) {
                console.error(error);
                $scope.alerts.push({ type: 'danger', msg: 'Something went wrong, try again.' });
            });
        };
    }
]);
