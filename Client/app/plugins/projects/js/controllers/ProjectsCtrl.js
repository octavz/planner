'use strict';

angular.module('myApp.plugins.projects.controllers', [
    'myApp.plugins.projects.rest'
])

.controller('ProjectsCtrl', ['$scope', '$location', '$routeParams', 'ProjectsApi', function ($scope, $location, $routeParams, ProjectsApi) {

    var id = $routeParams.id;
    if (id != null) {
        $scope.title = 'Edit project';
        ProjectsApi.get({ id: id }, function (resp) {
            console.log("received something", resp);

            //fill the scope
            $scope.project = {
                Name: resp.data.name,
                Description: resp.data.desc,
                Parent: resp.data.parent
            }
        });

    } else {
        $scope.title = 'Insert a new project';
    }

    ProjectsApi.get(function (resp) {
        $scope.projects = resp.data;
    });

    $scope.save = function () {

        if ($scope.projectForm.$invalid) {
            return;
        }

        if (id != null) {
            //make a post
            ProjectsApi.updateProject({
                id: id,
                name: $scope.project.Name,
                desc: $scope.project.Description,
                parent: $scope.project.Parent,
            }
            , function (data, headers) {
                if (typeof (data.error) !== "undefined") {
                    return;
                }
                if (data.ok) {
                    $location.path('/Projects');
                }
            });
        } else {
            //make a put

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
                    $location.path('/Projects');
                }
            });
        }
    }
}]);