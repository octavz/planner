'use strict';

angular.module('myApp.plugins.projects')

.controller('ProjectInsert',
    ['$scope', '$location', 'ProjectsApi', '$log', 'ProjectsSrv',
    function ($scope, $location, ProjectsApi, $log, ProjectsSrv) {


        ProjectsApi.get(function (resp) {
            $scope.projects = _(resp.data).map(ProjectsSrv.TransformProjects);
        });


        $scope.title = 'Insert a new project';
        $scope.save = function () {

            if ($scope.projectForm.$invalid) {
                return;
            }

            ProjectsApi.insertProject({
                name: $scope.project.Name,
                desc: $scope.project.Description,
                parent: $scope.project.Parent,
            }
            , function (data, headers) {
                if (typeof (data.error) !== "undefined") {
                    return;
                }
                if (data.ok) {
                    $scope.RedirectProject(data.o);
                }
            });
        }
    }]);