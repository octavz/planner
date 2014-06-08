'use strict';


// Declare app level module which depends on filters, and services
angular.module('myAppLogin', [
  'myAppLogin.controllersLogin'
]);

angular.module('myAppLogin.controllersLogin', [])
.controller('LoginCtrl', ['$scope', '$location', function ($scope, $location) {
    $scope.title = 'Login';
    $scope.goNext = function (navPath) {
        $location.path(navPath);
    }
}]);
