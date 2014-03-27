'use strict';

angular.module('myApp.controllers')

.controller('ProjectsCtrl', ['$scope', '$location', 'ProjectsApi', function ($scope, $location, ProjectsApi) {
    $scope.title = 'Projects';

    ProjectsApi.get(function (resp) {
        $scope.projects = resp.data;
    });

    $scope.save = function(){

        if ($scope.projectForm.$invalid) {
            return;
        }

        ProjectsApi.saveProject({
            Name: $scope.project.Name,
            Description: $scope.project.Description,
        }
        , function (data, headers) {
            if (typeof (data.error) !== "undefined") {
                return;
            }
            if (data.ok) {
                $location.path('index.html');
            }
        });
    }
}]);