'use strict';

angular.module('myApp.plugins.projects.controllers')

.controller('UserCtrl', ['$scope', '$location', '$window', '$routeParams', 'ProjectsApi', 'SiteMap', function ($scope, $location, $window, $routeParams, ProjectsApi, SiteMap) {
    $scope.username = "User 1";
    var TransformProjects = function (proj) {
        var ret = proj;
        ret.link = SiteMap.GetAbsolutePath(proj.code);
        return ret;
    }

    ProjectsApi.get(function (resp) {
        $scope.projects = _(resp.data).map(TransformProjects);
    });
}]);