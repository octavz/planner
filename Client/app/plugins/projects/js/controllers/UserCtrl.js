'use strict';

angular.module('myApp.plugins.projects.controllers')

.controller('UserCtrl', ['$scope', '$location', '$window', '$routeParams', 'ProjectsApi', 'SiteMap', function ($scope, $location, $window, $routeParams, ProjectsApi, SiteMap) {
    $scope.username = "User 1";
}]);