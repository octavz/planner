'use strict';

/* Controllers */

angular.module('myAppPublic.controllers')

.controller('RegisterCtrl', ['$scope', '$location', 'UsersApi', '$log',
    function ($scope, $location, UsersApi, $log) {

        $scope.alerts = [];

        $scope.title = 'Welcome';

        $scope.update = function (user) {
            $scope.master = angular.copy(user);
            $scope.alerts = [];

            if (!$scope.registerForm.$valid) {
                return;
            }

            UsersApi.register({
                login: user.Email,
                password: user.Password,
            }, function (data, headers) {
                //if we have the error property, than we have an error
                if (typeof (data.errMessage) !== "undefined") {
                    $scope.alerts.push({ type: 'danger', msg: data.errMessage });
                    return;
                }
                $location.path('/RegisterOk');
            }, function (error) {
                $log.error(error);
                $scope.alerts.push({ type: 'danger', msg: 'Something went wrong, try again.' });
            });
        };
    }
]);
