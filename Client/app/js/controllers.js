'use strict';

/* Controllers */

angular.module('myApp.controllers', [])

.controller('RegisterCtrl', ['$scope', '$location', 'UsersFactory', 'UserFactory', function ($scope, $location, UsersFactory, UserFactory) {

    $scope.title = 'Welcome';

    $scope.update = function (user) {
        $scope.master = angular.copy(user);

        if (!$scope.registerForm.$valid) {
            return;
        }

        //var userCall = UsersFactory.create(user);
        //todo cip should we take from user.email? or from the scope, or from where?
        var userCall = UsersFactory.create({ Email: user.Email }
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

