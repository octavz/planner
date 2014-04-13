'use strict';

/* Controllers */

angular.module('myAppPublic.controllers')

.controller('RegisterCtrl', ['$scope', '$location', 'UsersApi',
    function($scope, $location, UsersApi) {

        $scope.alerts = [];

        $scope.title = 'Welcome';

        $scope.update = function(user) {
            $scope.master = angular.copy(user);
            $scope.alerts = [];

            if (!$scope.registerForm.$valid) {
                return;
            }

            UsersApi.register({
                Email: user.Email,
                Password: user.Password,
            }, function(data, headers) {
                if (typeof(data.error) !== "undefined") {
                    $scope.alerts.push({ type: 'danger', msg: 'Oh snap! Change a few things up and try submitting again.' });
                    return;
                }

                if (data.ok) {
                    $location.path('/RegisterOk');
                }

            });
        };
    }
]);
