'use strict';

angular.module('myApp.plugins.projects')

.controller('ProjectEdit',
    ['$scope', '$location', '$routeParams', 'ProjectsApi', '$log', 'ProjectsSrv',
    function ($scope, $location, $routeParams, ProjectsApi, $log, ProjectsSrv) {
        var projectcode = $routeParams.projectcode;
        if (projectcode == null) {
            $log.error("invalid projectcode");
        }

        ProjectsApi.get(function (resp) {
            $scope.projects = _(resp.data).map(ProjectsSrv.TransformProjects);
        });

        $scope.title = 'Edit project';
        ProjectsApi.get({ id: projectcode }, function (resp) {
            $log.debug("received something", resp);

            //fill the scope
            $scope.project = {
                Name: resp.data.name,
                Description: resp.data.desc,
                Parent: resp.data.parent
            }
        });

        $scope.save = function () {
            if ($scope.projectForm.$invalid) {
                return;
            }

            //make a post
            ProjectsApi.updateProject({
                id: projectcode,
                name: $scope.project.Name,
                desc: $scope.project.Description,
                parent: $scope.project.Parent,
            }, function (data, headers) {
                if (typeof (data.error) !== "undefined") {
                    return;
                }
                if (data.ok) {
                    //$location.path('/Projects');
                }
            });
        }
    }]);