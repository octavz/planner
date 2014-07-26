'use strict';

angular.module('myApp.plugins.projects')

.controller('Projects',
    ['$scope', '$location', 'ProjectsApi', '$log', 'ProjectsSrv',
    function ($scope, $location, ProjectsApi, $log, ProjectsSrv) {

        ProjectsApi.get(function (resp) {
            $scope.projects = _(resp.data).map(ProjectsSrv.TransformProjects);
        });

    }]);